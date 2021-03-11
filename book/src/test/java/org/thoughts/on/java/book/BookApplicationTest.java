package org.thoughts.on.java.book;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookApplication.class)
public class BookApplicationTest {

	@Autowired
	private BookController bookController;

	@Test
	@Transactional
	@Commit
	public void testPersistBook() throws JsonProcessingException {
		Book book = new Book();
		book.setTitle("Hibernate Tips - More than 70 solutions to common Hibernate problems");
		book.setAuthor("Thorben Janssen");
		
		bookController.persistBook(book);

		Assert.assertNotNull(book.getId());
	}

	@Test
	@Transactional
	@Commit
	public void testPersistAndReadBook() throws JsonProcessingException, InterruptedException {
		Book book = new Book();
		book.setTitle("Hibernate Tips - More than 70 solutions to common Hibernate problems");
		book.setAuthor("Thorben Janssen");
		
		bookController.persistBook(book);

		Book book2 = bookController.getBook(book.getId());

		Assert.assertEquals(book, book2);
	}
}
