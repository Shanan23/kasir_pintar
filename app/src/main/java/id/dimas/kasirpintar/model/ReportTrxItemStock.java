package id.dimas.kasirpintar.model;

import java.io.Serializable;

public class ReportTrxItemStock implements Serializable {
    public int id;
    public String name;
    public int stock;

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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
