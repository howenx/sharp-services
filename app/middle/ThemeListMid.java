package middle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.Application;
import domain.*;
import play.Logger;
import play.libs.Json;
import service.CartService;
import service.PromotionService;
import service.ThemeService;

import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 中间事务处理层
 * Created by howen on 16/1/25.
 */
@Singleton
public class ThemeListMid {

    private  CartService cartService;
    private  PromotionService promotionService;
    private  ThemeService themeService;

    private ThemeListMid(){}

    public ThemeListMid(CartService cartService, PromotionService promotionService, ThemeService themeService){
        this.cartService = cartService;
        this.promotionService = promotionService;
        this.themeService = themeService;
    }

    /**
     * 获取主图列表,包括拼购
     * @param themeId 主题id
     * @return vo
     */
    public Optional<ThemeBasic> getThemeList(Long themeId){

        Theme theme = themeService.getThemeBy(themeId);
        ThemeBasic themeBasic = new ThemeBasic();
        themeBasic.setThemeId(themeId);

        JsonNode jsonNode_ThemeMasterImg = Json.parse(theme.getThemeMasterImg());
        if (jsonNode_ThemeMasterImg.has("url")) {
            ((ObjectNode) jsonNode_ThemeMasterImg).put("url", Application.IMAGE_URL + jsonNode_ThemeMasterImg.get("url").asText());
            themeBasic.setThemeImg(Json.stringify(jsonNode_ThemeMasterImg));//主题主商品宣传图
        }

        if(theme.getMasterItemTag()!=null){
            JsonNode jsonNodeTag = Json.parse(theme.getMasterItemTag());
            if (jsonNodeTag.isArray()) {
                for (final JsonNode url : jsonNodeTag) {
                    if (url.has("url")) {
                        JsonNode ujson = Json.parse(url.get("url").toString());
                        if (ujson.get("type").asText().equals("item")){
                            Inventory inventory = new Inventory();
                            inventory.setId( ujson.get("id").asLong());
                            List<Inventory> inventoryList = themeService.getInvBy(inventory);
                            if (inventoryList.size()>0){
                                ((ObjectNode) url).put("url", Application.DEPLOY_URL +"/comm/detail/"+inventoryList.get(0).getItemId()+"/"+ ujson.get("id").asText());
                            }
                        }else if (ujson.get("type").asText().equals("pin")){
                            PinSku pin = promotionService.getPinSkuById(ujson.get("id").asLong());
                            Inventory inv = new Inventory();
                            inv.setId(pin.getInvId());
                            List<Inventory> inventoryList = themeService.getInvBy(inv);

                            if (inventoryList.size()>0){
                                ((ObjectNode) url).put("url", Application.DEPLOY_URL +"/comm/detail/"+inventoryList.get(0).getItemId()+"/"+pin.getInvId()+"/"+ ujson.get("id").asText());
                            }

                        }else if (ujson.get("type").asText().equals("vary")){
                            VaryPrice varyPrice = new VaryPrice();
                            varyPrice.setId(ujson.get("id").asLong());
                            List<VaryPrice> varyPriceList =themeService.getVaryPriceBy(varyPrice);
                            if (varyPriceList.size()>0) {
                                varyPrice = varyPriceList.get(0);
                                Inventory inventory = new Inventory();
                                inventory.setId(varyPrice.getInvId());
                                List<Inventory> inventoryList = themeService.getInvBy(inventory);
                                if (inventoryList.size()>0){
                                    ((ObjectNode) url).put("url", Application.DEPLOY_URL +"/comm/detail/"+inventoryList.get(0).getItemId()+"/"+varyPrice.getInvId()+"/"+ ujson.get("id").asText());
                                }
                            }
                        }
                    }
                }
            }
            themeBasic.setMasterItemTag(Json.stringify(jsonNodeTag));
        }

        JsonNode itemJson = Json.parse(theme.getThemeItem());

        List<ThemeItem>  themeItems = new ArrayList<>();
        if (itemJson.isArray()){
            for (JsonNode jn: itemJson) {
                if (jn.get("type").asText().equals("item")){
                    ThemeItem ti = getItem(jn.get("id").asLong());
                    if (ti!=null){
                        themeItems.add(ti);
                    }

                }else if (jn.get("type").asText().equals("pin")){
                    ThemeItem ti = getPin(jn.get("id").asLong());
                    if (ti!=null){
                        themeItems.add(ti);
                    }
                }else if (jn.get("type").asText().equals("vary")){
                    ThemeItem ti = getVaryPriceInv(jn.get("id").asLong());
                    if (ti!=null){
                        themeItems.add(ti);
                    }
                }
            }
            themeBasic.setThemeItemList(themeItems);
        }
        return Optional.of(themeBasic);
    }


