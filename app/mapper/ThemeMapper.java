package mapper;

import domain.*;

import java.util.List;
import java.util.Map;

/**
 * Theme mapper interface.
 * Created by howen on 15/10/26.
 */
public interface ThemeMapper {

    List<Theme> getThemeIosDto(Map<String,Integer> params);

    List<ThemeItem> getThemeListDtoByThemeId(Long themeId);

    List<Slider> getSlider();

    Item getItemById(Item item);

    List<Inventory> getInvList(Item item);

    List<Address> getAddresssList(Address address);

    Integer updateAddress(Address address);

    Integer insertAddress(Address address);

    Integer deleteAddress(Address address);
}
