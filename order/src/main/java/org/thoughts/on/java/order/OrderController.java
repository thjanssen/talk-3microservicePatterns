package org.thoughts.on.java.order;

import java.util.logging.Logger;

import javax.persistence.NoResultException;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/order")
public class OrderController {

    Logger logger = Logger.getLogger(OrderController.class.getSimpleName());

    private PurchaseOrderRepository orderRepo;

    private OutboxUtil outboxUtil;

    private Orchestrator orchestrator;

    public OrderController(PurchaseOrderRepository orderRepo, OutboxUtil outboxUtil, Orchestrator orchestrator) {
        this.orderRepo = orderRepo;
        this.outboxUtil = outboxUtil;
        this.orchestrator = orchestrator;
    }

    @PostMapping
    public PurchaseOrder placeOrder(PurchaseOrder order) throws JsonProcessingException {
        order = this.orderRepo.save(order);
        outboxUtil.writeEventToOutbox(order, Operation.CREATED);
        orchestrator.orderCreated(order);
        return order;
    }

    @GetMapping(path = "/{id}")
    public PurchaseOrder getOrder(@PathVariable("id") Long id) {
        PurchaseOrder order = this.orderRepo.getOrderWithPositions(id);
        if (order == null) {
            throw new NoResultException();
        }
        return order;
    }
}