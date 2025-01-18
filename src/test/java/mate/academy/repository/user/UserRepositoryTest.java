package mate.academy.repository.user;

import static mate.academy.util.TestConstants.NON_EXISTING_USER_EMAIL;
import static mate.academy.util.TestConstants.VALID_USER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import mate.academy.model.User;
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
@Sql(scripts = {
        "classpath:database/users/add-users.sql",
})
@Sql(scripts = {
        "classpath:database/users/remove-users.sql",
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Verify findByEmail() method works")
    void findByEmail_validEmail_returnsUser() {
        // When
        Optional<User> user = userRepository.findByEmail(VALID_USER_EMAIL);

        // Then
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo(VALID_USER_EMAIL);
    }

    @Test
    @DisplayName("Verify findByEmail() returns empty for non-existing email")
    void findByEmail_nonExistingEmail_returnsEmpty() {
        // When
        Optional<User> user = userRepository.findByEmail(NON_EXISTING_USER_EMAIL);

        // Then
        assertThat(user).isNotPresent();
    }
}
