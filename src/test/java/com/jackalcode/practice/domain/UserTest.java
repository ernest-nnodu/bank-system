package com.jackalcode.practice.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateValidUser() {
        User user = new User("john", "john@email.com", 25);

        assertEquals("john", user.getUsername());
        assertEquals("john@email.com", user.getEmail());
        assertEquals(25, user.getAge());
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new User(null, "john@email.com", 25)
        );
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new User("   ", "john@email.com", 25)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
                new User("john", "johnemail.com", 25)
        );
    }

    @Test
    void shouldThrowExceptionWhenAgeNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                new User("john", "john@email.com", -1)
        );
    }

    @Test
    void usersWithSameUsernameAndEmailShouldBeEqual() {
        User u1 = new User("john", "john@email.com", 25);
        User u2 = new User("john", "john@email.com", 40);

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void usersWithDifferentUsernameShouldNotBeEqual() {
        User u1 = new User("john", "john@email.com", 25);
        User u2 = new User("mary", "john@email.com", 25);

        assertNotEquals(u1, u2);
    }

    @Test
    void toStringShouldContainAllFields() {
        User user = new User("john", "john@email.com", 25);

        String output = user.toString();

        assertTrue(output.contains("john"));
        assertTrue(output.contains("john@email.com"));
        assertTrue(output.contains("25"));
    }

    @Test
    void shouldUpdateEmailSuccessfully() {
        User user = new User("john", "john@email.com", 25);

        user.updateEmail("new@email.com");

        assertEquals("new@email.com", user.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenNewEmailIsNull() {
        User user = new User("john", "john@email.com", 25);

        assertThrows(IllegalArgumentException.class,
                () -> user.updateEmail(null));
    }

    @Test
    void shouldThrowExceptionWhenNewEmailIsBlank() {
        User user = new User("john", "john@email.com", 25);

        assertThrows(IllegalArgumentException.class,
                () -> user.updateEmail("   "));
    }

    @Test
    void shouldThrowExceptionWhenNewEmailDoesNotContainAtSymbol() {
        User user = new User("john", "john@email.com", 25);

        assertThrows(IllegalArgumentException.class,
                () -> user.updateEmail("invalidemail.com"));
    }

    @Test
    void shouldNotChangeEmailWhenUpdateFails() {
        User user = new User("john", "john@email.com", 25);

        try {
            user.updateEmail("invalid");
        } catch (IllegalArgumentException ignored) {}

        assertEquals("john@email.com", user.getEmail());
    }

    @Test
    void updatingEmailShouldAffectEqualityIfEmailIsPartOfEquals() {
        User user1 = new User("john", "john@email.com", 25);
        User user2 = new User("john", "john@email.com", 30);

        assertEquals(user1, user2);

        user1.updateEmail("new@email.com");

        assertNotEquals(user1, user2);
    }
}
