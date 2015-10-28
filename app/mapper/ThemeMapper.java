package mapper;

import domain.*;

import java.util.List;
import java.util.Map;

/**
 * Theme mapper interface.
 * Created by howen on 15/10/26.
 */
public interface ThemeMapper {

    List<ThemeDto> getThemeIosDto(Map<String,Integer> params);

    List<ThemeListDto> getThemeListDtoByThemeId(Long themeId);

    List<Slider> getSlider();
}
