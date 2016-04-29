package service;

import domain.*;
import mapper.ShoppingCartMapper;

import javax.inject.Inject;
import java.util.List;

/**
 * 购物车service实现
 * Created by howen on 15/11/22.
 */
public class CartServiceImpl implements CartService {

    @Inject
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public List<Cart> getCartByUserSku(Cart cart) throws Exception {
        return shoppingCartMapper.getCartByUserSku(cart);
    }

    @Override
    public List<Collect> selectCollect(Collect collect) throws Exception {
        return shoppingCartMapper.selectCollect(collect);
    }

    @Override
    public Integer getNotReadMsgNum(MsgRec msgRec) {
        return shoppingCartMapper.getNotReadMsgNum(msgRec);
    }

    @Override
    public List<Msg> getNotRecMsg(Long userId) {
        return shoppingCartMapper.getNotRecMsg(userId);
    }

    @Override
    public List<Remark> selectRemark(Remark remark) {
        return shoppingCartMapper.selectRemark(remark);
    }

    @Override
    public List<Remark> selectRemarkPaging(Remark remark) {
        return shoppingCartMapper.selectRemarkPaging(remark);
    }

    @Override
    public List<Order> selectOrder(Order order) {
        return shoppingCartMapper.selectOrder(order);
    }

    @Override
    public List<OrderLine> selectOrderLine(OrderLine orderLine) {
        return shoppingCartMapper.selectOrderLine(orderLine);
    }

}
