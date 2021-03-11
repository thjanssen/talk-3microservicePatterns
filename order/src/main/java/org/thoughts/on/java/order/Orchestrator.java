package org.thoughts.on.java.order;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Orchestrate the PurchaseOrder SAGA
 * 
 * Good case:
 * - Create order and set it to PENDING
 * - Reserve inventory and set order to INVENTORY_RESERVED
 * - Process payment and set order to CONFIRMED
 * 
 * No inventory:
 * - Create order and set it to PENDING
 * - Reserve inventory fails -> set order to REJECTED
 * 
 * Payment fails:
 * - Create order and set it to PENDING
 * - Reserve inventory and set order to INVENTORY_RESERVED
 * - Payment fails -> set order to PAYMENT_FAILED
 * - Release inventory and set order to REJECTED
 */
@Component
@Transactional
public class Orchestrator {

    private final Logger logger = Logger.getLogger(Orchestrator.class.getSimpleName());

    private OutboxUtil outboxUtil;

    private PurchaseOrderRepository orderRepo;

    public Orchestrator(OutboxUtil outboxUtil, PurchaseOrderRepository orderRepo) {
        this.outboxUtil = outboxUtil;
        this.orderRepo = orderRepo;
    }

    /**
     * We got a new order.
     * Next step: reserve inventory
     */
    public void orderCreated(PurchaseOrder order) throws JsonProcessingException {
        logger.info("Start SAGA");
        order.setSagaOperation(SagaOperation.RESERVE_INVENTORY);
        outboxUtil.writeSagaEventToOutbox(order, SagaOperation.RESERVE_INVENTORY);
    }

    /**
     * Inventory got reserved.
     * Next step: process payment
     */
    public void inventoryReserved(Long orderId) throws JsonProcessingException {
        logger.info("Inventory reserved -> process payment");
        PurchaseOrder order = orderRepo.findById(orderId).get();
        order.setSagaOperation(SagaOperation.PROCESS_PAYMENT);
        outboxUtil.writeSagaEventToOutbox(order, SagaOperation.PROCESS_PAYMENT);
    }

    /**
     * We don't have enough inventory and need to reject the order.
     * Next step: none
     */
    public void insufficientInventory(Long orderId) throws JsonProcessingException {
        logger.info("Insufficient inventory -> reject order");
        PurchaseOrder order = orderRepo.findById(orderId).get();
        order.setState(PurchaseOrderState.REJECTED);
        order.setSagaOperation(SagaOperation.INSUFFICIENT_INVENTORY);
        outboxUtil.writeEventToOutbox(order, Operation.REJECTED);
    }

    /**
     * Payment was successful and we can confirm the order.
     * Next step: none
     */
	public void paymentSuccessful(Long orderId) throws JsonProcessingException {
        logger.info("Payment successful -> confirm order");
        PurchaseOrder order = orderRepo.findById(orderId).get();
        order.setState(PurchaseOrderState.CONFIRMED);
        order.setSagaOperation(SagaOperation.PAYMENT_SUCCESSFUL);
        outboxUtil.writeEventToOutbox(order, Operation.CONFIRMED);
	}

    /**
     * Payment failed. We need to release the reserved inventory (compensation operation) and reject the order.
     * Next step: release inventory
     */
	public void paymentFailed(Long orderId) throws JsonProcessingException {
        logger.info("Payment failed -> release inventory");
        PurchaseOrder order = orderRepo.findById(orderId).get();
        order.setSagaOperation(SagaOperation.PAYMENT_FAILED);
        outboxUtil.writeSagaEventToOutbox(order, SagaOperation.RELEASE_INVENTORY);
	}

    /**
     * Inventory got released and we can set the order to rejected.
     * Next step: none
     */
	public void inventoryReleased(Long orderId) throws JsonProcessingException {
        logger.info("Inventory released -> reject order");
        PurchaseOrder order = orderRepo.findById(orderId).get();
        order.setState(PurchaseOrderState.REJECTED);
        order.setSagaOperation(SagaOperation.INVENTORY_RELEASED);
        outboxUtil.writeEventToOutbox(order, Operation.REJECTED);
	}

}