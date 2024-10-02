package mate.academy.service;

import java.util.List;
import mate.academy.dto.BookDto;
import mate.academy.dto.BookSearchParameters;
import mate.academy.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto updateBookById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters searchParameters);
}
