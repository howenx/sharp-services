package service;

import domain.Cart;
import domain.Collect;
import domain.Msg;
import domain.MsgRec;

import java.util.List;

/**
 * Cart service
 * Created by howen on 15/11/22.
 */
public interface CartService {

    List<Cart> getCartByUserSku(Cart cart) throws Exception;
    List<Collect> selectCollect(Collect collect)throws Exception;
    Integer getNotReadMsgNum(MsgRec msgRec);
    /***
     * 获取未接收的未过期的系统消息
     * @param userId
     * @return
     */
    List<Msg> getNotRecMsg(Long userId);
}
