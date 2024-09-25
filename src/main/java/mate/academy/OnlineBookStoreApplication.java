package mate.academy;

import java.math.BigDecimal;
import mate.academy.model.Book;
import mate.academy.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book harryPotter = new Book();
            harryPotter.setTitle("Harry Potter");
            harryPotter.setAuthor("J. K. Rowling");
            harryPotter.setPrice(BigDecimal.valueOf(250));
            harryPotter.setDescription("Story about boy in a magical world");
            harryPotter.setIsbn("978-3-16-148410-0");
            harryPotter.setCoverImage("A colorful picture of Harry flying in a car");

            bookService.save(harryPotter);
            System.out.println(bookService.findAll());
        };
    }

}
