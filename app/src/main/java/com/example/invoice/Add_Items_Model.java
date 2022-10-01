package com.example.invoice;

public class Add_Items_Model {
    String id, itemname, itemsize, itemrate, quantity, total, discount, subtotal,sizetype,cateogry;

    public Add_Items_Model(String id, String itemname, String itemsize, String itemrate, String quantity, String total, String discount, String subtotal, String sizetype, String cateogry) {
        this.id = id;
        this.itemname = itemname;
        this.itemsize = itemsize;
        this.itemrate = itemrate;
        this.quantity = quantity;
        this.total = total;
        this.discount = discount;
        this.subtotal = subtotal;
        this.sizetype = sizetype;
        this.cateogry = cateogry;
    }

    public String getId() {
        return id;
    }

    public String getItemname() {
        return itemname;
    }

    public String getItemsize() {
        return itemsize;
    }

    public String getItemrate() {
        return itemrate;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTotal() {
        return total;
    }

    public String getDiscount() {
        return discount;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getSizetype() {
        return sizetype;
    }

    public String getCateogry() {
        return cateogry;
    }
}