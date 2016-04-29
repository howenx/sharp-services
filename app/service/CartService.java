package service;

import domain.*;

import java.util.List;

/**
 * Cart service
 * Created by howen on 15/11/22.
 */
public interface CartService {

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

    /**
     * 查询评价
     *
     * @param remark remark
     * @return List<Remark>
     */
    List<Remark> selectRemark(Remark remark);

    /**
     * 分页查询评价
     *
     * @param remark remark
     * @return List<Remark>
     */
    List<Remark> selectRemarkPaging(Remark remark);

    /**
     * 查询订单
     * @param order order
     * @return List<Order>
     */
    List<Order> selectOrder(Order order);

    /**
     * 查询订单明细
     * @param orderLine orderLine
     * @return List<OrderLine>
     */
    List<OrderLine> selectOrderLine(OrderLine orderLine);

}
