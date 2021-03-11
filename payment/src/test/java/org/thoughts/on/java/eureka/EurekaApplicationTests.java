package org.thoughts.on.java.eureka;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EurekaApplicationTests {

	@Test
	public void contextLoads() {
	}

	// @Test
	// public void json() throws JsonProcessingException {
	// 	OrderEvent e = new OrderEvent();
	// 	e.setEventType("CREATE");
	// 	e.setId(1L);

	// 	Order o = new Order();
	// 	o.setCustomer("Thorben Janssen");
	// 	o.setId(10L);
		
	// 	List<OrderPosition> positions = new ArrayList<>();
	// 	OrderPosition p = new OrderPosition();
	// 	p.setBook("Hibernate Tips");
	// 	p.setId(100L);
	// 	positions.add(p);

	// 	o.setPositions(positions);

	// 	e.setPayload(o);

	// 	ObjectMapper objectMapper = new ObjectMapper();
	// 	System.out.println(objectMapper.writeValueAsString(e));
	// }

	@Test
	public void jsonDeserialization() throws IOException {
		String event = "{\"schema\":{\"type\":\"string\",\"optional\":false},\"payload\":\"24\"}, value = {\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"string\",\"optional\":false,\"field\":\"payload\"},{\"type\":\"string\",\"optional\":false,\"field\":\"eventType\"},{\"type\":\"string\",\"optional\":false,\"field\":\"id\"}],\"optional\":false},\"payload\":{\"payload\":\"{\"id\": 24, \"customer\": \"Thorben Janssen\", \"positions\": [{\"id\": 25, \"book\": \"Hibernate Tips - More than 70 solutions to common Hibernate problems\"}]}\",\"eventType\":\"CREATE\",\"id\":\"24\"}}";
		JsonObject json = Json.createReader(new StringReader(event)).readObject();
        JsonObject payload = json.containsKey("schema") ? json.getJsonObject("payload") :json;

        String eventType = payload.getString("eventType");
        String eventPayload = payload.getString("payload");

        JsonReader payloadReader = Json.createReader(new StringReader(eventPayload));
        JsonObject payloadObject = payloadReader.readObject();

	}
}
