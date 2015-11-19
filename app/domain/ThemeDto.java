package domain;

import java.io.Serializable;

/**
 * ios client homepage model
 * Created by howen on 15/10/27.
 */
public class ThemeDto implements Serializable {
    private Long        id;                                     //主键
    private Long        masterItemId;                           //主题主宣传商品ID
    private String      themeImg;   							//主题图片
    private String      themeUrl;   							//主题列表url
    private Integer     sortNu;									//排序编号

    public ThemeDto(Long id, Long masterItemId, String themeImg, String themeUrl, Integer sortNu) {
        this.id = id;
        this.masterItemId = masterItemId;
        this.themeImg = themeImg;
        this.themeUrl = themeUrl;
        this.sortNu = sortNu;
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

    public Integer getSortNu() {
        return sortNu;
    }

    public void setSortNu(Integer sortNu) {
        this.sortNu = sortNu;
    }
}
