package service;

import com.fasterxml.jackson.databind.JsonNode;
import domain.Address;
import domain.Slider;
import domain.Theme;
import domain.ThemeItem;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.List;
import java.util.Map;

/**
 * For homepage theme list display function.
 * Created by howen on 15/10/26.
 */
public interface ThemeService {

    List<Theme> getThemes(int pageSize, int offset);

    JsonNode getThemeList(Long itemId);

    List<Slider> getSlider();

    Map<String,Object> getItemDetail(Long id, Long skuId);

    List<Address> getAddress(Long userId);

    Boolean handleAddress(Address address,Integer handle);

}
