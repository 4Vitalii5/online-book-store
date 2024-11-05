package mate.academy.repository.category;

import static mate.academy.constant.CategoryTestConstants.INVALID_CATEGORY_NAME;
import static mate.academy.constant.CategoryTestConstants.SAMPLE_CATEGORY_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import mate.academy.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/clean-database.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Verify findByName() method works")
    @Sql(scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByName_validName_returnsCategory() {
        // When
        Category foundCategory = categoryRepository.findByName(SAMPLE_CATEGORY_NAME);
        String actual = foundCategory.getName();
        // Then
        assertThat(foundCategory).isNotNull();
        assertThat(actual).isEqualTo(SAMPLE_CATEGORY_NAME);
    }

    @Test
    @DisplayName("Verify findByName() method with invalid name")
    @Sql(scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByName_invalidName_returnsNull() {
        // When
        Category foundCategory = categoryRepository.findByName(INVALID_CATEGORY_NAME);
        // Then
        assertThat(foundCategory).isNull();
    }
}
