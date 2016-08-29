package mapper;

import domain.*;

import java.util.List;
import java.util.Map;

/**
 * Theme mapper interface.
 * Created by howen on 15/10/26.
 */
public interface ThemeMapper {

    List<Theme> getThemes(Theme theme);

    Theme getThemeBy(Theme theme);

    /**
     * 获取滚动导航
     * @return
     */
    List<Slider> getSlider(Slider slider);

    Item getItemBy(Item item);

    List<Inventory> getInvBy(Inventory inventory);

    List<SkuVo> getAllSkus(SkuVo skuVo);

    List<VersionVo> getVersioning(VersionVo versionVo);

    /**
     * 根据分类查询商品
     * @param navItemCateQuery
     * @return
     */
    List<SkuVo> getSkusByNavItemCate(NavItemCateQuery navItemCateQuery);

    /**
     * 获取分类
     * @param navItemCate
     * @return
     */
    List<NavItemCate> getNavItemCate(NavItemCate navItemCate);

    /**
     * 获取推荐商品
     * @param skuVo
     * @return
     */
    List<SkuVo> getRecommendSku(SkuVo skuVo);
}
