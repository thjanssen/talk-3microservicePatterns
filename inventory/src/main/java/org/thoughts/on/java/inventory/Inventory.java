package org.thoughts.on.java.inventory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Inventory {

    @Id
    private Long id;

    @Version
    private int version;

    private Long bookId;

    private int quantity;

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

}