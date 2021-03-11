package org.thoughts.on.java.inventory;

public enum SagaOperation {

	RESERVE_INVENTORY, INVENTORY_RESERVED, INSUFFICIENT_INVENTORY, RELEASE_INVENTORY, INVENTORY_RELEASED, PROCESS_PAYMENT, PAYMENT_SUCCESSFUL, PAYMENT_FAILED;
}