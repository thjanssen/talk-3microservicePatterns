package org.thoughts.on.java.inventory;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.stereotype.Component;

@Component
public class OutboxUtil {
  
    private EntityManager em;

    public OutboxUtil(EntityManager em) {
        this.em = em;
    }
	
	public void writeEventToOutbox(Long orderId, Operation op) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

		ObjectNode json = mapper.createObjectNode()
			.put("id", orderId)
			.put("status", op.toString());
		
		Query q = this.em.createNativeQuery("INSERT INTO Outboxevent (id, type, aggregatetype, aggregateid, payload) VALUES (:id, :type, :aggregatetype, :aggregateid, CAST(:payload as jsonb))");
		q.setParameter("id", UUID.randomUUID());
		q.setParameter("type", op.toString());
		q.setParameter("aggregatetype", "Inventory");
		q.setParameter("aggregateid", orderId);
		q.setParameter("payload", mapper.writeValueAsString(json));
		q.executeUpdate();
	}

	public void writeSagaEventToOutbox(Long orderId, SagaOperation op) throws JsonProcessingException {	
		Query q = this.em.createNativeQuery("INSERT INTO Outboxevent (id, type, aggregatetype, aggregateid) VALUES (:id, :type, :aggregatetype, :aggregateid)");
		q.setParameter("id", UUID.randomUUID());
		q.setParameter("type", op.toString());
		q.setParameter("aggregatetype", "SagaOrder");
		q.setParameter("aggregateid", orderId);
		q.executeUpdate();
	}
}