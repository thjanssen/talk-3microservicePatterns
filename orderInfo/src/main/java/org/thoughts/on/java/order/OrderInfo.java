package org.thoughts.on.java.order;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity
public class OrderInfo {

    @Id
    private Long id;

    @Version
    private int version;

    private String customerName;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderPosition> positions = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private OrderState state;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Set<OrderPosition> getPositions() {
        return positions;
    }

    public void setPositions(Set<OrderPosition> positions) {
        this.positions = positions;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }
    
}