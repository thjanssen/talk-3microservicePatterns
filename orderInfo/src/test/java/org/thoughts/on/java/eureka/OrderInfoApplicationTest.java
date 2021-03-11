package org.thoughts.on.java.eureka;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.thoughts.on.java.order.OrderInfoApplication;
import org.thoughts.on.java.order.OrderInfoController;
import org.thoughts.on.java.order.OrderInfo;
import org.thoughts.on.java.order.OrderPosition;
import org.thoughts.on.java.order.OrderState;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderInfoApplication.class)
public class OrderInfoApplicationTest {

	@Autowired
	private OrderInfoController orderController;

	// @Test
	// @Transactional
	// @Commit
	// public void testConfirmedOrder() throws JsonProcessingException {
	// 	OrderInfo order = new OrderInfo();
	// 	order.setCustomerName("Thorben Janssen");
	// 	order.setState(OrderState.PENDING);
		
	// 	Set<OrderPosition> positions = new HashSet<>();
	// 	OrderPosition p = new OrderPosition();
	// 	p.setBook("Hibernate Tips - More than 70 solutions to common Hibernate problems");
	// 	p.setOrder(order);
	// 	positions.add(p);
	// 	order.setPositions(positions);

	// 	orderController.placeOrder(order);
	// }

	// @Test
	// @Transactional
	// @Commit
	// public void testRejectedOrderOutOfStock() throws JsonProcessingException {
	// 	OrderInfo order = new OrderInfo();
	// 	order.setCustomerName("Thorben Janssen");
	// 	order.setState(OrderState.PENDING);
		
	// 	Set<OrderPosition> positions = new HashSet<>();
	// 	OrderPosition p = new OrderPosition();
	// 	p.setBook("Out of stock");
	// 	p.setOrder(order);
	// 	positions.add(p);
	// 	order.setPositions(positions);

	// 	orderController.placeOrder(order);
	// }

	// @Test
	// @Transactional
	// @Commit
	// public void testRejectedOrderUnknown() throws JsonProcessingException {
	// 	OrderInfo order = new OrderInfo();
	// 	order.setCustomerName("Thorben Janssen");
	// 	order.setState(OrderState.PENDING);
		
	// 	Set<OrderPosition> positions = new HashSet<>();
	// 	OrderPosition p = new OrderPosition();
	// 	p.setBook("Doesn't exist");
	// 	p.setOrder(order);
	// 	positions.add(p);
	// 	order.setPositions(positions);

	// 	orderController.placeOrder(order);
	// }
}
