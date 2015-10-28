package service;

import domain.Slider;
import domain.ThemeDto;
import domain.ThemeListDto;
import mapper.ThemeMapper;
import play.Logger;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * implements Service and transaction processing.
 * Created by howen on 15/10/26.
 */
public class ThemeServiceImpl implements ThemeService{

    @Inject
    private ThemeMapper themeMapper;

    @Override
    public List<ThemeDto> getThemes(int pageSize, int offset) {

        Map<String,Integer> params = new HashMap<String,Integer>();
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return themeMapper.getThemeIosDto(params);
    }

    @Override
    public List<ThemeListDto> getThemeList(Long themeId) {

        return themeMapper.getThemeListDtoByThemeId(themeId);
    }

    @Override
    public List<Slider> getSlider() {
        return themeMapper.getSlider();
    }
}