    /**
     * 获取多样化价格
     * @param varyId 多样化价格ID
     * @return themeItem
     */
    private ThemeItem getVaryPriceInv(Long varyId){
        VaryPrice varyPrice = new VaryPrice();
        varyPrice.setId(varyId);
        List<VaryPrice> varyPriceList =themeService.getVaryPriceBy(varyPrice);
        if (varyPriceList.size()>0){
            varyPrice = varyPriceList.get(0);
            //找到每一个主库存信息
            Inventory inventory = new Inventory();
            inventory.setId(varyPrice.getInvId());
            List<Inventory> inventoryList = themeService.getInvBy(inventory);

            if (inventoryList.size()>0){
                //去找到主sku
                inventory = inventoryList.get(0);

                ThemeItem themeItem = new ThemeItem();

                themeItem.setItemId(inventory.getItemId());
                themeItem.setItemTitle(inventory.getInvTitle());
                themeItem.setCollectCount(inventory.getCollectCount());
                themeItem.setItemDiscount(inventory.getItemDiscount());
                JsonNode jsonNodeInvImg = Json.parse(inventory.getInvImg());
                if (jsonNodeInvImg.has("url")) {
                    ((ObjectNode) jsonNodeInvImg).put("url", Application.IMAGE_URL + jsonNodeInvImg.get("url").asText());
                    themeItem.setItemImg(Json.stringify(jsonNodeInvImg));
                }
                themeItem.setItemPrice(varyPrice.getPrice());
                themeItem.setItemSoldAmount(varyPrice.getSoldAmount());
                themeItem.setItemSrcPrice(inventory.getItemSrcPrice());
                themeItem.setItemTitle(inventory.getInvTitle());
                themeItem.setItemUrl(Application.DEPLOY_URL + "/comm/detail/" + inventory.getItemId()+"/"+inventory.getId()+"/"+varyPrice.getId());
                themeItem.setItemUrlAndroid(Application.DEPLOY_URL + "/comm/detail/web/" + inventory.getItemId()+"/"+inventory.getId()+"/"+varyPrice.getId());
                themeItem.setItemType("vary");
                themeItem.setState(varyPrice.getStatus());//商品状态
                themeItem.setStartAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inventory.getStartAt()));
                themeItem.setEndAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inventory.getEndAt()));
                return themeItem;
            }else return null;
        } return null;
    }

    /**
     * 获取拼购商品信息
     * @param pinId 拼购id
     * @return themeItem
     */
    private ThemeItem getPin(Long pinId){

        PinSku pin = promotionService.getPinSkuById(pinId);

        ThemeItem themeItem = new ThemeItem();

        themeItem.setItemId(pin.getPinId());
        themeItem.setItemTitle(pin.getPinTitle());

        Inventory inv = new Inventory();
        inv.setId(pin.getInvId());
        List<Inventory> inventoryList = themeService.getInvBy(inv);

        if (inventoryList.size()>0){
            inv = inventoryList.get(0);
            themeItem.setCollectCount(inv.getCollectCount());
            themeItem.setItemDiscount(pin.getPinDiscount());
            JsonNode jsonNodeInvImg = Json.parse(pin.getPinImg());
            if (jsonNodeInvImg.has("url")) {
                ((ObjectNode) jsonNodeInvImg).put("url", Application.IMAGE_URL + jsonNodeInvImg.get("url").asText());
                themeItem.setItemImg(Json.stringify(jsonNodeInvImg));
            }
            themeItem.setItemPrice(Json.parse(pin.getFloorPrice()).get("price").decimalValue());
            themeItem.setItemSoldAmount(inv.getSoldAmount());
            themeItem.setItemSrcPrice(inv.getItemSrcPrice());
            themeItem.setItemTitle(inv.getInvTitle());
            themeItem.setItemUrl(Application.DEPLOY_URL + "/comm/pin/detail/" + inv.getItemId()+"/"+inv.getId()+"/"+pin.getPinId());
            themeItem.setItemUrlAndroid(Application.DEPLOY_URL + "/comm/pin/detail/web/" + inv.getItemId()+"/"+inv.getId()+"/"+pin.getPinId());
            themeItem.setItemType("pin");
            themeItem.setState(pin.getStatus());//商品状态
            themeItem.setStartAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pin.getStartAt()));
            themeItem.setEndAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pin.getEndAt()));
            return themeItem;
        }else return null;
    }

    /**
     * 获取主题商品
     * @param invId  库存ID
     * @return themeItem
     */
    private ThemeItem getItem(Long invId){

        //找到每一个主库存信息
        Inventory inventory = new Inventory();
        inventory.setId(invId);
        List<Inventory> inventoryList = themeService.getInvBy(inventory);

        if (inventoryList.size() > 0) {
            //去找到主sku
            inventory = inventoryList.get(0);

            ThemeItem themeItem = new ThemeItem();

            themeItem.setItemId(inventory.getItemId());
            themeItem.setItemTitle(inventory.getInvTitle());
            themeItem.setCollectCount(inventory.getCollectCount());
            themeItem.setItemDiscount(inventory.getItemDiscount());
            JsonNode jsonNodeInvImg = Json.parse(inventory.getInvImg());
            if (jsonNodeInvImg.has("url")) {
                ((ObjectNode) jsonNodeInvImg).put("url", Application.IMAGE_URL + jsonNodeInvImg.get("url").asText());
                themeItem.setItemImg(Json.stringify(jsonNodeInvImg));
            }
            themeItem.setItemPrice(inventory.getItemPrice());
            themeItem.setItemSoldAmount(inventory.getSoldAmount());
            themeItem.setItemSrcPrice(inventory.getItemSrcPrice());
            themeItem.setItemTitle(inventory.getInvTitle());
            themeItem.setItemUrl(Application.DEPLOY_URL + "/comm/detail/" + inventory.getItemId()+"/"+inventory.getId());
            themeItem.setItemUrlAndroid(Application.DEPLOY_URL + "/comm/detail/web/" + inventory.getItemId()+"/"+inventory.getId());
            themeItem.setItemType("item");
            themeItem.setState(inventory.getState());//商品状态
            themeItem.setStartAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inventory.getStartAt()));
            themeItem.setEndAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inventory.getEndAt()));
            return themeItem;
        }else  return null;
    }

}
