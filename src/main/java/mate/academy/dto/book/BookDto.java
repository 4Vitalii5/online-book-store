package mate.academy.dto.book;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private List<Long> categoryIds;

    public BookDto(Long id, String title, String author, String isbn, BigDecimal price,
                   String coverImage, String description, List<Long> categoryIds) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.coverImage = coverImage;
        this.description = description;
        this.categoryIds = categoryIds;
    }
}
