package org.thoughts.on.java.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "book", fallback = BookClientFallback.class)
public interface BookClient {
    
    @RequestMapping(method = RequestMethod.GET, value = "/book/{bookId}")
    public Book getBook(@PathVariable Long bookId);
}