package mapper;

import domain.*;

import java.util.List;
import java.util.Map;

/**
 * Theme mapper interface.
 * Created by howen on 15/10/26.
 */
public interface ThemeMapper {

    List<Theme> getThemes(Map<String,Integer> params);

    Theme getThemeBy(Theme theme);

    List<Slider> getSlider();

    Item getItemBy(Item item);

    List<Inventory> getInvBy(Item item);

    List<Address> getAddressList(Address address);

    Integer updateAddress(Address address);

    Integer insertAddress(Address address);

    Integer deleteAddress(Address address);
}
