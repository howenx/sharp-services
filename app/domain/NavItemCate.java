package domain;

import java.io.Serializable;

/**
 * 入口分类
 * Created by sibyl.sun on 16/8/24.
 */
public class NavItemCate implements Serializable {
    private Long id  ;
    private Long navId;  //导航ID(slider表中的主键ID)
    private Integer cateType; //1.商品，2.sku，3.拼购商品，4.商品分类，5.主题
    private Long cateTypeId ; //item_id，inv_id，pin_id，cate_id，theme_id
    private Boolean orDestroy; //true：已经删除

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNavId() {
        return navId;
    }

    public void setNavId(Long navId) {
        this.navId = navId;
    }

    public Integer getCateType() {
        return cateType;
    }

    public void setCateType(Integer cateType) {
        this.cateType = cateType;
    }

    public Long getCateTypeId() {
        return cateTypeId;
    }

    public void setCateTypeId(Long cateTypeId) {
        this.cateTypeId = cateTypeId;
    }


    public Boolean getOrDestroy() {
        return orDestroy;
    }

    public void setOrDestroy(Boolean orDestroy) {
        this.orDestroy = orDestroy;
    }

    @Override
    public String toString() {
        return "NavItemCate{" +
                "id=" + id +
                ", navId=" + navId +
                ", cateType=" + cateType +
                ", cateTypeId=" + cateTypeId +
                ", orDestroy=" + orDestroy +
                '}';
    }
}
