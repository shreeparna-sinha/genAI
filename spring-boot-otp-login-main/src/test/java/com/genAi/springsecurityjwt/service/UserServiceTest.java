package com.genAi.springsecurityjwt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.genAi.springsecurityjwt.model.User;
import com.genAi.springsecurityjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldHashPasswordAndSaveUser() {
        User user = new User();
        user.setUserName("john");
        user.setEmail("john@example.com");
        user.setPhoneNo("9876543210");
        user.setPassword("secret123");

        when(passwordEncoder.encode("secret123")).thenReturn("encodedSecret");
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.registerUser(user);

        assertEquals("encodedSecret", savedUser.getPassword());
        assertNull(savedUser.getId());
        assertNull(savedUser.getCart());
        verify(userRepository).save(user);
    }

    @Test
    void registerUser_shouldRejectNullUser() {
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null));
    }

    @Test
    void registerUser_shouldRejectBlankUserFields() {
        User userWithMissingEmail = new User();
        userWithMissingEmail.setUserName("john");
        userWithMissingEmail.setPhoneNo("9876543210");
        userWithMissingEmail.setPassword("secret123");

        User userWithMissingPhone = new User();
        userWithMissingPhone.setUserName("john");
        userWithMissingPhone.setEmail("john@example.com");
        userWithMissingPhone.setPassword("secret123");

        User userWithMissingPassword = new User();
        userWithMissingPassword.setUserName("john");
        userWithMissingPassword.setEmail("john@example.com");
        userWithMissingPassword.setPhoneNo("9876543210");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(userWithMissingEmail));
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(userWithMissingPhone));
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(userWithMissingPassword));
    }

    @Test
    void registerUser_shouldRejectDuplicateEmailAndPhoneNumber() {
        User user = new User();
        user.setUserName("john");
        user.setEmail("john@example.com");
        user.setPhoneNo("9876543210");
        user.setPassword("secret123");

        when(userRepository.findByEmail("john@example.com")).thenReturn(new User());

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void isEmailAlreadyRegistered_shouldReturnFalseForBlankEmail() {
        assertFalse(userService.isEmailAlreadyRegistered(null));
        assertFalse(userService.isEmailAlreadyRegistered("   "));
        verifyNoInteractions(userRepository);
    }

    @Test
    void isEmailAlreadyRegistered_shouldReturnTrueWhenRepositoryFindsEmail() {
        User existingUser = new User();
        when(userRepository.findByEmail("john@example.com")).thenReturn(existingUser);

        assertTrue(userService.isEmailAlreadyRegistered("john@example.com"));
    }

    @Test
    void findByPhoneNo_shouldReturnNullForBlankPhoneNumber() {
        assertNull(userService.findByPhoneNo(null));
        assertNull(userService.findByPhoneNo("   "));
        verifyNoInteractions(userRepository);
    }

    @Test
    void findByPhoneNo_shouldDelegateToRepository() {
        User user = new User();
        when(userRepository.findByPhoneNo("9876543210")).thenReturn(user);

        assertEquals(user, userService.findByPhoneNo("9876543210"));
    }
}
