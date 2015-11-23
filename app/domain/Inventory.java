package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 商品库存信息
 * Created by howen on 15/11/12.
 */
public class Inventory implements Serializable{

    private     Long        id;//库存ID
    private     String      itemColor;//颜色
    private     String      itemSize;//尺码
    private     BigDecimal  itemSrcPrice;//商品原价
    private     BigDecimal  itemPrice;//商品价格
    private     BigDecimal  itemDiscount;//商品折扣
    private     Boolean     orMasterInv;//是否主商品
    private     String      state;//状态
    private     BigDecimal  shipFee;//邮费
    private     String      invArea;//库存区域区分：'B'保税区仓库发货，‘Z’韩国直邮
    private     Integer     restrictAmount;//限购数量
    private     Integer     restAmount;//商品余量
    private     String      invImg;//sku主图
    private     String      itemPreviewImgs;//sku预览图
    private     String      invUrl;//用于方便前段获取库存跳转链接
    private     String      invTitle;//sku标题
    @JsonIgnore
    private     Long        itemId;
    @JsonIgnore
    private     Integer     amount;//库存总量
    @JsonIgnore
    private     BigDecimal  itemCostPrice; //商品成本价
    @JsonIgnore
    private     Integer     soldAmount;
    @JsonIgnore
    private     Boolean     orDestroy;
    @JsonIgnore
    private     Timestamp   destroyAt;
    @JsonIgnore
    private     Timestamp   updateAt;
    @JsonIgnore
    private     Timestamp   createAt;


    public Inventory() {
    }

    public Inventory(Long id, String itemColor, String itemSize, BigDecimal itemSrcPrice, BigDecimal itemPrice, BigDecimal itemDiscount, Boolean orMasterInv, String state, BigDecimal shipFee, String invArea, Integer restrictAmount, Integer restAmount, String invImg, String itemPreviewImgs, String invUrl, String invTitle, Long itemId, Integer amount, BigDecimal itemCostPrice, Integer soldAmount, Boolean orDestroy, Timestamp destroyAt, Timestamp updateAt, Timestamp createAt) {
        this.id = id;
        this.itemColor = itemColor;
        this.itemSize = itemSize;
        this.itemSrcPrice = itemSrcPrice;
        this.itemPrice = itemPrice;
        this.itemDiscount = itemDiscount;
        this.orMasterInv = orMasterInv;
        this.state = state;
        this.shipFee = shipFee;
        this.invArea = invArea;
        this.restrictAmount = restrictAmount;
        this.restAmount = restAmount;
        this.invImg = invImg;
        this.itemPreviewImgs = itemPreviewImgs;
        this.invUrl = invUrl;
        this.invTitle = invTitle;
        this.itemId = itemId;
        this.amount = amount;
        this.itemCostPrice = itemCostPrice;
        this.soldAmount = soldAmount;
        this.orDestroy = orDestroy;
        this.destroyAt = destroyAt;
        this.updateAt = updateAt;
        this.createAt = createAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public BigDecimal getItemSrcPrice() {
        return itemSrcPrice;
    }

    public void setItemSrcPrice(BigDecimal itemSrcPrice) {
        this.itemSrcPrice = itemSrcPrice;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(BigDecimal itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public Boolean getOrMasterInv() {
        return orMasterInv;
    }

    public void setOrMasterInv(Boolean orMasterInv) {
        this.orMasterInv = orMasterInv;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getShipFee() {
        return shipFee;
    }

    public void setShipFee(BigDecimal shipFee) {
        this.shipFee = shipFee;
    }

    public String getInvArea() {
        return invArea;
    }

    public void setInvArea(String invArea) {
        this.invArea = invArea;
    }

    public Integer getRestrictAmount() {
        return restrictAmount;
    }

    public void setRestrictAmount(Integer restrictAmount) {
        this.restrictAmount = restrictAmount;
    }

    public Integer getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(Integer restAmount) {
        this.restAmount = restAmount;
    }

    public String getInvImg() {
        return invImg;
    }

    public void setInvImg(String invImg) {
        this.invImg = invImg;
    }

    public String getItemPreviewImgs() {
        return itemPreviewImgs;
    }

    public void setItemPreviewImgs(String itemPreviewImgs) {
        this.itemPreviewImgs = itemPreviewImgs;
    }

    public String getInvUrl() {
        return invUrl;
    }

    public void setInvUrl(String invUrl) {
        this.invUrl = invUrl;
    }

    public String getInvTitle() {
        return invTitle;
    }

    public void setInvTitle(String invTitle) {
        this.invTitle = invTitle;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getItemCostPrice() {
        return itemCostPrice;
    }

    public void setItemCostPrice(BigDecimal itemCostPrice) {
        this.itemCostPrice = itemCostPrice;
    }

    public Integer getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(Integer soldAmount) {
        this.soldAmount = soldAmount;
    }

    public Boolean getOrDestroy() {
        return orDestroy;
    }

    public void setOrDestroy(Boolean orDestroy) {
        this.orDestroy = orDestroy;
    }

    public Timestamp getDestroyAt() {
        return destroyAt;
    }

    public void setDestroyAt(Timestamp destroyAt) {
        this.destroyAt = destroyAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", itemColor='" + itemColor + '\'' +
                ", itemSize='" + itemSize + '\'' +
                ", itemSrcPrice=" + itemSrcPrice +
                ", itemPrice=" + itemPrice +
                ", itemDiscount=" + itemDiscount +
                ", orMasterInv=" + orMasterInv +
                ", state='" + state + '\'' +
                ", shipFee=" + shipFee +
                ", invArea='" + invArea + '\'' +
                ", restrictAmount=" + restrictAmount +
                ", restAmount=" + restAmount +
                ", invImg='" + invImg + '\'' +
                ", itemPreviewImgs='" + itemPreviewImgs + '\'' +
                ", invUrl='" + invUrl + '\'' +
                ", invTitle='" + invTitle + '\'' +
                ", itemId=" + itemId +
                ", amount=" + amount +
                ", itemCostPrice=" + itemCostPrice +
                ", soldAmount=" + soldAmount +
                ", orDestroy=" + orDestroy +
                ", destroyAt=" + destroyAt +
                ", updateAt=" + updateAt +
                ", createAt=" + createAt +
                '}';
    }
}
