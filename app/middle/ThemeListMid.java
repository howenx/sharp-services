package middle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.SkuVo;
import domain.Theme;
import domain.ThemeBasic;
import domain.ThemeItem;
import modules.SysParCom;
import play.Logger;
import play.libs.Json;
import service.ThemeService;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 中间事务处理层
 * Created by howen on 16/1/25.
 */
public class ThemeListMid {

    @Inject
    private ThemeService themeService;

    /**
     * 获取主图列表,包括拼购
     *
     * @param themeId 主题id
     * @return vo
     */
    public ThemeBasic getThemeList(Long themeId) {

        Theme theme = themeService.getThemeBy(themeId);
        ThemeBasic themeBasic = new ThemeBasic();
        themeBasic.setThemeId(themeId);

        //主题主宣传图拼接URL
        JsonNode jsonNode_ThemeMasterImg = Json.parse(theme.getThemeMasterImg());
        if (jsonNode_ThemeMasterImg.has("url")) {
            ((ObjectNode) jsonNode_ThemeMasterImg).put("url", SysParCom.IMAGE_URL + jsonNode_ThemeMasterImg.get("url").asText());
            themeBasic.setThemeImg(Json.stringify(jsonNode_ThemeMasterImg));//主题主商品宣传图
        }

        //处理主宣传图上的标签跳转连接
        if (theme.getMasterItemTag() != null) {
            JsonNode jsonNodeTag = Json.parse(theme.getMasterItemTag());
            List<JsonNode> tags = new ArrayList<>();
            if (jsonNodeTag.isArray()) {
                for (final JsonNode url : jsonNodeTag) {
                    if (url.has("url")) {
                        JsonNode urlJson = Json.parse(url.get("url").toString());
                        SkuVo skuVo = new SkuVo();
                        skuVo.setSkuType(urlJson.get("type").asText());
                        skuVo.setSkuTypeId(urlJson.get("id").asLong());
                        List<SkuVo> skuVos = themeService.getAllSkus(skuVo);
                        if (skuVos.size() > 0) {
                            skuVo = skuVos.get(0);
                            ((ObjectNode) url).put("url", SysParCom.DEPLOY_URL + "/comm/detail/" + skuVo.getSkuType() + "/" + skuVo.getItemId() + "/" + skuVo.getSkuTypeId());
                            tags.add(url);
                        }
                    }
                }
            }
            themeBasic.setMasterItemTag(Json.stringify(Json.toJson(tags)));
        }

        JsonNode itemJson = Json.parse(theme.getThemeItem());

        List<ThemeItem> themeItems = new ArrayList<>();
        if (itemJson.isArray()) {
            for (JsonNode jn : itemJson) {
                ThemeItem themeItem = getThemeItem(jn.get("type").asText(), jn.get("id").asLong());
                if (themeItem != null) themeItems.add(themeItem);
            }
        }
        themeBasic.setThemeItemList(themeItems);
        return themeBasic;
    }


    /**
     * 获取主题商品
     *
     * @param skuType   skuType
     * @param skuTypeId skuTypeId
     * @return ThemeItem
     */
    private ThemeItem getThemeItem(String skuType, Long skuTypeId) {
        SkuVo skuVo = new SkuVo();
        skuVo.setSkuType(skuType);
        skuVo.setSkuTypeId(skuTypeId);
        List<SkuVo> skuVos = themeService.getAllSkus(skuVo);
        if (skuVos.size() > 0) {
            ThemeItem themeItem = new ThemeItem();
            skuVo = skuVos.get(0);

            themeItem.setCollectCount(skuVo.getCollectCount().intValue());
            themeItem.setItemDiscount(skuVo.getSkuTypeDiscount());
            JsonNode jsonNodeInvImg = Json.parse(skuVo.getSkuTypeImg());
            if (jsonNodeInvImg.has("url")) {
                ((ObjectNode) jsonNodeInvImg).put("url", SysParCom.IMAGE_URL + jsonNodeInvImg.get("url").asText());
                themeItem.setItemImg(Json.stringify(jsonNodeInvImg));
            }
            themeItem.setItemPrice(skuVo.getSkuTypePrice());
            themeItem.setItemSoldAmount(skuVo.getSkuTypeSoldAmount());
            themeItem.setItemSrcPrice(skuVo.getItemSrcPrice());
            themeItem.setItemTitle(skuVo.getSkuTypeTitle());
            themeItem.setItemUrl(SysParCom.DEPLOY_URL + "/comm/detail/" + skuVo.getSkuType() + "/" + skuVo.getItemId() + "/" + skuVo.getSkuTypeId());

            themeItem.setItemType(skuVo.getSkuType());
            themeItem.setState(skuVo.getSkuTypeStatus());//商品状态
            themeItem.setStartAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(skuVo.getSkuTypeStartAt()));
            themeItem.setEndAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(skuVo.getSkuTypeEndAt()));
            return themeItem;
        } else return null;
    }
}
