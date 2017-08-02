package rathore.book_a_book.pojos;

/**
 * Created by Rathore on 28-07-2017.
 */

public class OrderPojo {
    public String id;
    public int price;
    public String uri;
    public String name;
    public int quantity;
    public String author;
    public String address;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "OrderPojo{" +
                "id='" + id + '\'' +
                ", uri='" + uri + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", address='" + address + '\'' +
                '}';
    }
}
