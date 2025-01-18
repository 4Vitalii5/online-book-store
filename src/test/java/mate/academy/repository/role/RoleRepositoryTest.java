package mate.academy.repository.role;

import static mate.academy.util.TestConstants.DEFAULT_ROLE;
import static mate.academy.util.TestConstants.INVALID_ROLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import mate.academy.model.Role;
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
        "classpath:database/roles/add-roles.sql",
})
@Sql(scripts = {
        "classpath:database/roles/remove-roles.sql",
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Verify findByRole() method works")
    void findByRole_validRole_returnsRole() {
        // When
        Optional<Role> role = roleRepository.findByRole(DEFAULT_ROLE);

        // Then
        assertThat(role).isPresent();
        assertThat(role.get().getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @DisplayName("Verify findByRole() returns empty for non-existing role")
    void findByRole_nonExistingRole_returnsEmpty() {
        // When
        Optional<Role> role = roleRepository.findByRole(INVALID_ROLE);

        // Then
        assertThat(role).isNotPresent();
    }
}
