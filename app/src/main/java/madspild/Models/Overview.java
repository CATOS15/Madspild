package madspild.Models;

import java.util.Date;
import java.util.UUID;

public class Overview {


    private String inventoryId;
    private long gtin;
    private Date expdate;
    private String name;
    private ProductType productType;
    private Boolean deleted;

    public Overview(String inventoryId, long gtin, Date expdate, String name, ProductType productType, Boolean deleted) {
        this.inventoryId = inventoryId;
        this.gtin = gtin;
        this.expdate = expdate;
        this.name = name;
        this.productType = productType;
        this.deleted = deleted;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public long getGtin() {
        return gtin;
    }

    public void setGtin(long gtin) {
        this.gtin = gtin;
    }

    public Date getExpdate() {
        return expdate;
    }

    public void setExpdate(Date expdate) {
        this.expdate = expdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }



}
