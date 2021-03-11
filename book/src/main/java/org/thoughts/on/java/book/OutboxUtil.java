package org.thoughts.on.java.book;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.stereotype.Component;

@Component
public class OutboxUtil {
  
    private EntityManager em;

    public OutboxUtil(EntityManager em) {
        this.em = em;
    }
	
	public void writeToOutbox(Book order, Operation op) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

		ObjectNode json = mapper.createObjectNode()
			.put("id", order.getId())
			.put("title", order.getTitle())
			.put("author", order.getAuthor());
		
		Query q = this.em.createNativeQuery("INSERT INTO Outboxevent (id, type, aggregatetype, aggregateid, payload) VALUES (:id, :type, :aggregatetype, :aggregateid, CAST(:payload as jsonb))");
		q.setParameter("id", UUID.randomUUID());
		q.setParameter("type", op.toString());
		q.setParameter("aggregatetype", "Book");
		q.setParameter("aggregateid", order.getId());
		q.setParameter("payload", mapper.writeValueAsString(json));
		q.executeUpdate();
	}
}