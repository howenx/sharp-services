package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.*;
import mapper.SubjectPriceMapper;
import mapper.ThemeMapper;
import mapper.VaryPriceMapper;

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

    @Inject
    private VaryPriceMapper varyPriceMapper;

    @Inject
    private SubjectPriceMapper subjectPriceMapper;

    //将Json串转换成List
    final static ObjectMapper mapper = new ObjectMapper();

    @Override
    public Optional<List<Theme>> getThemes(int pageSize, int offset) {

        Map<String, Integer> params = new HashMap<>();
        params.put("pageSize", pageSize);
        params.put("offset", offset);
        return Optional.ofNullable(themeMapper.getThemes(params));
    }

    @Override
    public Optional<List<Slider>> getSlider() {
        return Optional.ofNullable(themeMapper.getSlider());
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
    public List<VaryPrice> getVaryPriceBy(VaryPrice varyPrice) {
        return varyPriceMapper.getVaryPriceBy(varyPrice);
    }

    @Override
    public SubjectPrice getSbjPriceById(Long id) {
        return subjectPriceMapper.getSbjPriceById(id);
    }

    public void setThemeMapper(ThemeMapper themeMapper) {
        this.themeMapper = themeMapper;
    }
}
