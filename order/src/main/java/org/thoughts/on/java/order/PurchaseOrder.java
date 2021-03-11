package org.thoughts.on.java.order;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
public class PurchaseOrder {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private int version;

    private String customerName;

    private Double amount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    @NotAudited
    private Set<PurchaseOrderPosition> positions = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private PurchaseOrderState state;

    @Enumerated(EnumType.STRING)
    private SagaOperation sagaOperation;

    public Long getId() {
        return id;
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

    public Set<PurchaseOrderPosition> getPositions() {
        return positions;
    }

    public void setPositions(Set<PurchaseOrderPosition> positions) {
        this.positions = positions;
    }

    public PurchaseOrderState getState() {
        return state;
    }

    public void setState(PurchaseOrderState state) {
        this.state = state;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public SagaOperation getSagaOperation() {
        return sagaOperation;
    }

    public void setSagaOperation(SagaOperation sagaOperation) {
        this.sagaOperation = sagaOperation;
    }

}