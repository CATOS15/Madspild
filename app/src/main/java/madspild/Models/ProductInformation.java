package madspild.Models;

public class ProductInformation {
    private Long gtin;
    private String name;
    private String description;
    private ProductType productType;

    public Long getGtin() {
        return gtin;
    }

    public void setGtin(Long gtin) {
        this.gtin = gtin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
