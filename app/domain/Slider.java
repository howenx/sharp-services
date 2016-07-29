package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * slider
 * Created by howen on 15/10/28.
 */
public class Slider implements Serializable{

    private static final long serialVersionUID = 21L;

    @JsonIgnore
    private Long id;
    @JsonIgnore
    private String img;
    @JsonIgnore
    private Integer sortNu;
    @JsonIgnore
    private Timestamp createAt;
    private String  itemTarget;
    private String  targetType;
    private String  url;
    @JsonIgnore
    private Boolean orNav; //是否是导航图
    @JsonIgnore
    private String navText;//导航文字显示  

    public Slider() {
    }

    public Slider(Long id, String img, Integer sortNu, Timestamp createAt, String itemTarget, String targetType, String url) {
        this.id = id;
        this.img = img;
        this.sortNu = sortNu;
        this.createAt = createAt;
        this.itemTarget = itemTarget;
        this.targetType = targetType;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getSortNu() {
        return sortNu;
    }

    public void setSortNu(Integer sortNu) {
        this.sortNu = sortNu;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public String getItemTarget() {
        return itemTarget;
    }

    public void setItemTarget(String itemTarget) {
        this.itemTarget = itemTarget;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getOrNav() {
        return orNav;
    }

    public void setOrNav(Boolean orNav) {
        this.orNav = orNav;
    }

    public String getNavText() {
        return navText;
    }

    public void setNavText(String navText) {
        this.navText = navText;
    }

    @Override
    public String toString() {
        return "Slider{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", sortNu=" + sortNu +
                ", createAt=" + createAt +
                ", itemTarget='" + itemTarget + '\'' +
                ", targetType='" + targetType + '\'' +
                ", url='" + url + '\'' +
                ", orNav=" + orNav +
                ", navText='" + navText + '\'' +
                '}';
    }
}
