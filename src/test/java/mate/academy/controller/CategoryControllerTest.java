package mate.academy.controller;

import static mate.academy.util.TestConstants.CONTENT_TYPE_JSON;
import static mate.academy.util.TestUtil.CREATE_CATEGORY_REQUEST_DTO;
import static mate.academy.util.TestUtil.FIRST_CATEGORY;
import static mate.academy.util.TestUtil.UPDATE_CATEGORY_REQUEST_DTO;
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
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Verify creation of new category")
    @WithMockUser(roles = "ADMIN")
    void createCategory_withValidInput_returnsCreatedCategory() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .with(csrf())
                        .contentType(CONTENT_TYPE_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_CATEGORY_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CategoryDto responseDto = objectMapper.readValue(jsonResponse, CategoryDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.name()).isEqualTo(CREATE_CATEGORY_REQUEST_DTO.name());
    }

    @Test
    @DisplayName("Get all categories")
    @WithMockUser(roles = {"USER", "ADMIN"})
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
    void getCategoryById_withValidId_returnsCategory() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(get("/categories/{id}", FIRST_CATEGORY.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CategoryDto responseDto = objectMapper.readValue(jsonResponse, CategoryDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.id()).isEqualTo(FIRST_CATEGORY.getId());
    }

    @Test
    @DisplayName("Update category by id")
    @WithMockUser(roles = "ADMIN")
    void updateCategory_withValidId_returnsUpdatedCategory() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(put("/categories/{id}", FIRST_CATEGORY.getId())
                        .with(csrf())
                        .contentType(CONTENT_TYPE_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_CATEGORY_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CategoryDto responseDto = objectMapper.readValue(jsonResponse, CategoryDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.name()).isEqualTo(UPDATE_CATEGORY_REQUEST_DTO.name());
    }

    @Test
    @DisplayName("Delete category by id")
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_withValidId_deletesCategory() throws Exception {
        // When
        mockMvc.perform(delete("/categories/{id}", FIRST_CATEGORY.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        // Then
        mockMvc.perform(get("/categories/{id}", FIRST_CATEGORY.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get books by category id")
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getBooksByCategoryId_withValidId_returnsBooks() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(get("/categories/{id}/books",
                        FIRST_CATEGORY.getId()))
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
