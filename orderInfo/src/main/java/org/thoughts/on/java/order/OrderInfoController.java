package org.thoughts.on.java.order;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/orderinfo")
public class OrderInfoController {

    Logger logger = Logger.getLogger(OrderInfoController.class.getSimpleName());

    private OrderInfoRepository orderRepo;

    public OrderInfoController(OrderInfoRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping(path = "/{id}")
    public OrderInfo getOrder(@PathVariable Long id) throws JsonProcessingException {
        return orderRepo.getOrderInfoWithPositions(id);
    }
}