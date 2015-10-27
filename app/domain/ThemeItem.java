package domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Table ThemeItem model.
 * Created by howen on 15/10/26.
 */
public class ThemeItem implements Serializable {

    private Long        id;				//主键
    private Long        themeId;   	    //主题ID
    private Long        itemId;   	    //商品ID
    private String      itemImg;   	    //商品图片
    private String      itemUrl;   		//商品详细页面链接
    private String      itemTitle;   	//商品标题
    private Double      itemPrice;   	//商品价格
    private Double      itemCostPrice;  //商品原价
    private Double      itemDiscount;   //商品折扣
    private Integer     itemSoldAmount; //商品销量
    private Boolean     isMasterItem;   //是否是主题主打宣传商品
    private String      masterItemTag;	//如果是主打宣传商品，会需要tag json串
    private Integer     likeCount;   	//商品点赞数
    private Integer     collectCount;   //商品收藏数
    private Integer     sortNu;   		//商品排序
    private Boolean     isDestory;      //是否删除,
    private Long        destoryUid;   	//删除操作用户id,
    private Timestamp   updateAt;    	//更新时间,
    private Long        updateUid;    	//更新操作用户id,
    private Timestamp   createAt;     	//创建时间
    private Long        createUid;    	//创建操作用户id

    public ThemeItem(Long id, Long themeId, Long itemId, String itemImg, String itemUrl, String itemTitle, Double itemPrice, Double itemCostPrice, Double itemDiscount, Integer itemSoldAmount, Boolean isMasterItem, String masterItemTag, Integer likeCount, Integer collectCount, Integer sortNu, Boolean isDestory, Long destoryUid, Timestamp updateAt, Long updateUid, Timestamp createAt, Long createUid) {
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
        this.isMasterItem = isMasterItem;
        this.masterItemTag = masterItemTag;
        this.likeCount = likeCount;
        this.collectCount = collectCount;
        this.sortNu = sortNu;
        this.isDestory = isDestory;
        this.destoryUid = destoryUid;
        this.updateAt = updateAt;
        this.updateUid = updateUid;
        this.createAt = createAt;
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
                ", isMasterItem=" + isMasterItem +
                ", masterItemTag='" + masterItemTag + '\'' +
                ", likeCount=" + likeCount +
                ", collectCount=" + collectCount +
                ", sortNu=" + sortNu +
                ", isDestory=" + isDestory +
                ", destoryUid=" + destoryUid +
                ", updateAt=" + updateAt +
                ", updateUid=" + updateUid +
                ", createAt=" + createAt +
                ", createUid=" + createUid +
                '}';
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

    public Boolean getIsMasterItem() {
        return isMasterItem;
    }

    public void setIsMasterItem(Boolean isMasterItem) {
        this.isMasterItem = isMasterItem;
    }

    public String getMasterItemTag() {
        return masterItemTag;
    }

    public void setMasterItemTag(String masterItemTag) {
        this.masterItemTag = masterItemTag;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getSortNu() {
        return sortNu;
    }

    public void setSortNu(Integer sortNu) {
        this.sortNu = sortNu;
    }

    public Boolean getIsDestory() {
        return isDestory;
    }

    public void setIsDestory(Boolean isDestory) {
        this.isDestory = isDestory;
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
}
