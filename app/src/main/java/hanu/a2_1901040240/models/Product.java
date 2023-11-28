package hanu.a2_1901040240.models;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String thumbnail;
    private String description;
    private int price;
    private int quantity;

    public Product(int id) {
        this.id = id;
    }

    public Product(int id, String thumbnail) {
        this.id = id;
        this.thumbnail = thumbnail;
    }

    public Product(int id, String thumbnail, String description) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    public Product(int id, String thumbnail, String description, int price) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.description = description;
        this.price = price;
    }

    public Product(int id, String thumbnail, String description, int price, int quantity) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(int id, String description, int price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public Product(String description, int price) {
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "descriptionItem='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
