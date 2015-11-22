package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Theme table module
 * Created by howen on 15/10/26.
 */
public class Theme implements Serializable {

    private     Long        id;//主题ID
    private     String      themeImg;//主题图片
    private     String      themeUrl;//跳转到主题列表页的链接


    @JsonIgnore
    private     String      title;
    @JsonIgnore
    private     Timestamp   startAt;
    @JsonIgnore
    private     Timestamp   endAt;
    @JsonIgnore
    private     Integer     sortNu;
    @JsonIgnore
    private     Boolean     orDestory;
    @JsonIgnore
    private     Timestamp   destoryAt;
    @JsonIgnore
    private     Timestamp   updateAt;
    @JsonIgnore
    private     Timestamp   createAt;
    @JsonIgnore
    private     String      themeSrcImg;
    @JsonIgnore
    private     JsonNode    themeConfigInfo;
    @JsonIgnore
    private     JsonNode    themeItem;
    @JsonIgnore
    private     JsonNode    masterItemTag;
    @JsonIgnore
    private     Long        masterItemId;

    public Theme() {
    }

    public Theme(Long id, String themeImg, String themeUrl, String title, Timestamp startAt, Timestamp endAt, Integer sortNu, Boolean orDestory, Timestamp destoryAt, Timestamp updateAt, Timestamp createAt, String themeSrcImg, JsonNode themeConfigInfo, JsonNode themeItem, JsonNode masterItemTag, Long masterItemId) {
        this.id = id;
        this.themeImg = themeImg;
        this.themeUrl = themeUrl;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.sortNu = sortNu;
        this.orDestory = orDestory;
        this.destoryAt = destoryAt;
        this.updateAt = updateAt;
        this.createAt = createAt;
        this.themeSrcImg = themeSrcImg;
        this.themeConfigInfo = themeConfigInfo;
        this.themeItem = themeItem;
        this.masterItemTag = masterItemTag;
        this.masterItemId = masterItemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public String getThemeSrcImg() {
        return themeSrcImg;
    }

    public void setThemeSrcImg(String themeSrcImg) {
        this.themeSrcImg = themeSrcImg;
    }

    public JsonNode getThemeConfigInfo() {
        return themeConfigInfo;
    }

    public void setThemeConfigInfo(JsonNode themeConfigInfo) {
        this.themeConfigInfo = themeConfigInfo;
    }

    public JsonNode getThemeItem() {
        return themeItem;
    }

    public void setThemeItem(JsonNode themeItem) {
        this.themeItem = themeItem;
    }

    public JsonNode getMasterItemTag() {
        return masterItemTag;
    }

    public void setMasterItemTag(JsonNode masterItemTag) {
        this.masterItemTag = masterItemTag;
    }

    public Long getMasterItemId() {
        return masterItemId;
    }

    public void setMasterItemId(Long masterItemId) {
        this.masterItemId = masterItemId;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", themeImg='" + themeImg + '\'' +
                ", themeUrl='" + themeUrl + '\'' +
                ", title='" + title + '\'' +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", sortNu=" + sortNu +
                ", orDestory=" + orDestory +
                ", destoryAt=" + destoryAt +
                ", updateAt=" + updateAt +
                ", createAt=" + createAt +
                ", themeSrcImg='" + themeSrcImg + '\'' +
                ", themeConfigInfo=" + themeConfigInfo +
                ", themeItem=" + themeItem +
                ", masterItemTag=" + masterItemTag +
                ", masterItemId=" + masterItemId +
                '}';
    }
}
