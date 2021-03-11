package org.thoughts.on.java.inventory;

import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/inventory")
public class InventoryController {

    Logger logger = Logger.getLogger(InventoryController.class);

    InventoryRepository repo;

    public InventoryController(InventoryRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/book/{bookId}")
    public int getInventoryOfBook(@PathVariable Long bookId) {	
        logger.info("Return inventory for book "+bookId);
        return repo.findByBookId(bookId).getQuantity();
    }
}