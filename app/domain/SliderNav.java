package domain;


import java.io.Serializable;

/**
 * slider
 * Created by howen on 15/10/28.
 */
public class SliderNav implements Serializable{

    private static final long serialVersionUID = 21L;


    private String  itemTarget;
    private String  targetType;
    private String  url;
    private String navText;//导航文字显示

    public SliderNav(String itemTarget, String targetType, String url, String navText) {
        this.itemTarget = itemTarget;
        this.targetType = targetType;
        this.url = url;
        this.navText = navText;
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

    public String getNavText() {
        return navText;
    }

    public void setNavText(String navText) {
        this.navText = navText;
    }

    @Override
    public String toString() {
        return "SliderNav{" +
                "itemTarget='" + itemTarget + '\'' +
                ", targetType='" + targetType + '\'' +
                ", url='" + url + '\'' +
                ", navText='" + navText + '\'' +
                '}';
    }
}
