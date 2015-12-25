package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.Application;
import domain.Inventory;
import domain.Item;
import domain.Slider;
import domain.Theme;
import mapper.ThemeMapper;
import play.Logger;
import play.libs.Json;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * implements Service and transaction processing.
 * Created by howen on 15/10/26.
 */
public class ThemeServiceImpl implements ThemeService {

    @Inject
    private ThemeMapper themeMapper;

    //将Json串转换成List
    final static ObjectMapper mapper = new ObjectMapper();

    @Override
    public Optional<List<Theme>> getThemes(int pageSize, int offset) {

        Map<String, Integer> params = new HashMap<>();
        params.put("pageSize", pageSize);
        params.put("offset", offset);
        return Optional.ofNullable(themeMapper.getThemes(params));
    }

    @Override
    public Optional<JsonNode> getThemeList(Long themeId) {
        //先获取主题里的所有itemId
        Theme theme = new Theme();
        theme.setId(themeId);
        Optional<Theme> themeOptional = Optional.ofNullable(themeMapper.getThemeBy(theme));
        if (themeOptional.isPresent()) {
            theme = themeOptional.get();
            List<Map> list = new ArrayList<>();
            JsonNode jsonNode = Json.parse(theme.getThemeItem());
            for (final JsonNode objNode : jsonNode) {
                //获取每一个Item
                Item item = new Item();
                item.setId(objNode.asLong());
                item = themeMapper.getItemBy(item);
                //找到每一个主库存信息
                Inventory inventory = new Inventory();
                inventory.setItemId(item.getId());
                inventory.setOrMasterInv(true);
                List<Inventory> inventoryList = themeMapper.getInvBy(inventory);
                if (inventoryList.size() != 0) {
                    //去找到主sku
                    inventory = inventoryList.get(0);
                    Map<String, Object> map = new HashMap<>();
                    map.put("themeId", theme.getId());
                    map.put("itemId", item.getId());

                    map.put("itemTitle", item.getItemTitle());//主商品标题
                    map.put("itemPrice", inventory.getItemPrice().setScale(2, BigDecimal.ROUND_DOWN).toPlainString());//主sku价格
                    map.put("itemImg", Application.IMAGE_URL + inventory.getInvImg());//主sku图片
                    map.put("itemSrcPrice", inventory.getItemSrcPrice().setScale(2, BigDecimal.ROUND_DOWN).toPlainString());//主sku原价
                    map.put("itemDiscount", inventory.getItemDiscount().setScale(1, BigDecimal.ROUND_DOWN).toPlainString());//主sku的折扣
                    map.put("itemSoldAmount", inventory.getSoldAmount());//主sku的销量
                    map.put("itemUrl", Application.DEPLOY_URL + "/comm/detail/" + item.getId());//主sku的销量
                    map.put("itemUrlAndroid", Application.DEPLOY_URL + "/comm/detail/web/" + item.getId());//主sku的销量
                    map.put("collectCount", item.getCollectCount());//商品收藏数
                    if (item.getId().equals(theme.getMasterItemId())) {
                        JsonNode jsonNode1 = Json.parse(theme.getMasterItemTag());
                        if (jsonNode.isArray()) {
                            for (final JsonNode url : jsonNode1) {
                                if (url.has("url")) {
                                    ((ObjectNode) url).put("url", Application.DEPLOY_URL + url.findValue("url").asText());
                                }
                            }
                        }
                        map.put("masterItemTag", jsonNode1.toString());//如果是主宣传商品,增加tag
                        map.put("orMasterItem", true);
                        map.put("itemMasterImg", Application.IMAGE_URL + theme.getThemeMasterImg());//主题主商品宣传图
                    }

                    //遍历所有sku状态,如果所有sku状态均为下架或者售空,则提示商品是售空或者下架
                    Inventory invState = new Inventory();
                    inventory.setItemId(item.getId());
                    List<Inventory> invStateList = themeMapper.getInvBy(inventory);
                    int countK=0;
                    int countD=0;
                    int count=0;
                    //单个sku状态  'Y'--正常,'D'--下架,'N'--删除,'K'--售空
                    for (Inventory inv:invStateList){
                        switch (inv.getState()) {
                            case "Y":
                                map.put("state", "Y");//商品状态
                                break;
                            case "K":
                                countK++;
                                break;
                            case "D":
                                countD++;
                                break;
                            default:
                                count++;
                                break;
                        }
                    }
                    if (count==invStateList.size()){
                        map.put("state", "K");//商品状态
                    }
                    if (countK==invStateList.size()){
                        map.put("state", "K");//商品状态
                    }
                    if (countD==invStateList.size()){
                        map.put("state", "D");//商品状态
                    }

                    map.put("invWeight", inventory.getInvWeight());//商品重量
                    map.put("invCustoms", inventory.getInvCustoms());//报关单位
                    map.put("invArea", inventory.getInvArea());//仓储名称
                    map.put("invAreaNm", inventory.getInvAreaNm());//仓储名称
                    map.put("postalTaxRate", inventory.getPostalTaxRate());//发货仓库
                    list.add(map);
                }
            }
            return Optional.ofNullable(Json.toJson(list));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Slider>> getSlider() {
        return Optional.ofNullable(themeMapper.getSlider());
    }

    /**
     * 组装返回详细页面数据
     *
     * @param id 商品主键
     * @return map
     */
    @Override
    public Optional<Map<String, Object>> getItemDetail(Long id, Long skuId) {

        Item item = new Item();
        item.setId(id);

        Map<String, Object> map = new HashMap<>();
        Optional<Item> itemOptional = Optional.ofNullable(themeMapper.getItemBy(item));
        if (itemOptional.isPresent()) {
            try {
                item = itemOptional.get();
                //将Json字符串转成list
                List<String> itemDetailImgsList = mapper.readValue(item.getItemDetailImgs(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));

                //使用Java8 Stream写法,增加图片地址前缀
                item.setItemDetailImgs(Json.toJson(itemDetailImgsList.stream().map((s) -> {
                    Logger.error("详情页URL: "+Application.IMAGE_URL + s);
                    return Application.IMAGE_URL + s;
                }).collect(Collectors.toList())).toString());

                item.setItemMasterImg(Application.IMAGE_URL + item.getItemMasterImg());

                map.put("main", item);

                Inventory inventory = new Inventory();
                inventory.setItemId(item.getId());

                //遍历库存list 对其进行相应的处理
                List<Inventory> list = themeMapper.getInvBy(inventory).stream().map(l -> {

                    //拼接sku链接
                    if (null != l.getInvUrl() && !"".equals(l.getInvUrl())) {
                        l.setInvUrl(controllers.Application.DEPLOY_URL + l.getInvUrl());
                    } else {
                        l.setInvUrl(controllers.Application.DEPLOY_URL + "/comm/detail/" + id + "/" + l.getId());
                    }

                    //SKU图片
                    l.setInvImg(Application.IMAGE_URL + l.getInvImg());

                    //判断是否是当前需要显示的sku
                    if (!skuId.equals(((Integer) (-1)).longValue()) && !l.getId().equals(skuId)) {
                        l.setOrMasterInv(false);
                    } else if (l.getId().equals(skuId)) {
                        l.setOrMasterInv(true);
                    }

                    //将Json字符串转成list
                    List<String> previewList = new ArrayList<>();
                    try {
                        previewList = mapper.readValue(l.getItemPreviewImgs(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                    } catch (IOException e) {
                        Logger.error("getItemDetail->list: " + e.getMessage());
                    }
                    //使用Java8 Stream写法,增加图片地址前缀
                    l.setItemPreviewImgs(Json.toJson(previewList.stream().map((s) -> Application.IMAGE_URL + s).collect(Collectors.toList())).toString());
                    return l;
                }).collect(Collectors.toList());

                map.put("stock", list);
                return Optional.of(map);
            } catch (Exception ex) {
                Logger.error("getItemDetail: " + ex.getMessage());
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * 组装返回详细页面数据
     *
     * @param id 商品主键
     * @return map
     */
    @Override
    public Optional<Map<String, Object>> getItemDetailWeb(Long id, Long skuId) {

        Item item = new Item();
        item.setId(id);

        Map<String, Object> map = new HashMap<>();
        Optional<Item> itemOptional = Optional.ofNullable(themeMapper.getItemBy(item));
        if (itemOptional.isPresent()) {
            try {
                item = itemOptional.get();
                //将Json字符串转成list
                List<String> itemDetailImgsList = mapper.readValue(item.getItemDetailImgs(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));

                StringBuilder stringBuilder;
                stringBuilder = new StringBuilder("<!DOCTYPE HTML><html><meta charset='UTF-8'><title>Image Canvas</title></head><style>body {margin: 0px;} p {width: 100%;line-height: 24px;font-size: 12px;text-align: left;margin: 0px auto 0 auto;padding: 0;}p img{float: none;margin: 0;padding: 0;border: 0;vertical-align: top;}</style><body><p>");

                //使用Java8 Stream写法,增加图片地址前缀
                itemDetailImgsList.stream().map((s) -> {
                    stringBuilder.append("<img width=\"100%\" src=\"").append(Application.IMAGE_URL).append(s).append("\">");
                    Logger.error("详情页URL: "+Application.IMAGE_URL + s);
                    return Application.IMAGE_URL + s;
                }).collect(Collectors.toList());

                stringBuilder.append("</p></body></html>");

                item.setItemDetailImgs(stringBuilder.toString());

                item.setItemMasterImg(Application.IMAGE_URL + item.getItemMasterImg());

                map.put("main", item);

                Inventory inventory = new Inventory();
                inventory.setItemId(item.getId());

                //遍历库存list 对其进行相应的处理
                List<Inventory> list = themeMapper.getInvBy(inventory).stream().map(l -> {

                    //拼接sku链接
                    if (null != l.getInvUrl() && !"".equals(l.getInvUrl())) {
                        l.setInvUrl(controllers.Application.DEPLOY_URL + l.getInvUrl());
                    } else {
                        l.setInvUrl(controllers.Application.DEPLOY_URL + "/comm/detail/" + id + "/" + l.getId());
                    }

                    //SKU图片
                    l.setInvImg(Application.IMAGE_URL + l.getInvImg());

                    //判断是否是当前需要显示的sku
                    if (!skuId.equals(((Integer) (-1)).longValue()) && !l.getId().equals(skuId)) {
                        l.setOrMasterInv(false);
                    } else if (l.getId().equals(skuId)) {
                        l.setOrMasterInv(true);
                    }

                    //将Json字符串转成list
                    List<String> previewList = new ArrayList<>();
                    try {
                        previewList = mapper.readValue(l.getItemPreviewImgs(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                    } catch (IOException e) {
                        Logger.error("getItemDetail->list: " + e.getMessage());
                    }
                    //使用Java8 Stream写法,增加图片地址前缀
                    l.setItemPreviewImgs(Json.toJson(previewList.stream().map((s) -> Application.IMAGE_URL + s).collect(Collectors.toList())).toString());
                    return l;
                }).collect(Collectors.toList());

                map.put("stock", list);
                return Optional.of(map);
            } catch (Exception ex) {
                Logger.error("getItemDetail: " + ex.getMessage());
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public void setThemeMapper(ThemeMapper themeMapper) {
        this.themeMapper = themeMapper;
    }
}
