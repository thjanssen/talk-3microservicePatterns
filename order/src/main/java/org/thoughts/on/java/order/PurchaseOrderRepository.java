package org.thoughts.on.java.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("SELECT o FROM PurchaseOrder o LEFT JOIN FETCH o.positions WHERE o.id = :id")
    PurchaseOrder getOrderWithPositions(@Param("id") Long id);
}