package org.thoughts.on.java.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class PurchaseOrderPosition {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private int version;

    private Long bookId;

    private int quantity;

    @ManyToOne
    @JsonIgnore
    private PurchaseOrder order;

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public PurchaseOrder getOrder() {
        return order;
    }

    public void setOrder(PurchaseOrder order) {
        this.order = order;
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
}