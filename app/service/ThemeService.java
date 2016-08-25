package service;

import domain.*;

import java.util.List;
import java.util.Optional;

/**
 * For homepage theme list display function.
 * Created by howen on 15/10/26.
 */
public interface ThemeService {

    Optional<List<Theme>> getThemes(Theme theme);

    Optional<List<Slider>> getSlider(Slider slider);
    Optional<List<Slider>> getSliderNav();

    Theme getThemeBy(Long themeId);

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
}
