package org.thoughts.on.java.book;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/book")
public class BookController {

    Logger logger = Logger.getLogger(BookController.class.getSimpleName());

    private BookRepository bookRepo;

    private OutboxUtil outboxUtil;

    public BookController(BookRepository bookRepo, OutboxUtil outboxUtil) {
        this.bookRepo = bookRepo;
        this.outboxUtil = outboxUtil;
    }

    @PostMapping
    public Book persistBook(Book book) throws JsonProcessingException {
        book = this.bookRepo.save(book);
        outboxUtil.writeToOutbox(book, Operation.CREATE);
        return book;
    }

    @GetMapping(path = "/{bookId}")
    public Book getBook(@PathVariable Long bookId) throws JsonProcessingException, InterruptedException {
        // logger.info("Sleeping for 5000ms before returning book with id "+bookId);
        // Thread.sleep(5000);
        // logger.info("Done ... returning book now");
        return this.bookRepo.findById(bookId).get();
    }
}