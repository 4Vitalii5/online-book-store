package mate.academy.controller;

import static mate.academy.constant.CategoryTestConstants.CATEGORY_ID;
import static mate.academy.constant.CategoryTestConstants.CONTENT_TYPE_JSON;
import static mate.academy.constant.CategoryTestConstants.SECOND_CATEGORY_DESCRIPTION;
import static mate.academy.constant.CategoryTestConstants.SECOND_CATEGORY_NAME;
import static mate.academy.constant.CategoryTestConstants.UPDATED_CATEGORY_DESCRIPTION;
import static mate.academy.constant.CategoryTestConstants.UPDATED_CATEGORY_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryRequestDto;
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
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Verify creation of new category")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_withValidInput_returnsCreatedCategory() throws Exception {
        //Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                SECOND_CATEGORY_NAME,
                SECOND_CATEGORY_DESCRIPTION
        );

        //When
        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .with(csrf())
                        .contentType(CONTENT_TYPE_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CategoryDto responseDto = objectMapper.readValue(jsonResponse, CategoryDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.name()).isEqualTo(SECOND_CATEGORY_NAME);
    }

    @Test
    @DisplayName("Get all categories")
    @WithMockUser(roles = {"USER", "ADMIN"})
    @Sql(scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllCategories_withValidRequest_returnsCategories() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CategoryDto[] responseDtos = objectMapper.readValue(jsonResponse, CategoryDto[].class);

        assertThat(responseDtos).isNotEmpty();
    }

    @Test
    @DisplayName("Get category by id")
    @WithMockUser(roles = {"USER", "ADMIN"})
    @Sql(scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_withValidId_returnsCategory() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(get("/categories/{id}", CATEGORY_ID))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CategoryDto responseDto = objectMapper.readValue(jsonResponse, CategoryDto.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Update category by id")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_withValidId_returnsUpdatedCategory() throws Exception {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                UPDATED_CATEGORY_NAME, UPDATED_CATEGORY_DESCRIPTION
        );

        // When
        MvcResult mvcResult = mockMvc.perform(put("/categories/{id}", CATEGORY_ID)
                        .with(csrf())
                        .contentType(CONTENT_TYPE_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CategoryDto responseDto = objectMapper.readValue(jsonResponse, CategoryDto.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.name()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @DisplayName("Delete category by id")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_withValidId_deletesCategory() throws Exception {
        // When
        mockMvc.perform(delete("/categories/{id}", CATEGORY_ID)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        // Then
        mockMvc.perform(get("/categories/{id}", CATEGORY_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get books by category id")
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
    void getBooksByCategoryId_withValidId_returnsBooks() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(get("/categories/{id}/books", CATEGORY_ID))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        BookDtoWithoutCategoryIds[] responseDtos = objectMapper.readValue(
                jsonResponse, BookDtoWithoutCategoryIds[].class
        );

        assertThat(responseDtos).isNotEmpty();
    }
}
