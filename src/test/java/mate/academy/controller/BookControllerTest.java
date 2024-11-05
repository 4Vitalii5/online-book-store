package mate.academy.controller;

import static mate.academy.constant.BookTestConstants.NEW_BOOK_AUTHOR;
import static mate.academy.constant.BookTestConstants.NEW_BOOK_CATEGORY_IDS;
import static mate.academy.constant.BookTestConstants.NEW_BOOK_COVER_IMAGE;
import static mate.academy.constant.BookTestConstants.NEW_BOOK_DESCRIPTION;
import static mate.academy.constant.BookTestConstants.NEW_BOOK_ISBN;
import static mate.academy.constant.BookTestConstants.NEW_BOOK_PRICE;
import static mate.academy.constant.BookTestConstants.NEW_BOOK_TITLE;
import static mate.academy.constant.BookTestConstants.SEARCH_AUTHOR;
import static mate.academy.constant.BookTestConstants.SEARCH_PARAM_AUTHORS;
import static mate.academy.constant.BookTestConstants.SEARCH_PARAM_PRICES;
import static mate.academy.constant.BookTestConstants.SEARCH_PARAM_TITLES;
import static mate.academy.constant.BookTestConstants.SEARCH_PRICE_MAX;
import static mate.academy.constant.BookTestConstants.SEARCH_PRICE_MIN;
import static mate.academy.constant.BookTestConstants.SEARCH_TITLE;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_AUTHOR;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_CATEGORY_IDS;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_COVER_IMAGE;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_DESCRIPTION;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_ISBN;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_PRICE;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_TITLE;
import static mate.academy.constant.BookTestConstants.VALID_FIRST_BOOK_ID;
import static mate.academy.constant.BookTestConstants.VALID_SECOND_BOOK_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookSearchParameters;
import mate.academy.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "classpath:database/clean-database.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Verify creation of new book")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/books/add-books.sql",
            "classpath:database/categories/add-categories.sql",
            "classpath:database/books-categories/set-books-to-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-books.sql",
            "classpath:database/categories/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_withValidInput_returnsCreatedBook() throws Exception {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                NEW_BOOK_TITLE,
                NEW_BOOK_AUTHOR,
                NEW_BOOK_ISBN,
                NEW_BOOK_PRICE,
                NEW_BOOK_DESCRIPTION,
                NEW_BOOK_COVER_IMAGE,
                NEW_BOOK_CATEGORY_IDS
        );
        // When
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();
        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        BookDto responseDto = objectMapper.readValue(jsonResponse, BookDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getTitle()).isEqualTo(NEW_BOOK_TITLE);
    }

    @Test
    @DisplayName("Get all books")
    @WithMockUser(roles = {"USER", "ADMIN"})
    @Sql(scripts = {
            "classpath:database/books/add-books.sql",
            "classpath:database/categories/add-categories.sql",
            "classpath:database/books-categories/set-books-to-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-books.sql",
            "classpath:database/categories/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllBooks_withValidRequest_returnsBooks() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();
        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        BookDto[] responseDtos = objectMapper.readValue(jsonResponse, BookDto[].class);
        assertThat(responseDtos).isNotEmpty();
    }

    @Test
    @DisplayName("Get book by id")
    @WithMockUser(roles = {"USER", "ADMIN"})
    @Sql(scripts = {
            "classpath:database/books/add-books.sql",
            "classpath:database/categories/add-categories.sql",
            "classpath:database/books-categories/set-books-to-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-books.sql",
            "classpath:database/categories/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_withValidId_returnsBook() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(get("/books/{id}", VALID_FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andReturn();
        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        BookDto responseDto = objectMapper.readValue(jsonResponse, BookDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo(VALID_FIRST_BOOK_ID);
    }

    @Test
    @DisplayName("Update book by id")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/books/add-books.sql",
            "classpath:database/categories/add-categories.sql",
            "classpath:database/books-categories/set-books-to-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-books.sql",
            "classpath:database/categories/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_withValidId_returnsUpdatedBook() throws Exception {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                UPDATED_BOOK_TITLE,
                UPDATED_BOOK_AUTHOR,
                UPDATED_BOOK_ISBN,
                UPDATED_BOOK_PRICE,
                UPDATED_BOOK_DESCRIPTION,
                UPDATED_BOOK_COVER_IMAGE,
                UPDATED_BOOK_CATEGORY_IDS
        );
        // When
        MvcResult mvcResult = mockMvc.perform(put("/books/{id}", VALID_SECOND_BOOK_ID)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();
        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        BookDto responseDto = objectMapper.readValue(jsonResponse, BookDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getTitle()).isEqualTo(UPDATED_BOOK_TITLE);
    }

    @Test
    @DisplayName("Delete book by id")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/books/add-books.sql",
            "classpath:database/categories/add-categories.sql",
            "classpath:database/books-categories/set-books-to-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-books.sql",
            "classpath:database/categories/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBook_withValidId_deletesBook() throws Exception {
        // When
        mockMvc.perform(delete("/books/{id}", VALID_SECOND_BOOK_ID)
                        .with(csrf()))
                .andExpect(status().isNoContent());
        // Then
        mockMvc.perform(get("/books/{id}", VALID_SECOND_BOOK_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Search books by parameters")
    @WithMockUser(roles = {"USER", "ADMIN"})
    @Sql(scripts = {
            "classpath:database/books/add-books.sql",
            "classpath:database/categories/add-categories.sql",
            "classpath:database/books-categories/set-books-to-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-books.sql",
            "classpath:database/categories/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void searchBooks_withValidParameters_returnsBooks() throws Exception {
        // Given
        BookSearchParameters searchParameters = new BookSearchParameters(
                new String[]{SEARCH_TITLE},
                new String[]{SEARCH_AUTHOR},
                new String[]{
                        SEARCH_PRICE_MIN,
                        SEARCH_PRICE_MAX
                }
        );
        // When
        MvcResult mvcResult = mockMvc.perform(get("/books/search")
                        .param(SEARCH_PARAM_TITLES, SEARCH_TITLE)
                        .param(SEARCH_PARAM_AUTHORS, SEARCH_AUTHOR)
                        .param(SEARCH_PARAM_PRICES, SEARCH_PRICE_MIN,
                                SEARCH_PRICE_MAX))
                .andExpect(status().isOk()).andReturn();
        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        BookDto[] responseDtos = objectMapper.readValue(jsonResponse, BookDto[].class);
        assertThat(responseDtos).isNotEmpty();
    }
}
