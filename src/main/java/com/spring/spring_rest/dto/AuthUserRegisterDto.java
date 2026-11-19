package com.spring.spring_rest.dto;

/**
 * Request body for registering a user.
 */
public class AuthUserRegisterDto {

    private String username;
    private String password;

    /**
     * Creates a user registration request.
     *
     * @param username the new username
     * @param password the plain-text password to be encoded
     */
    public AuthUserRegisterDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
