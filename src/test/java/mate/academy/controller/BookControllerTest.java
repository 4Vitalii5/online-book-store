package mate.academy.controller;

import static mate.academy.util.TestConstants.SEARCH_AUTHOR;
import static mate.academy.util.TestConstants.SEARCH_PARAM_AUTHORS;
import static mate.academy.util.TestConstants.SEARCH_PARAM_PRICES;
import static mate.academy.util.TestConstants.SEARCH_PARAM_TITLES;
import static mate.academy.util.TestConstants.SEARCH_PRICE_MAX;
import static mate.academy.util.TestConstants.SEARCH_PRICE_MIN;
import static mate.academy.util.TestConstants.SEARCH_TITLE;
import static mate.academy.util.TestConstants.VALID_FIRST_BOOK_ID;
import static mate.academy.util.TestConstants.VALID_SECOND_BOOK_ID;
import static mate.academy.util.TestUtil.CREATE_BOOK_REQUEST_DTO;
import static mate.academy.util.TestUtil.UPDATE_BOOK_REQUEST_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.dto.book.BookDto;
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
@Sql(scripts = {
        "classpath:database/books/add-books.sql",
        "classpath:database/categories/add-categories.sql",
        "classpath:database/books-categories/set-books-to-categories.sql"
})
@Sql(scripts = {
        "classpath:database/books/remove-books.sql",
        "classpath:database/categories/remove-categories.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Verify creation of new book")
    @WithMockUser(roles = "ADMIN")
    void createBook_withValidInput_returnsCreatedBook() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(CREATE_BOOK_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        BookDto responseDto = objectMapper.readValue(jsonResponse, BookDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getTitle()).isEqualTo(CREATE_BOOK_REQUEST_DTO.title());
    }

    @Test
    @DisplayName("Get all books")
    @WithMockUser(roles = {"USER", "ADMIN"})
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
    void updateBook_withValidId_returnsUpdatedBook() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(put("/books/{id}", VALID_SECOND_BOOK_ID)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(UPDATE_BOOK_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        BookDto responseDto = objectMapper.readValue(jsonResponse, BookDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getTitle()).isEqualTo(UPDATE_BOOK_REQUEST_DTO.title());
    }

    @Test
    @DisplayName("Delete book by id")
    @WithMockUser(roles = "ADMIN")
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
    void searchBooks_withValidParameters_returnsBooks() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(get("/books/search")
                        .param(SEARCH_PARAM_TITLES, SEARCH_TITLE)
                        .param(SEARCH_PARAM_AUTHORS, SEARCH_AUTHOR)
                        .param(SEARCH_PARAM_PRICES, SEARCH_PRICE_MIN, SEARCH_PRICE_MAX))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        BookDto[] responseDtos = objectMapper.readValue(jsonResponse, BookDto[].class);
        assertThat(responseDtos).isNotEmpty();
    }
}
