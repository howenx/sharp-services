package mapper;

import domain.Theme;
import domain.ThemeDto;
import domain.ThemeItem;
import domain.ThemeListDto;

import java.util.List;
import java.util.Map;

/**
 * Theme mapper interface.
 * Created by howen on 15/10/26.
 */
public interface ThemeMapper {


    List<ThemeItem> getThemeItemByThemeId(Long itemId);

    Theme getThemesById(Long id);

    List<Theme> getThemeAll(Map<String,Integer> params);

    List<ThemeItem> getThemeItemAll();

    List<ThemeDto> getThemeIosDto(Map<String,Integer> params);

    List<ThemeListDto> getThemeListDtoByThemeId(Long themeId);

}
