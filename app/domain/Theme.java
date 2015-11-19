package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Theme table module
 * Created by howen on 15/10/26.
 */
public class Theme implements Serializable {

    private Long        id;                                     //主键
    private Long        masterItemId;                           //主题主宣传商品ID
    private String      themeImg;   							//主题图片
    private String      themeUrl;   							//主题列表url

    @JsonIgnore
    private String      title;                                  //标题
    @JsonIgnore
    private String      themeDesc;   							//主题描述
    @JsonIgnore
    private Timestamp   startAt;    							//主题开始时间
    @JsonIgnore
    private Timestamp   endAt;   								//主题结束时间
    @JsonIgnore
    private BigDecimal  themeDiscountUp;						//几折扣起
    @JsonIgnore
    private BigDecimal  itemPriceTop;   						//主题包含商品最高价格
    @JsonIgnore
    private BigDecimal  itemPriceLow;   						//主题包含商品最低价格
    @JsonIgnore
    private String      themeTags;								//主题主图标签json串 name,angle,top,left
    @JsonIgnore
    private Integer     itemCount;   							//主题包含商品数量
    @JsonIgnore
    private Integer     themeTagCount;   						//主题图片标签数
    @JsonIgnore
    private Integer     sortNu;									//排序编号
    @JsonIgnore
    private Boolean     orDestory;    							//是否删除,
    @JsonIgnore
    private Long        destoryUid;   							//删除操作用户id,
    @JsonIgnore
    private Timestamp   destoryAt;    							//更新时间,
    @JsonIgnore
    private Timestamp   updateAt;    							//更新时间,
    @JsonIgnore
    private Long        updateUid;    							//更新操作用户id,
    @JsonIgnore
    private Timestamp   createAt;     							//创建时间
    @JsonIgnore
    private Long        createUid;    							//创建操作用户id
    @JsonIgnore
    private String      themeSrcImg;                            //主题底图

    public Theme() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMasterItemId() {
        return masterItemId;
    }

    public void setMasterItemId(Long masterItemId) {
        this.masterItemId = masterItemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThemeDesc() {
        return themeDesc;
    }

    public void setThemeDesc(String themeDesc) {
        this.themeDesc = themeDesc;
    }

    public Timestamp getStartAt() {
        return startAt;
    }

    public void setStartAt(Timestamp startAt) {
        this.startAt = startAt;
    }

    public Timestamp getEndAt() {
        return endAt;
    }

    public void setEndAt(Timestamp endAt) {
        this.endAt = endAt;
    }

    public BigDecimal getThemeDiscountUp() {
        return themeDiscountUp;
    }

    public void setThemeDiscountUp(BigDecimal themeDiscountUp) {
        this.themeDiscountUp = themeDiscountUp;
    }

    public BigDecimal getItemPriceTop() {
        return itemPriceTop;
    }

    public void setItemPriceTop(BigDecimal itemPriceTop) {
        this.itemPriceTop = itemPriceTop;
    }

    public BigDecimal getItemPriceLow() {
        return itemPriceLow;
    }

    public void setItemPriceLow(BigDecimal itemPriceLow) {
        this.itemPriceLow = itemPriceLow;
    }

    public String getThemeImg() {
        return themeImg;
    }

    public void setThemeImg(String themeImg) {
        this.themeImg = themeImg;
    }

    public String getThemeUrl() {
        return themeUrl;
    }

    public void setThemeUrl(String themeUrl) {
        this.themeUrl = themeUrl;
    }

    public String getThemeTags() {
        return themeTags;
    }

    public void setThemeTags(String themeTags) {
        this.themeTags = themeTags;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getThemeTagCount() {
        return themeTagCount;
    }

    public void setThemeTagCount(Integer themeTagCount) {
        this.themeTagCount = themeTagCount;
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

    public Timestamp getDestoryAt() {
        return destoryAt;
    }

    public void setDestoryAt(Timestamp destoryAt) {
        this.destoryAt = destoryAt;
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

    public String getThemeSrcImg() {
        return themeSrcImg;
    }

    public void setThemeSrcImg(String themeSrcImg) {
        this.themeSrcImg = themeSrcImg;
    }

    public Theme(Long id, Long masterItemId, String title, String themeDesc, Timestamp startAt, Timestamp endAt, BigDecimal themeDiscountUp, BigDecimal itemPriceTop, BigDecimal itemPriceLow, String themeImg, String themeUrl, String themeTags, Integer itemCount, Integer themeTagCount, Integer sortNu, Boolean orDestory, Long destoryUid, Timestamp destoryAt, Timestamp updateAt, Long updateUid, Timestamp createAt, Long createUid, String themeSrcImg) {
        this.id = id;
        this.masterItemId = masterItemId;
        this.title = title;
        this.themeDesc = themeDesc;
        this.startAt = startAt;
        this.endAt = endAt;
        this.themeDiscountUp = themeDiscountUp;
        this.itemPriceTop = itemPriceTop;
        this.itemPriceLow = itemPriceLow;
        this.themeImg = themeImg;
        this.themeUrl = themeUrl;
        this.themeTags = themeTags;
        this.itemCount = itemCount;
        this.themeTagCount = themeTagCount;
        this.sortNu = sortNu;
        this.orDestory = orDestory;
        this.destoryUid = destoryUid;
        this.destoryAt = destoryAt;
        this.updateAt = updateAt;
        this.updateUid = updateUid;
        this.createAt = createAt;
        this.createUid = createUid;
        this.themeSrcImg = themeSrcImg;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", masterItemId=" + masterItemId +
                ", title='" + title + '\'' +
                ", themeDesc='" + themeDesc + '\'' +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", themeDiscountUp=" + themeDiscountUp +
                ", itemPriceTop=" + itemPriceTop +
                ", itemPriceLow=" + itemPriceLow +
                ", themeImg='" + themeImg + '\'' +
                ", themeUrl='" + themeUrl + '\'' +
                ", themeTags='" + themeTags + '\'' +
                ", itemCount=" + itemCount +
                ", themeTagCount=" + themeTagCount +
                ", sortNu=" + sortNu +
                ", orDestory=" + orDestory +
                ", destoryUid=" + destoryUid +
                ", destoryAt=" + destoryAt +
                ", updateAt=" + updateAt +
                ", updateUid=" + updateUid +
                ", createAt=" + createAt +
                ", createUid=" + createUid +
                ", themeSrcImg='" + themeSrcImg + '\'' +
                '}';
    }
}
