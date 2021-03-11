package org.thoughts.on.java.order;

import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class OrderPosition {

    @Id
    private Long id;

    @Version
    private int version;

    private Long bookId;

    private String book;

    private int quantity;

    private Boolean available;

    @ManyToOne
    @JsonIgnore
    private OrderInfo order;

    public OrderPosition(JsonValue jsonValue) {
        JsonObject json = jsonValue.asJsonObject();
        this.id = json.getJsonNumber("id").longValue();
        this.bookId = json.getJsonNumber("bookId").longValue();
        this.quantity = json.getJsonNumber("quantity").intValue();
        this.available = json.containsKey("available") ? json.getBoolean("available") : null;
    }

    public OrderPosition() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    @Override
    public String toString() {
        return "OrderPosition [available=" + available + ", book=" + book + ", id=" + id + ", order=" + order
                + ", quantity=" + quantity + ", version=" + version + "]";
    }
}