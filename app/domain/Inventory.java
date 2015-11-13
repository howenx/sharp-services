package domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.Timestamp;

/**
 * 商品库存信息
 * Created by howen on 15/11/12.
 */
public class Inventory implements Serializable{

    private     Long id;//库存ID
    private     String itemColor;//颜色
    private     String itemSize;//尺寸
    private     BigDecimal itemPrice;//原价
    private     BigDecimal itemCostPrice;//售价
    private     BigDecimal itemDiscout;//折扣
    private     Integer restAmount;//剩余数量
    private     Boolean orSoldOut;//是否卖光
    private     String itemPreviewImgs;//预览图
    private     String invTitle;//商品标题,数据库不使用
    private     String invCollection;//商品收藏数,数据库不使用
    private     Boolean orMasterInv;//是否是主sku,现在将其用作标志进到详情页时候需要显示的哪一个sku
    private     String invUrl;//单个sku链接

    public Inventory() {
    }

    public Inventory(Long id, String itemColor, String itemSize, BigDecimal itemPrice, BigDecimal itemCostPrice, BigDecimal itemDiscout, Integer restAmount, Boolean orSoldOut, String itemPreviewImgs, String invTitle, String invCollection, Boolean orMasterInv, String invUrl) {
        this.id = id;
        this.itemColor = itemColor;
        this.itemSize = itemSize;
        this.itemPrice = itemPrice;
        this.itemCostPrice = itemCostPrice;
        this.itemDiscout = itemDiscout;
        this.restAmount = restAmount;
        this.orSoldOut = orSoldOut;
        this.itemPreviewImgs = itemPreviewImgs;
        this.invTitle = invTitle;
        this.invCollection = invCollection;
        this.orMasterInv = orMasterInv;
        this.invUrl = invUrl;
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

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getItemCostPrice() {
        return itemCostPrice;
    }

    public void setItemCostPrice(BigDecimal itemCostPrice) {
        this.itemCostPrice = itemCostPrice;
    }

    public BigDecimal getItemDiscout() {
        return itemDiscout;
    }

    public void setItemDiscout(BigDecimal itemDiscout) {
        this.itemDiscout = itemDiscout;
    }

    public Integer getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(Integer restAmount) {
        this.restAmount = restAmount;
    }

    public Boolean getOrSoldOut() {
        return orSoldOut;
    }

    public void setOrSoldOut(Boolean orSoldOut) {
        this.orSoldOut = orSoldOut;
    }

    public String getItemPreviewImgs() {
        return itemPreviewImgs;
    }

    public void setItemPreviewImgs(String itemPreviewImgs) {
        this.itemPreviewImgs = itemPreviewImgs;
    }

    public String getInvTitle() {
        return invTitle;
    }

    public void setInvTitle(String invTitle) {
        this.invTitle = invTitle;
    }

    public String getInvCollection() {
        return invCollection;
    }

    public void setInvCollection(String invCollection) {
        this.invCollection = invCollection;
    }

    public Boolean getOrMasterInv() {
        return orMasterInv;
    }

    public void setOrMasterInv(Boolean orMasterInv) {
        this.orMasterInv = orMasterInv;
    }

    public String getInvUrl() {
        return invUrl;
    }

    public void setInvUrl(String invUrl) {
        this.invUrl = invUrl;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", itemColor='" + itemColor + '\'' +
                ", itemSize='" + itemSize + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemCostPrice=" + itemCostPrice +
                ", itemDiscout=" + itemDiscout +
                ", restAmount=" + restAmount +
                ", orSoldOut=" + orSoldOut +
                ", itemPreviewImgs='" + itemPreviewImgs + '\'' +
                ", invTitle='" + invTitle + '\'' +
                ", invCollection='" + invCollection + '\'' +
                ", orMasterInv=" + orMasterInv +
                ", invUrl='" + invUrl + '\'' +
                '}';
    }
}
