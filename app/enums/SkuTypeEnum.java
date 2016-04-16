package enums;

/**
 * 商品类型枚举
 */
public enum SkuTypeEnum {
    ITEM("item","普通商品"),
    VARY("vary","vary商品"),
    CUSTOMIZE("customize","自定义商品"),
    PIN("pin","拼购商品"),
    DIRECT("direct","海外直邮商品");

    private String skuType;
    private String name;

    private SkuTypeEnum(String skuType, String name){
        this.skuType=skuType;
        this.name=name;
    }

    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static SkuTypeEnum getSkuTypeEnum(String skuType){
        for(SkuTypeEnum skuTypeEnum:SkuTypeEnum.values()){
            if(skuTypeEnum.getSkuType().equals(skuType)){
                return skuTypeEnum;
            }
        }
        return null;
    }
}
