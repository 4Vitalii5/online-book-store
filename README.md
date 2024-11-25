<h1 style="text-align: center;">Online Book Store</h1>

## Introduction
This project is developed to implement an online bookstore where users can browse, purchase books, and manage the store. We used modern technologies such as Spring Boot for fast and secure application development.

## Technologies Used
- **Spring Boot** for building productive applications
- **Spring Data JPA** for database interactions
- **Liquibase** for database change management
- **Mapstruct** for automatic object mapping
- **Swagger** for API documentation
- **Spring Security** for securing the application
- **JWT** for authentication

## Main Features
### Controllers and Functions:
- **Book Controller**:
    - Get all books: `GET /api/books`
    - Get book by ID: `GET /api/books/{id}`
    - Create a new book: `POST /api/books`
    - Update a book by ID: `PUT /api/books/{id}`
    - Delete a book by ID: `DELETE /api/books/{id}`
    - Search books: `GET /api/books/search`
        - Example search parameters: `BookSearchParametersDto(title, author, isbn)`

- **Authentication Controller**:
    - User registration: `POST /api/auth/register`
        - Example request body:
          ```json
          {
            "email": "john.doe@example.com",
            "password": "securePassword123",
            "repeatPassword": "securePassword123",
            "firstName": "John",
            "lastName": "Doe",
            "shippingAddress": "123 Main St, City, Country"
          }
          ```
        - Example response body:
          ```json
          {
            "id": 1,
            "email": "john.doe@example.com",
            "firstName": "John",
            "lastName": "Doe",
            "shippingAddress": "123 Main St, City, Country"
          }
          ```
    - User login: `POST /api/auth/login`
        - Example request body:
          ```json
          {
            "email": "john.doe@example.com",
            "password": "securePassword123"
          }
          ```
        - Example response body:
          ```json
          {
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
          }
          ```

- **Category Controller**:
    - Get all categories: `GET /api/categories`
    - Get category by ID: `GET /api/categories/{id}`
    - Create a new category: `POST /api/categories`
    - Update category by ID: `PUT /api/categories/{id}`
    - Delete category by ID: `DELETE /api/categories/{id}`
    - Get books by category: `GET /api/categories/{id}/books`

- **Shopping Cart Controller**:
    - Get user's shopping cart: `GET /api/cart`
    - Add book to cart: `POST /api/cart`
    - Update book quantity in cart: `PUT /api/cart/items/{cartItemId}`
    - Remove book from cart: `DELETE /api/cart/items/{cartItemId}`

- **Order Controller**:
    - Place an order: `POST /api/orders`
        - Example request body:
          ```json
          {
            "shippingAddress": "Kyiv, Shevchenko ave, 1"
          }
          ```
    - Get user's order history: `GET /api/orders`
    - Update order status: `PATCH /api/orders/{id}`
        - Example request body:
          ```json
          {
            "status": "DELIVERED"
          }
          ```

- **Order Item Controller**:
    - Get items in an order: `GET /api/orders/{orderId}/items`
    - Get item in an order by ID: `GET /api/orders/{orderId}/items/{itemId}`

### Models:
- **Book**: includes fields id, title, author, isbn, price, description, coverImage.
- **User**: information about registered users, including their authentication details and personal information.
- **Role**: role of a user in the system, such as admin or user.
- **Category**: category to which a book can belong.
- **Shopping Cart**: user's shopping cart.
- **Cart Item**: item in a user's shopping cart.
- **Order**: order placed by a user.
- **Order Item**: item in a user's order.

## Setup Instructions
1. Clone this repository:
    ```sh
    git clone https://github.com/yourusername/your-repo.git
    ```
2. Navigate to the project directory:
    ```sh
    cd your-repo
    ```
3. Install dependencies:
    ```sh
    mvn install
    ```
4. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## Challenges and Solutions
### Challenge 1: Setting up CI for the project
**Solution:** Using GitHub Actions for automatic build and testing of the project.

### Challenge 2: Setting up and integrating Liquibase
**Solution:** Adding Liquibase support for managing database changes.

### Challenge 3: Handling Global Exceptions
**Solution:** Adding `CustomGlobalExceptionHandler` for handling exceptions globally across the application.

### Challenge 4: Data Validation
**Solution:** Adding meaningful validation for existing DTO classes to ensure data integrity.

### Challenge 5: Adding Pagination and Sorting
**Solution:** Implementing pagination and sorting in existing controllers to improve performance.

### Challenge 6: Security and Authentication
**Solution:** Implementing Spring Security and JWT for securing and authenticating users.

## Docker
### Docker Setup
1. Create a Docker image:
    ```sh
    docker build -t online-book-store .
    ```
2. Run the Docker container:
    ```sh
    docker-compose up
    ```

### Using Docker Hub
1. Pull the Docker image:
    ```sh
    docker pull vitalii454/online-book-store:latest
    ```
2. Run the Docker container:
    ```sh
    docker run -p 8081:8080 vitalii454/online-book-store
    ```

## Tests
## Test Results and Coverage

| Metric | Value |
|--------|-------|
| Total Tests | 92    |
| Passed Tests | 92    |
| Failed Tests | 0     |
| Skipped Tests | 0     |

### Coverage Summary

| Coverage Type | Percentage |
|---------------|------------|
| Lines         | 97%        |
| Branches      | 75%        |
| Method        | 99%        |
| Class             | 100%       |


## Postman Collection
For easy testing and interaction with the API, you can use the Postman collection containing all necessary requests. 

### Usage
1. Open Postman and import the [OnlineBookStore.postman_collection.json](src/main/resources/postman/Online-Book-Store.postman_collection.json).
2. Navigate to the imported Online Book Store collection.
3. Execute the necessary requests using the appropriate methods and parameters.

> **Note:** Before using the requests, ensure that your local server is running and you have access to the database.

## Video Demonstration
[Online Book Store demonstration in Postman](https://www.loom.com/share/5a21def77a634f698610a819174d644c?sid=a7b41903-f9c1-4838-8956-e0f64cd3b2cf)

---