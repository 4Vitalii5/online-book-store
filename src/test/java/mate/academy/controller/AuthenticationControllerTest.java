package mate.academy.controller;

import static mate.academy.util.TestConstants.DUPLICATE_EMAIL_MESSAGE;
import static mate.academy.util.TestConstants.VALID_USER_EMAIL;
import static mate.academy.util.TestUtil.USER_LOGIN_REQUEST_DTO;
import static mate.academy.util.TestUtil.USER_REGISTRATION_REQUEST_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.dto.user.UserLoginResponseDto;
import mate.academy.dto.user.UserResponseDto;
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
        "classpath:database/roles/add-roles.sql",
        "classpath:database/users/add-users.sql",
        "classpath:database/users-roles/set-users-roles.sql",
})
@Sql(scripts = {
        "classpath:database/orders/remove-order-items.sql",
        "classpath:database/shopping-carts/remove-cart-items.sql",
        "classpath:database/shopping-carts/remove-shopping-carts.sql",
        "classpath:database/orders/remove-orders.sql",
        "classpath:database/books-categories/remove-books-categories.sql",
        "classpath:database/books/remove-books.sql",
        "classpath:database/categories/remove-categories.sql",
        "classpath:database/users/remove-users.sql",
        "classpath:database/roles/remove-roles.sql",
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Register new user")
    @WithMockUser(username = VALID_USER_EMAIL, roles = "USER")
    void register_withValidInput_returnsRegisteredUser() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(post("/auth/registration")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(USER_REGISTRATION_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        UserResponseDto responseDto = objectMapper.readValue(jsonResponse, UserResponseDto.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.email()).isEqualTo(USER_REGISTRATION_REQUEST_DTO.email());
    }

    @Test
    @DisplayName("Login user")
    @WithMockUser(username = VALID_USER_EMAIL, roles = "USER")
    void login_withValidCredentials_returnsToken() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(USER_LOGIN_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        UserLoginResponseDto responseDto = objectMapper.readValue(jsonResponse,
                UserLoginResponseDto.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.token()).isNotBlank();
    }

    @Test
    @DisplayName("Register user with existing email")
    void register_withExistingEmail_throwsRegistrationException() throws Exception {
        // When
        mockMvc.perform(post("/auth/registration")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(USER_REGISTRATION_REQUEST_DTO)))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(post("/auth/registration")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(USER_REGISTRATION_REQUEST_DTO)))
                .andExpect(status().isConflict())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonResponse).contains(DUPLICATE_EMAIL_MESSAGE);
    }
}
