package org.thoughts.on.java.inventory;

import javax.json.JsonObject;

public class OrderPosition {

    private Long id;

    private Long bookId;

    private int quantity;

    public OrderPosition(JsonObject json) {
        this.id = json.getJsonNumber("id").longValue();
        this.bookId = json.getJsonNumber("bookId").longValue();
        this.quantity = json.getJsonNumber("quantity").intValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "OrderPosition [bookId=" + bookId + ", id=" + id + ", quantity=" + quantity + "]";
    }

    
}