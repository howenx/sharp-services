package service;

import domain.*;

import java.util.List;
import java.util.Optional;

/**
 * For homepage theme list display function.
 * Created by howen on 15/10/26.
 */
public interface ThemeService {

    Optional<List<Theme>> getThemes(int pageSize, int offset);

    Optional<List<Slider>> getSlider();
    Optional<List<Slider>> getSliderNav();

    Theme getThemeBy(Long themeId);

    Item getItemBy(Item item);

    List<Inventory> getInvBy(Inventory inventory);

    List<SkuVo> getAllSkus(SkuVo skuVo);

    List<VersionVo> getVersioning(VersionVo versionVo);
}
