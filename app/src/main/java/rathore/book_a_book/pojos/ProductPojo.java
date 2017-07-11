package rathore.book_a_book.pojos;

import android.net.Uri;

/**
 * Created by Rathore on 10-Jul-17.
 */

public class ProductPojo {
    private String name;
    private String price;
    private String mrp;
    private String desc;
    private String uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "ProductPojo{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", mrp='" + mrp + '\'' +
                ", desc='" + desc + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
