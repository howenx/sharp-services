package service;

import domain.Slider;
import domain.ThemeDto;
import domain.ThemeListDto;

import java.util.List;
import java.util.Map;

/**
 * For homepage theme list display function.
 * Created by howen on 15/10/26.
 */
public interface ThemeService {

    List<ThemeDto> getThemes(int pageSize,int offset);

    List<ThemeListDto> getThemeList(Long itemId);

    List<Slider> getSlider();

    Map<String,Object> getItemDetail(Long id,Long skuId);
}
