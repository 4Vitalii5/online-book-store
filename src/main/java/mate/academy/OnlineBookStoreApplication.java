package mate.academy;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import mate.academy.model.Book;
import mate.academy.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class OnlineBookStoreApplication {
    private final BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Harry Potter");
            book.setAuthor("J. K. Rowling");
            book.setPrice(BigDecimal.valueOf(250));
            book.setDescription("Story about boy in a magical world");
            book.setIsbn("978-3-16-148410-0");
            book.setCoverImage("A colorful picture of Harry flying in a car");

            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }

}
