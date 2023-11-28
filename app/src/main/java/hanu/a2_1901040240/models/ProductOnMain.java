package hanu.a2_1901040240.models;

public class ProductOnMain {
    // Initialize variables
    private String description;
    private int price;
    private int quantity;
    private String thumbnail;

    public ProductOnMain(String thumbnail, int quantity, String description, int price) {
        this.thumbnail = thumbnail;
        this.quantity = quantity;
        this.description = description;
        this.price = price;
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
}
