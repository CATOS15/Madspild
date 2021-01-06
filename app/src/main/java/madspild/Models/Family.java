package madspild.Models;

import java.util.UUID;

public class Family {
    private UUID id;
    private String name;
    private UUID ownerid;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(UUID ownerid) {
        this.ownerid = ownerid;
    }
}
