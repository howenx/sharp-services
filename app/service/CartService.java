package service;

import domain.Cart;
import domain.Collect;

import java.util.List;

/**
 * Cart service
 * Created by howen on 15/11/22.
 */
public interface CartService {

    List<Cart> getCartByUserSku(Cart cart) throws Exception;
    List<Collect> selectCollect(Collect collect)throws Exception;
}
