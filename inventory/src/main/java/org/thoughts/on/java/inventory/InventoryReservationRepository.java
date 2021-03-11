package org.thoughts.on.java.inventory;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface InventoryReservationRepository extends CrudRepository<InventoryReservation, Long> {

    List<InventoryReservation> getByOrderId(Long orderId);
}