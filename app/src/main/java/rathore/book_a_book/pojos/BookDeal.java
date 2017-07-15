package rathore.book_a_book.pojos;

/**
 * Created by Rathore on 14-Jul-17.
 */

public class BookDeal {
    String name;
    String author;
    String desc;
    String price;
    String mrp;
    String disc;
    String imgURI;

    @Override
    public String toString() {
        return "BookDeal{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", desc='" + desc + '\'' +
                ", price='" + price + '\'' +
                ", mrp='" + mrp + '\'' +
                ", disc='" + disc + '\'' +
                ", imgURI='" + imgURI + '\'' +
                '}';
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

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgURI() {
        return imgURI;
    }

    public void setImgURI(String imgURI) {
        this.imgURI = imgURI;
    }
}
