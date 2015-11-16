package domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品主题列表页
 * Created by howen on 15/10/27.
 */
public class ThemeListDto implements Serializable {

    private Long        themeId;   	    //主题ID
    private Long        itemId;   	    //商品ID
    private String      itemImg;   	    //商品图片
    private String      itemUrl;   		//商品详细页面链接
    private String      itemTitle;   	//商品标题
    private BigDecimal  itemPrice;   	//商品价格
    private BigDecimal  itemCostPrice;  //商品原价
    private BigDecimal  itemDiscount;   //商品折扣
    private Integer     itemSoldAmount; //商品销量
    private Boolean     orMasterItem;   //是否是主题主打宣传商品
    private String      masterItemTag;	//如果是主打宣传商品，会需要tag json串
    private Integer     collectCount;   //商品收藏数
    private String      masterItemImg;  //主题属宣传商品图片url
    private String      onShelvesAt;    //商品销售起始时间
    private String      offShelvesAt;   //商品销售终止时间


    public ThemeListDto() {
    }

    public ThemeListDto(Long themeId, Long itemId, String itemImg, String itemUrl, String itemTitle, BigDecimal itemPrice, BigDecimal itemCostPrice, BigDecimal itemDiscount, Integer itemSoldAmount, Boolean orMasterItem, String masterItemTag, Integer collectCount, String masterItemImg, String onShelvesAt, String offShelvesAt) {
        this.themeId = themeId;
        this.itemId = itemId;
        this.itemImg = itemImg;
        this.itemUrl = itemUrl;
        this.itemTitle = itemTitle;
        this.itemPrice = itemPrice;
        this.itemCostPrice = itemCostPrice;
        this.itemDiscount = itemDiscount;
        this.itemSoldAmount = itemSoldAmount;
        this.orMasterItem = orMasterItem;
        this.masterItemTag = masterItemTag;
        this.collectCount = collectCount;
        this.masterItemImg = masterItemImg;
        this.onShelvesAt = onShelvesAt;
        this.offShelvesAt = offShelvesAt;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
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

    public BigDecimal getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(BigDecimal itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public Integer getItemSoldAmount() {
        return itemSoldAmount;
    }

    public void setItemSoldAmount(Integer itemSoldAmount) {
        this.itemSoldAmount = itemSoldAmount;
    }

    public Boolean getOrMasterItem() {
        return orMasterItem;
    }

    public void setOrMasterItem(Boolean orMasterItem) {
        this.orMasterItem = orMasterItem;
    }

    public String getMasterItemTag() {
        return masterItemTag;
    }

    public void setMasterItemTag(String masterItemTag) {
        this.masterItemTag = masterItemTag;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public String getMasterItemImg() {
        return masterItemImg;
    }

    public void setMasterItemImg(String masterItemImg) {
        this.masterItemImg = masterItemImg;
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

    @Override
    public String toString() {
        return "ThemeListDto{" +
                "themeId=" + themeId +
                ", itemId=" + itemId +
                ", itemImg='" + itemImg + '\'' +
                ", itemUrl='" + itemUrl + '\'' +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemCostPrice=" + itemCostPrice +
                ", itemDiscount=" + itemDiscount +
                ", itemSoldAmount=" + itemSoldAmount +
                ", orMasterItem=" + orMasterItem +
                ", masterItemTag='" + masterItemTag + '\'' +
                ", collectCount=" + collectCount +
                ", masterItemImg='" + masterItemImg + '\'' +
                ", onShelvesAt='" + onShelvesAt + '\'' +
                ", offShelvesAt='" + offShelvesAt + '\'' +
                '}';
    }
}
