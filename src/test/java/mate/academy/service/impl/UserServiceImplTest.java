package mate.academy.service.impl;

import static mate.academy.util.TestConstants.DEFAULT_ROLE;
import static mate.academy.util.TestConstants.DUPLICATE_EMAIL_MESSAGE;
import static mate.academy.util.TestConstants.ENCODED_PASSWORD;
import static mate.academy.util.TestConstants.ROLE_NOT_FOUND_MESSAGE;
import static mate.academy.util.TestConstants.USER_EMAIL;
import static mate.academy.util.TestConstants.USER_PASSWORD;
import static mate.academy.util.TestUtil.NEW_USER;
import static mate.academy.util.TestUtil.ROLE;
import static mate.academy.util.TestUtil.USER;
import static mate.academy.util.TestUtil.USER_REGISTRATION_REQUEST_DTO;
import static mate.academy.util.TestUtil.USER_RESPONSE_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.exception.RegistrationException;
import mate.academy.mapper.UserMapper;
import mate.academy.repository.role.RoleRepository;
import mate.academy.repository.user.UserRepository;
import mate.academy.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ShoppingCartService shoppingCartService;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        when(userMapper.toModel(USER_REGISTRATION_REQUEST_DTO)).thenReturn(NEW_USER);
    }

    @Test
    @DisplayName("Register new user")
    void register_validUser_createsNewUser() throws RegistrationException {
        // Given
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        when(userMapper.toUserResponse(NEW_USER)).thenReturn(USER_RESPONSE_DTO);
        when(roleRepository.findByRole(DEFAULT_ROLE)).thenReturn(Optional.of(ROLE));
        when(passwordEncoder.encode(USER_PASSWORD)).thenReturn(ENCODED_PASSWORD);

        // When
        UserResponseDto actualResponseDto = userService.register(USER_REGISTRATION_REQUEST_DTO);

        // Then
        assertThat(actualResponseDto).isEqualTo(USER_RESPONSE_DTO);
        verify(userRepository, times(1)).findByEmail(USER_EMAIL);
        verify(passwordEncoder, times(1)).encode(USER_PASSWORD);
        verify(roleRepository, times(1)).findByRole(DEFAULT_ROLE);
        verify(userRepository, times(1)).save(NEW_USER);
        verify(shoppingCartService, times(1)).createShoppingCart(NEW_USER);
        verify(userMapper, times(1)).toUserResponse(NEW_USER);
    }

    @Test
    @DisplayName("Throw RegistrationException if email already exists")
    void register_emailExists_throwsRegistrationException() {
        // Given
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(USER));

        // When
        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> userService.register(USER_REGISTRATION_REQUEST_DTO));

        // Then
        assertThat(exception.getMessage()).isEqualTo(DUPLICATE_EMAIL_MESSAGE);
        verify(userRepository, times(1)).findByEmail(USER_EMAIL);
        verify(userMapper, times(1)).toModel(USER_REGISTRATION_REQUEST_DTO);
        verifyNoMoreInteractions(userRepository, passwordEncoder, roleRepository,
                shoppingCartService, userMapper);
    }

    @Test
    @DisplayName("Throw RegistrationException if role not found")
    void register_roleNotFound_throwsRegistrationException() {
        // Given
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        when(roleRepository.findByRole(DEFAULT_ROLE)).thenReturn(Optional.empty());

        // When
        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> userService.register(USER_REGISTRATION_REQUEST_DTO));

        // Then
        assertThat(exception.getMessage()).isEqualTo(ROLE_NOT_FOUND_MESSAGE);
        verify(userRepository, times(1)).findByEmail(USER_EMAIL);
        verify(userMapper, times(1)).toModel(USER_REGISTRATION_REQUEST_DTO);
        verify(roleRepository, times(1)).findByRole(DEFAULT_ROLE);
        verifyNoMoreInteractions(userRepository, passwordEncoder, roleRepository,
                shoppingCartService, userMapper);
    }
}
