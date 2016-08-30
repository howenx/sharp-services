package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.*;
import mapper.ThemeMapper;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Optional<List<Theme>> getThemes(Theme theme) {
        return Optional.ofNullable(themeMapper.getThemes(theme));
    }

    @Override
    public Optional<List<Slider>> getSlider(Slider slider) {
        return Optional.ofNullable(themeMapper.getSlider(slider));
    }

    @Override
    public Theme getThemeBy(Long themeId) {
        Theme theme = new Theme();
        theme.setId(themeId);
        return themeMapper.getThemeBy(theme);
    }

    @Override
    public Item getItemBy(Item item) {
        return themeMapper.getItemBy(item);
    }

    @Override
    public List<Inventory> getInvBy(Inventory inventory) {
        return themeMapper.getInvBy(inventory);
    }

    @Override
    public List<SkuVo> getAllSkus(SkuVo skuVo) {
        return themeMapper.getAllSkus(skuVo);
    }

    @Override
    public List<VersionVo> getVersioning(VersionVo versionVo) {
        return themeMapper.getVersioning(versionVo);
    }

    @Override
    public List<SkuVo> getSkusByNavItemCate(NavItemCateQuery navItemCateQuery) {
        return themeMapper.getSkusByNavItemCate(navItemCateQuery);
    }

    @Override
    public List<NavItemCate> getNavItemCate(NavItemCate navItemCate) {
        return themeMapper.getNavItemCate(navItemCate);
    }

    @Override
    public List<SkuVo> getRecommendSku(SkuVo skuVo) {
        return themeMapper.getRecommendSku(skuVo);
    }

    @Override
    public List<Cates> getCate(Cates cates) {
        return themeMapper.getCate(cates);
    }

    public void setThemeMapper(ThemeMapper themeMapper) {
        this.themeMapper = themeMapper;
    }


}
