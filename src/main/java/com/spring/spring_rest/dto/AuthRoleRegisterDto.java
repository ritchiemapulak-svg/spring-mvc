package com.spring.spring_rest.dto;

/**
 * Request body for registering a role.
 */
public class AuthRoleRegisterDto {

    private String name;

    /**
     * Creates a role registration request.
     *
     * @param name the new role name
     */
    public AuthRoleRegisterDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
