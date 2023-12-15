package id.dimas.kasirpintar.model;

import java.io.Serializable;

public class ReportTrxItem implements Serializable {
    public int id;
    public String name;
    public String totalQty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }
}
