package mate.academy.controller;

import static mate.academy.util.TestConstants.INVALID_ORDER_ID;
import static mate.academy.util.TestConstants.ITEM_ID;
import static mate.academy.util.TestConstants.NEW_ORDER_STATUS;
import static mate.academy.util.TestConstants.ORDER_ID;
import static mate.academy.util.TestConstants.VALID_USER_EMAIL;
import static mate.academy.util.TestUtil.CREATE_ORDER_REQUEST_DTO;
import static mate.academy.util.TestUtil.UPDATE_ORDER_REQUEST_DTO;
import static mate.academy.util.TestUtil.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
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
        "classpath:database/books/add-books.sql",
        "classpath:database/shopping-carts/add-shopping-carts.sql",
        "classpath:database/shopping-carts/add-cart-items.sql",
        "classpath:database/categories/add-categories.sql",
        "classpath:database/books-categories/set-books-to-categories.sql",
        "classpath:database/orders/add-orders.sql",
        "classpath:database/orders/add-order-items.sql"
})
@Sql(scripts = {
        "classpath:database/orders/remove-order-items.sql",
        "classpath:database/shopping-carts/remove-cart-items.sql",
        "classpath:database/books-categories/remove-books-categories.sql",
        "classpath:database/shopping-carts/remove-shopping-carts.sql",
        "classpath:database/orders/remove-orders.sql",
        "classpath:database/books/remove-books.sql",
        "classpath:database/categories/remove-categories.sql",
        "classpath:database/users/remove-users.sql",
        "classpath:database/roles/remove-roles.sql",
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Verify creation of new order")
    @WithUserDetails(value = VALID_USER_EMAIL)
    void addOrder_withValidInput_returnsCreatedOrder() throws Exception {
        //When
        MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(CREATE_ORDER_REQUEST_DTO)))
                .andExpect(status().isCreated())
                .andReturn();

        //Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        OrderDto responseDto = objectMapper.readValue(jsonResponse, OrderDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.userId()).isEqualTo(USER.getId());
        assertThat(responseDto.status()).isEqualTo(NEW_ORDER_STATUS);
        assertThat(responseDto.orderItems()).isNotEmpty();
    }

    @Test
    @DisplayName("Get all orders")
    @WithUserDetails(value = VALID_USER_EMAIL)
    void getOrders_withValidRequest_returnsOrders() throws Exception {
        //When
        MvcResult mvcResult = mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        OrderDto[] responseDtos = objectMapper.readValue(jsonResponse, OrderDto[].class);
        assertThat(responseDtos).isNotEmpty();
    }

    @Test
    @DisplayName("Get order items")
    @WithMockUser(username = VALID_USER_EMAIL, roles = "USER")
    void getOrderItems_withValidOrderId_returnsOrderItems() throws Exception {
        //When
        MvcResult mvcResult = mockMvc.perform(get("/orders/{orderId}/items", ORDER_ID))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        OrderItemDto[] responseDtos = objectMapper.readValue(jsonResponse, OrderItemDto[].class);
        assertThat(responseDtos).isNotEmpty();
    }

    @Test
    @DisplayName("Get order item by id")
    @WithMockUser(username = VALID_USER_EMAIL, roles = "USER")
    void getOrderItem_withValidId_returnsOrderItem() throws Exception {
        //When
        MvcResult mvcResult = mockMvc.perform(get("/orders/{orderId}/items/{id}",
                        ORDER_ID, ITEM_ID))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        OrderItemDto responseDto = objectMapper.readValue(jsonResponse, OrderItemDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.bookId()).isEqualTo(ITEM_ID);
    }

    @Test
    @DisplayName("Update order status")
    @WithMockUser(username = VALID_USER_EMAIL, roles = "ADMIN")
    void updateOrderStatus_withValidId_returnsUpdatedOrder() throws Exception {
        //When
        MvcResult mvcResult = mockMvc.perform(patch("/orders/{id}", ORDER_ID)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(UPDATE_ORDER_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        OrderDto responseDto = objectMapper.readValue(jsonResponse, OrderDto.class);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.status()).isEqualTo(UPDATE_ORDER_REQUEST_DTO.status());
    }

    @Test
    @DisplayName("Get orders with invalid user role")
    @WithMockUser(username = VALID_USER_EMAIL, roles = "ANONYMOUS")
    void getOrders_withInvalidUserRole_returnsForbidden() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update order status with invalid order id")
    @WithMockUser(username = VALID_USER_EMAIL, roles = "ADMIN")
    void updateOrderStatus_withInvalidId_returnsNotFound() throws Exception {
        mockMvc.perform(patch("/orders/{id}", INVALID_ORDER_ID)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(UPDATE_ORDER_REQUEST_DTO)))
                .andExpect(status().isNotFound());
    }
}
