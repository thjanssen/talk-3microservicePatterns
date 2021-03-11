package org.thoughts.on.java.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {

    @Query("SELECT o FROM OrderInfo o LEFT JOIN FETCH o.positions WHERE o.id = :id")
    OrderInfo getOrderInfoWithPositions(@Param("id") Long id);
}
