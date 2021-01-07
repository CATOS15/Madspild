package madspild.Models;

import java.util.Date;
import java.util.UUID;

public class Inventory {
    private UUID id;
    private String serialnumber;
    private Date expdate;
    private String batchnumber;
    private Long gtin;
    private UUID familyid;
    private Boolean deleted = false;

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public Date getExpdate() {
        return expdate;
    }

    public void setExpdate(Date expdate) {
        this.expdate = expdate;
    }

    public String getBatchnumber() {
        return batchnumber;
    }

    public void setBatchnumber(String batchnumber) {
        this.batchnumber = batchnumber;
    }

    public Long getGtin() {
        return gtin;
    }

    public void setGtin(Long gtin) {
        this.gtin = gtin;
    }

    public UUID getFamilyid() {
        return familyid;
    }

    public void setFamilyid(UUID familyid) {
        this.familyid = familyid;
    }
}
