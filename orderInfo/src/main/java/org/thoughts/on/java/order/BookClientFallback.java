package org.thoughts.on.java.order;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class BookClientFallback implements BookClient {

    Logger log = Logger.getLogger(BookClientFallback.class.getSimpleName());

    @Override
    public Book getBook(Long bookId) {
        log.info("Request to book service failed. Use default book.");
        return new Book();
    }

}