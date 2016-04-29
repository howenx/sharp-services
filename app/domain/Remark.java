package domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户评价
 * Created by howen on 16/4/25.
 */
public class Remark implements Serializable {

    private static final long serialVersionUID = 21L;
    @JsonIgnore
    private Long id;//主键
    @JsonIgnore
    private Long userId;//用户ID
    @JsonIgnore
    private Long orderId;//订单ID
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createAt;//评价时间
    private String content;//评价内容
    private String picture;//晒图
    private Integer grade;//评分1,2,3,4,5
    @JsonIgnore
    private String skuType;//商品类型
    @JsonIgnore
    private Long skuTypeId;//商品类型ID

    @JsonIgnore
    private Integer pageSize;
    @JsonIgnore
    private Integer offset;
    @JsonIgnore
    private Integer countNum;

    private String userImg;//用户头像
    private String userName;//用户名

    private String buyAt;//购买时间
    private String size;//规格

    public Remark() {
    }

    public Remark(Long id, Long userId, Long orderId, Timestamp createAt, String content, String picture, Integer grade, String skuType, Long skuTypeId, Integer pageSize, Integer offset, Integer countNum, String userImg, String userName, String buyAt, String size) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.createAt = createAt;
        this.content = content;
        this.picture = picture;
        this.grade = grade;
        this.skuType = skuType;
        this.skuTypeId = skuTypeId;
        this.pageSize = pageSize;
        this.offset = offset;
        this.countNum = countNum;
        this.userImg = userImg;
        this.userName = userName;
        this.buyAt = buyAt;
        this.size = size;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    public Long getSkuTypeId() {
        return skuTypeId;
    }

    public void setSkuTypeId(Long skuTypeId) {
        this.skuTypeId = skuTypeId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBuyAt() {
        return buyAt;
    }

    public void setBuyAt(String buyAt) {
        this.buyAt = buyAt;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Remark{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", createAt=" + createAt +
                ", content='" + content + '\'' +
                ", picture='" + picture + '\'' +
                ", grade=" + grade +
                ", skuType='" + skuType + '\'' +
                ", skuTypeId=" + skuTypeId +
                ", pageSize=" + pageSize +
                ", offset=" + offset +
                ", countNum=" + countNum +
                ", userImg='" + userImg + '\'' +
                ", userName='" + userName + '\'' +
                ", buyAt='" + buyAt + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
