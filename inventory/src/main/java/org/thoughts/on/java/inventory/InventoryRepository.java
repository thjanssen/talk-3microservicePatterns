package org.thoughts.on.java.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository <Inventory, Long> {

    Inventory findByBookId(Long bookId);
}