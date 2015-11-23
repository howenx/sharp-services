package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.Application;
import domain.*;
import mapper.ThemeMapper;
import play.Logger;
import play.libs.Json;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * implements Service and transaction processing.
 * Created by howen on 15/10/26.
 */
public class ThemeServiceImpl implements ThemeService {

    @Inject
    private ThemeMapper themeMapper;

    @Override
    public List<Theme> getThemes(int pageSize, int offset) {

        Map<String, Integer> params = new HashMap<>();
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return themeMapper.getThemes(params);
    }

    @Override
    public JsonNode getThemeList(Long themeId) {
        //先获取主题里的所有itemId
        Theme theme = new Theme();
        theme.setId(themeId);
        theme = themeMapper.getThemeBy(theme);
        List<Map> list = new ArrayList<>();
        JsonNode jsonNode = Json.parse(theme.getThemeItem());
        for (final JsonNode objNode : jsonNode) {
            //获取每一个Item
            Item item = new Item();
            item.setId(objNode.asLong());
            item = themeMapper.getItemBy(item);
            //找到每一个主库存信息
            Inventory inventory = new Inventory();
            inventory.setId(item.getMasterInvId());
            inventory  = themeMapper.getInvBy(inventory).get(0);
            Map<String,Object> map = new HashMap<>();
            map.put("themeId",theme.getId());
            map.put("itemId",item.getId());
            map.put("itemMasterImg",Application.IMAGE_URL +item.getItemMasterImg());//主题主商品宣传图
            map.put("itemTitle",item.getItemTitle());//主商品标题
            map.put("itemPrice",inventory.getItemPrice());//主sku价格
            map.put("itemImg",Application.IMAGE_URL +inventory.getInvImg());//主sku图片
            map.put("itemSrcPrice",inventory.getItemSrcPrice());//主sku原价
            map.put("itemDiscount",inventory.getItemDiscount());//主sku的折扣
            map.put("itemSoldAmount",inventory.getSoldAmount());//主sku的销量
            map.put("itemUrl",Application.DEPLOY_URL + "/comm/detail/" + item.getId());//主sku的销量
            map.put("collectCount",item.getCollectCount());//商品收藏数
            if (item.getId().equals(theme.getMasterItemId())){
                map.put("masterItemTag",theme.getMasterItemTag());//如果是主宣传商品,增加tag
                map.put("orMasterItem",true);
            }else {
                map.put("masterItemTag","");
                map.put("orMasterItem",false);
            }
            map.put("state",item.getState());//商品状态
            list.add(map);
        }
        return Json.toJson(list);
    }

    @Override
    public List<Slider> getSlider() {
        return themeMapper.getSlider();
    }

    /**
     * 组装返回详细页面数据
     *
     * @param id 商品主键
     * @return map
     */
    @Override
    public Map<String,Object> getItemDetail(Long id, Long skuId) {
        Item item = new Item();
        item.setId(id);

        Map<String,Object> map = new HashMap<>();
        item = themeMapper.getItemBy(item);

        //将Json字符串转成list
        List<String> itemDetailImgsList =new ArrayList<>(json2List(item.getItemDetailImgs(),ArrayList.class,List.class,String.class));

        //使用Java8 Stream写法,增加图片地址前缀
        item.setItemDetailImgs(Json.toJson(itemDetailImgsList.stream().map((s) -> Application.IMAGE_URL+s).collect(Collectors.toList())).toString());

        item.setItemMasterImg(Application.IMAGE_URL+item.getItemMasterImg());

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
            l.setInvImg(Application.IMAGE_URL+l.getInvImg());

            //判断是否是当前需要显示的sku
            if (!skuId.equals(((Integer)(-1)).longValue()) && !l.getId().equals(skuId)) {
                l.setOrMasterInv(false);
            } else if (l.getId().equals(skuId)) {
                l.setOrMasterInv(true);
            }

            //将Json字符串转成list
            List<String> previewList =new ArrayList<>(json2List(l.getItemPreviewImgs(),ArrayList.class,List.class,String.class));

            //使用Java8 Stream写法,增加图片地址前缀
            l.setItemPreviewImgs(Json.toJson(previewList.stream().map((s) -> Application.IMAGE_URL+s).collect(Collectors.toList())).toString());

            return l;

        }).collect(Collectors.toList());

        map.put("stock", list);
        return map;
    }

    /**
     * 获取用户收货地址
     * @param userId 用户token
     * @return  Json
     */
    @Override
    public List<Address> getAddress(Long userId) {
        Address address = new Address();
        address.setUserId(userId);
        return themeMapper.getAddressList(address);
    }

    /**
     * 地址更删改
     * @param address 地址vo
     * @return 是否成功
     */
    @Override
    public Boolean handleAddress(Address address,Integer handle) {
        try{
            if(handle==0){
                themeMapper.updateAddress(address);
            }else if(handle ==1){
                themeMapper.insertAddress(address);

            }else if(handle ==2){
                themeMapper.deleteAddress(address);
            }
            return true;
        }catch (Exception ex){
            Logger.error("1005 地址更删改发生异常"+ex.toString());
            return false;
        }
    }


    //将Json串转换成List
    final static ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> json2List(String json,Class<?> parametrized, Class<?> parametersFor,
                                           Class<?>... parameterClasses){
        List<T> lst = new ArrayList<>();
        try {
                lst = mapper.readValue(json, mapper.getTypeFactory().constructParametrizedType(parametrized,parametersFor, parameterClasses));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lst;
    }

}
