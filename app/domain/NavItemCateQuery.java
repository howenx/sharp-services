package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    private List<Long> itemIdList;
    private List<Long> invIdList;

    private List<Long> pinIdList;
    private List<Long> cateIdList;


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

    public List<Long> getItemIdList() {
        return itemIdList;
    }

    public void setItemIdList(List<Long> itemIdList) {
        this.itemIdList = itemIdList;
    }

    public List<Long> getInvIdList() {
        return invIdList;
    }

    public void setInvIdList(List<Long> invIdList) {
        this.invIdList = invIdList;
    }

    public List<Long> getPinIdList() {
        return pinIdList;
    }

    public void setPinIdList(List<Long> pinIdList) {
        this.pinIdList = pinIdList;
    }

    public List<Long> getCateIdList() {
        return cateIdList;
    }

    public void setCateIdList(List<Long> cateIdList) {
        this.cateIdList = cateIdList;
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
