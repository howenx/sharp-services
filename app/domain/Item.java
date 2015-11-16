package domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商品vo
 * Created by howen on 15/11/12.
 */
public class Item implements Serializable {

    private Long id;//商品ID
    private String itemTitle;//商品标题
    private String onShelvesAt;//商品销售起始时间
    private String offShelvesAt;//商品销售终止时间
    private String itemDetailImgs;//商品详细图
    private String itemFeatures;//商品属性
    private Long themeId;//主题ID
    private String state;//商品状态
    private Boolean orFreeShip;//是否包邮
    private String deliveryArea;//发货区域
    private String deliveryTime;//配送时间
    private Boolean orRestrictBuy;//是否限购
    private Integer restrictAmount;//限购数量
    private Boolean orShoppingPoll;//是否拼购
    private String shareImg;//分享的图片
    private String shareUrl;//分享的地址
    private Integer collectCount;//收藏数
    private String itemNotice;//商品重要布告
    private String publicity;//宣传标识

    public Item() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getOnShelvesAt() {
        return onShelvesAt;
    }

    public void setOnShelvesAt(String onShelvesAt) {
        this.onShelvesAt = onShelvesAt;
    }

    public String getOffShelvesAt() {
        return offShelvesAt;
    }

    public void setOffShelvesAt(String offShelvesAt) {
        this.offShelvesAt = offShelvesAt;
    }

    public String getItemDetailImgs() {
        return itemDetailImgs;
    }

    public void setItemDetailImgs(String itemDetailImgs) {
        this.itemDetailImgs = itemDetailImgs;
    }

    public String getItemFeatures() {
        return itemFeatures;
    }

    public void setItemFeatures(String itemFeatures) {
        this.itemFeatures = itemFeatures;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getOrFreeShip() {
        return orFreeShip;
    }

    public void setOrFreeShip(Boolean orFreeShip) {
        this.orFreeShip = orFreeShip;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Boolean getOrRestrictBuy() {
        return orRestrictBuy;
    }

    public void setOrRestrictBuy(Boolean orRestrictBuy) {
        this.orRestrictBuy = orRestrictBuy;
    }

    public Integer getRestrictAmount() {
        return restrictAmount;
    }

    public void setRestrictAmount(Integer restrictAmount) {
        this.restrictAmount = restrictAmount;
    }

    public Boolean getOrShoppingPoll() {
        return orShoppingPoll;
    }

    public void setOrShoppingPoll(Boolean orShoppingPoll) {
        this.orShoppingPoll = orShoppingPoll;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public String getItemNotice() {
        return itemNotice;
    }

    public void setItemNotice(String itemNotice) {
        this.itemNotice = itemNotice;
    }

    public String getPublicity() {
        return publicity;
    }

    public void setPublicity(String publicity) {
        this.publicity = publicity;
    }

    public Item(Long id, String itemTitle, String onShelvesAt, String offShelvesAt, String itemDetailImgs, String itemFeatures, Long themeId, String state, Boolean orFreeShip, String deliveryArea, String deliveryTime, Boolean orRestrictBuy, Integer restrictAmount, Boolean orShoppingPoll, String shareImg, String shareUrl, Integer collectCount, String itemNotice, String publicity) {
        this.id = id;
        this.itemTitle = itemTitle;
        this.onShelvesAt = onShelvesAt;
        this.offShelvesAt = offShelvesAt;
        this.itemDetailImgs = itemDetailImgs;
        this.itemFeatures = itemFeatures;
        this.themeId = themeId;
        this.state = state;
        this.orFreeShip = orFreeShip;
        this.deliveryArea = deliveryArea;
        this.deliveryTime = deliveryTime;
        this.orRestrictBuy = orRestrictBuy;
        this.restrictAmount = restrictAmount;
        this.orShoppingPoll = orShoppingPoll;
        this.shareImg = shareImg;
        this.shareUrl = shareUrl;
        this.collectCount = collectCount;
        this.itemNotice = itemNotice;
        this.publicity = publicity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemTitle='" + itemTitle + '\'' +
                ", onShelvesAt='" + onShelvesAt + '\'' +
                ", offShelvesAt='" + offShelvesAt + '\'' +
                ", itemDetailImgs='" + itemDetailImgs + '\'' +
                ", itemFeatures='" + itemFeatures + '\'' +
                ", themeId=" + themeId +
                ", state='" + state + '\'' +
                ", orFreeShip=" + orFreeShip +
                ", deliveryArea='" + deliveryArea + '\'' +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", orRestrictBuy=" + orRestrictBuy +
                ", restrictAmount=" + restrictAmount +
                ", orShoppingPoll=" + orShoppingPoll +
                ", shareImg='" + shareImg + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", collectCount=" + collectCount +
                ", itemNotice='" + itemNotice + '\'' +
                ", publicity='" + publicity + '\'' +
                '}';
    }
}