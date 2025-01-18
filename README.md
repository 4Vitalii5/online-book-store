<!-- LOGO -->
<br />
<h1 align="center">
  <img src="src/main/resources/icons/online_book_store.png" alt="Project Logo" width="200"/>
  <br>Online Book Store
</h1>

<p align="center">
  <a href="#introduction">Introduction</a> •
  <a href="#technologies-used">Technologies</a> •
  <a href="#main-features">Main Features</a> •
  <a href="#setup-instructions">Setup</a> •
  <a href="#docker-setup">Docker</a> •
  <a href="#challenges-and-solutions">Challenges</a> •
  <a href="#tests">Tests</a> •
  <a href="#postman-collection">Postman</a> •
  <a href="#video-demonstration">Demonstration</a> •
  <a href="#author">Author</a>
</p>

## Introduction

Welcome to the Online Book Store project! This application aims to provide users with an intuitive and seamless platform to browse, purchase, and manage books online. Leveraging the power of modern technologies such as Spring Boot, the project ensures a secure, scalable, and high-performance environment for both shoppers and administrators. Users can effortlessly search for books, add them to their shopping cart, and complete purchases, while administrators can efficiently manage the book catalog and orders.

## Technologies Used

- **Spring Boot v3.3.4** for building productive applications
- **Spring Security v3.3.4** for securing the application
- **Spring Data JPA v3.3.4** for database interactions
- **Liquibase v4.27.0** for database change management
- **Mapstruct v1.5.5** for automatic object mapping
- **Swagger v2.1.0** for API documentation
- **JWT v0.12.5** for authentication

## Main Features

- **Book Management**: Add, update, retrieve, and delete books.
- **Authentication**: Register and log in to the system.
- **Category Management**: Add, update, retrieve, and delete book categories.
- **Shopping Cart Management**: Add books to the cart, update book quantities, and remove books from the cart.
- **Order Management**: Create, update, retrieve, and cancel orders.
- **Order Item Management**: Retrieve and manage items within orders.

## Setup Instructions

1. **Clone this repository:**
    ```sh
    git clone https://github.com/4Vitalii5/online-book-store
    ```
2. **Navigate to the project directory:**
    ```sh
    cd online-book-store
    ```
3. **Install dependencies:**
    ```sh
    mvn install
    ```
4. **Set up Environment Variables**

    - Use a `.env.template` file and add the values for these variables:
    ```plaintext
    DB_URL=your_database_url
    DB_USERNAME=your_database_username
    DB_PASSWORD=your_database_password
    ```
5. **Run the application:**
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

1. **Create a Docker image:**
    ```sh
    docker build -t online-book-store .
    ```
2. **Run the Docker container:**
    ```sh
    docker-compose up
    ```
3. **Access the Application**

   - The application will be running at `http://localhost:8081/api`.


### Using Docker Hub

1. **Pull the Docker image:**
    ```sh
    docker pull vitalii454/online-book-store:latest
    ```
2. **Run the Docker container:**
    ```sh
    docker run -p 8081:8080 vitalii454/online-book-store
    ```

## Tests

### Test Results and Coverage

| Metric        | Value |
|---------------|-------|
| Total Tests   | 92    |
| Passed Tests  | 92    |
| Failed Tests  | 0     |
| Skipped Tests | 0     |

### Coverage Summary

| Coverage Type | Percentage |
|---------------|------------|
| Lines         | 97%        |
| Branches      | 75%        |
| Methods       | 99%        |
| Classes       | 100%       |

## Postman Collection

For easy testing and interaction with the API, you can use the Postman collection containing all necessary requests.

### 🚀 Usage

1. Open Postman and import the [OnlineBookStore.postman_collection.json](src/main/resources/postman/online-book-store.postman_collection.json).
2. Navigate to the imported Online Book Store collection.
3. Execute the necessary requests using the appropriate methods and parameters.

> **Note:** Before using the requests, ensure that your local server is running, and you have access to the database.

## Video Demonstration

[Online Book Store demonstration in Postman](https://www.loom.com/share/5a21def77a634f698610a819174d644c?sid=a7b41903-f9c1-4838-8956-e0f64cd3b2cf)

## Author

👤 **Vitalii Pavlyk**

- Linkedin: [@VitaliiPavlyk](https://www.linkedin.com/in/vitalii-pavlyk-82b5aa1a1/)
- Github: [@4Vitalii5](https://github.com/4Vitalii5)

## Happy Coding!

---
