package mapper;

import domain.Cart;
import domain.Collect;

import java.util.List;

/**
 * Theme mapper interface.
 * Created by howen on 15/10/26.
 */
public interface ShoppingCartMapper {
    List<Cart> getCartByUserSku(Cart cart) throws Exception;
    List<Collect> selectCollect(Collect collect)throws Exception;

}
