package mapper;

import domain.*;

import java.util.List;

/**
 * Theme mapper interface.
 * Created by howen on 15/10/26.
 */
public interface ShoppingCartMapper {
    List<Cart> getCartByUserSku(Cart cart) throws Exception;

    List<Collect> selectCollect(Collect collect) throws Exception;

    Integer getNotReadMsgNum(MsgRec msgRec);

    /***
     * 获取未接收的未过期的系统消息
     *
     * @param userId
     * @return
     */
    List<Msg> getNotRecMsg(Long userId);

    List<Remark> selectRemark(Remark remark);

    List<Remark> selectRemarkPaging(Remark remark);

    List<Order> selectOrder(Order order);

    List<OrderLine> selectOrderLine(OrderLine orderLine);
}
