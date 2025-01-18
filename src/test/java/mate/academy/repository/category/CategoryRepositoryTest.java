package mate.academy.repository.category;

import static mate.academy.util.TestConstants.INVALID_CATEGORY_NAME;
import static mate.academy.util.TestUtil.FIRST_CATEGORY;
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
@Sql(scripts = "classpath:database/categories/add-categories.sql")
@Sql(scripts = "classpath:database/categories/remove-categories.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Verify findByName() method works")
    void findByName_validName_returnsCategory() {
        // When
        Category foundCategory = categoryRepository.findByName(FIRST_CATEGORY.getName());
        String actual = foundCategory.getName();

        // Then
        assertThat(foundCategory).isNotNull();
        assertThat(actual).isEqualTo(FIRST_CATEGORY.getName());
    }

    @Test
    @DisplayName("Verify findByName() method with invalid name")
    void findByName_invalidName_returnsNull() {
        // When
        Category foundCategory = categoryRepository.findByName(INVALID_CATEGORY_NAME);

        // Then
        assertThat(foundCategory).isNull();
    }
}
