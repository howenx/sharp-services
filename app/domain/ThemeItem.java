package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Table ThemeItem model.
 * Created by howen on 15/10/26.
 */
public class ThemeItem implements Serializable {

    private Long        themeId;   	    //主题ID
    private Long        itemId;   	    //商品ID
    private String      itemImg;   	    //商品图片
    private String      itemUrl;   		//商品详细页面链接
    private String      itemTitle;   	//商品标题
    private Double      itemPrice;   	//商品价格
    private Double      itemCostPrice;  //商品原价
    private Double      itemDiscount;   //商品折扣
    private Integer     itemSoldAmount; //商品销量
    private Boolean     orMasterItem;   //是否是主题主打宣传商品
    private String      masterItemImg;  //如果是主宣传商品图片url
    private String      masterItemTag;	//如果是主打宣传商品，会需要tag json串
    private Integer     collectCount;   //商品收藏数
    private String      state;          //商品状态
    @JsonIgnore
    private String      onShelvesAt;    //商品销售起始时间
    @JsonIgnore
    private String      offShelvesAt;   //商品销售终止时间
    @JsonIgnore
    private Long        id;				//主键
    @JsonIgnore
    private Integer     likeCount;   	//商品点赞数
    @JsonIgnore
    private Integer     sortNu;   		//商品排序
    @JsonIgnore
    private Boolean     orDestory;      //是否删除,
    @JsonIgnore
    private Long        destoryUid;   	//删除操作用户id,
    @JsonIgnore
    private Timestamp   updateAt;    	//更新时间,
    @JsonIgnore
    private Long        updateUid;    	//更新操作用户id,
    @JsonIgnore
    private Timestamp   createAt;     	//创建时间
    @JsonIgnore
    private Long        createUid;    	//创建操作用户id

    public ThemeItem() {
    }

    public ThemeItem(Long id, Long themeId, Long itemId, String itemImg, String itemUrl, String itemTitle, Double itemPrice, Double itemCostPrice, Double itemDiscount, Integer itemSoldAmount, String onShelvesAt, String offShelvesAt, Boolean orMasterItem, String masterItemImg, String masterItemTag, Integer collectCount, String state, Integer likeCount, Integer sortNu, Boolean orDestory, Long destoryUid, Timestamp updateAt, Long updateUid, Timestamp createAt, Long createUid) {
        this.id = id;
        this.themeId = themeId;
        this.itemId = itemId;
        this.itemImg = itemImg;
        this.itemUrl = itemUrl;
        this.itemTitle = itemTitle;
        this.itemPrice = itemPrice;
        this.itemCostPrice = itemCostPrice;
        this.itemDiscount = itemDiscount;
        this.itemSoldAmount = itemSoldAmount;
        this.onShelvesAt = onShelvesAt;
        this.offShelvesAt = offShelvesAt;
        this.orMasterItem = orMasterItem;
        this.masterItemImg = masterItemImg;
        this.masterItemTag = masterItemTag;
        this.collectCount = collectCount;
        this.state = state;
        this.likeCount = likeCount;
        this.sortNu = sortNu;
        this.orDestory = orDestory;
        this.destoryUid = destoryUid;
        this.updateAt = updateAt;
        this.updateUid = updateUid;
        this.createAt = createAt;
        this.createUid = createUid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getItemCostPrice() {
        return itemCostPrice;
    }

    public void setItemCostPrice(Double itemCostPrice) {
        this.itemCostPrice = itemCostPrice;
    }

    public Double getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(Double itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public Integer getItemSoldAmount() {
        return itemSoldAmount;
    }

    public void setItemSoldAmount(Integer itemSoldAmount) {
        this.itemSoldAmount = itemSoldAmount;
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

    public Boolean getOrMasterItem() {
        return orMasterItem;
    }

    public void setOrMasterItem(Boolean orMasterItem) {
        this.orMasterItem = orMasterItem;
    }

    public String getMasterItemImg() {
        return masterItemImg;
    }

    public void setMasterItemImg(String masterItemImg) {
        this.masterItemImg = masterItemImg;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getSortNu() {
        return sortNu;
    }

    public void setSortNu(Integer sortNu) {
        this.sortNu = sortNu;
    }

    public Boolean getOrDestory() {
        return orDestory;
    }

    public void setOrDestory(Boolean orDestory) {
        this.orDestory = orDestory;
    }

    public Long getDestoryUid() {
        return destoryUid;
    }

    public void setDestoryUid(Long destoryUid) {
        this.destoryUid = destoryUid;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }

    public Long getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(Long updateUid) {
        this.updateUid = updateUid;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public Long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Long createUid) {
        this.createUid = createUid;
    }

    @Override
    public String toString() {
        return "ThemeItem{" +
                "id=" + id +
                ", themeId=" + themeId +
                ", itemId=" + itemId +
                ", itemImg='" + itemImg + '\'' +
                ", itemUrl='" + itemUrl + '\'' +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemCostPrice=" + itemCostPrice +
                ", itemDiscount=" + itemDiscount +
                ", itemSoldAmount=" + itemSoldAmount +
                ", onShelvesAt='" + onShelvesAt + '\'' +
                ", offShelvesAt='" + offShelvesAt + '\'' +
                ", orMasterItem=" + orMasterItem +
                ", masterItemImg='" + masterItemImg + '\'' +
                ", masterItemTag='" + masterItemTag + '\'' +
                ", collectCount=" + collectCount +
                ", state='" + state + '\'' +
                ", likeCount=" + likeCount +
                ", sortNu=" + sortNu +
                ", orDestory=" + orDestory +
                ", destoryUid=" + destoryUid +
                ", updateAt=" + updateAt +
                ", updateUid=" + updateUid +
                ", createAt=" + createAt +
                ", createUid=" + createUid +
                '}';
    }
}
