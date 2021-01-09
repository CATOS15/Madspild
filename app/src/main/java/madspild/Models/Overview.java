package madspild.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

public class Overview implements Comparable<Overview> {
    private UUID productId;
    private String gtin;
    private Date expdate;
    private String name;
    private ProductType productType;
    private Boolean deleted;

    @JsonIgnore
    private boolean marked = false;

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
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

    @Override
    public int compareTo(Overview Overview) {
        return getExpdate().compareTo(Overview.getExpdate());
    }
}
