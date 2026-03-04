package com.jackalcode.practice.day1;

import java.util.Objects;

public class User {

    private final String username;
    private String email;
    private final int age;

    public User(String username, String email, int age) {
        validateUsername(username);
        validateEmail(email);
        validateAge(age);

        this.username = username;
        this.email = email;
        this.age = age;
    }

    public void updateEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object == null || getClass() != object.getClass())
            return false;

        User user = (User) object;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }

    private void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age must be a positive number");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (!email.contains(("@"))) {
            throw new IllegalArgumentException("Email is invalid");
        }
    }

    private void validateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username is required");
        }

        if (username.isBlank()) {
            throw new IllegalArgumentException("Username should not be blank");
        }
    }
}
