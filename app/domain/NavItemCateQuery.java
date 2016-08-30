package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.List;

/**
 * nav_item_cate 查询条件
 * Created by sibyl.sun on 16/8/24.
 */
public class NavItemCateQuery {

    @JsonIgnore
    private Integer pageSize;
    @JsonIgnore
    private Integer offset;

    private HashSet<Long> itemIdList;
    private HashSet<Long> invIdList;

    private HashSet<Long> pinIdList;
    private HashSet<Long> cateIdList;
    //主题id
    private HashSet<Long> themeIdList;



    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public HashSet<Long> getItemIdList() {
        return itemIdList;
    }

    public void setItemIdList(HashSet<Long> itemIdList) {
        this.itemIdList = itemIdList;
    }

    public HashSet<Long> getInvIdList() {
        return invIdList;
    }

    public void setInvIdList(HashSet<Long> invIdList) {
        this.invIdList = invIdList;
    }

    public HashSet<Long> getPinIdList() {
        return pinIdList;
    }

    public void setPinIdList(HashSet<Long> pinIdList) {
        this.pinIdList = pinIdList;
    }

    public HashSet<Long> getCateIdList() {
        return cateIdList;
    }

    public void setCateIdList(HashSet<Long> cateIdList) {
        this.cateIdList = cateIdList;
    }

    public HashSet<Long> getThemeIdList() {
        return themeIdList;
    }

    public void setThemeIdList(HashSet<Long> themeIdList) {
        this.themeIdList = themeIdList;
    }

    @Override
    public String toString() {
        return "NavItemCateQuery{" +
                "pageSize=" + pageSize +
                ", offset=" + offset +
                ", itemIdList=" + itemIdList +
                ", invIdList=" + invIdList +
                ", pinIdList=" + pinIdList +
                ", cateIdList=" + cateIdList +
                '}';
    }
}
