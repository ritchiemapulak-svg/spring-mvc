package com.spring.spring_rest.dto;

/**
 * Request body for logging in.
 */
public class AuthLoginDto {

    private String username;
    private String password;

    /**
     * Creates a login request.
     *
     * @param username the username
     * @param password the plain-text password
     */
    public AuthLoginDto(String username, String password) {
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
