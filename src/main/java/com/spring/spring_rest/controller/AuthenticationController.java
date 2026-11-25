package com.spring.spring_rest.controller;

import com.spring.spring_rest.dto.AuthLoginDto;
import com.spring.spring_rest.dto.AuthRoleRegisterDto;
import com.spring.spring_rest.dto.AuthUserRegisterDto;
import com.spring.spring_rest.entity.Auth_Role;
import com.spring.spring_rest.entity.Auth_User;
import com.spring.spring_rest.repository.AuthRoleRepository;
import com.spring.spring_rest.repository.AuthUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication under /api/auth. Handles role
 * registration, user registration and login.
 */
@RestController 
@RequestMapping("/api/auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private AuthUserRepository authUserRepo;
    private AuthRoleRepository authRoleRepo;
    private BCryptPasswordEncoder passwordEncoder;

    public AuthenticationController(
        AuthenticationManager authenticationManager, 
        AuthUserRepository authUserRepo, AuthRoleRepository authRoleRepo,
        BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.authUserRepo = authUserRepo;
        this.authRoleRepo = authRoleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new role.
     *
     * @param authRoleRegisterDto the role name
     * @return 200 if created, 400 if the role already exists
     */
    @PostMapping("/register_authorized_role")
    public ResponseEntity<String> register_authRole(
        @RequestBody AuthRoleRegisterDto authRoleRegisterDto) {
        if(authRoleRepo.existsByName(authRoleRegisterDto.getName())) {
            return new ResponseEntity<>(authRoleRegisterDto
                .getName() + " already created.", 
                HttpStatus.BAD_REQUEST);    
        }
            Auth_Role auth_role = new Auth_Role();
            auth_role.setName(authRoleRegisterDto.getName());

            authRoleRepo.save(auth_role);

            return new ResponseEntity<>(auth_role.getName() + 
            " is successfully created", HttpStatus.OK);

    }

    /**
     * Registers a new user with the USER role and a BCrypt password.
     *
     * @param authUserRegisterDto the username and plain password
     * @return 200 if created, 400 if the username is taken
     * @throws RuntimeException if the USER role does not exist
     */
    @PostMapping("/register_authorized_user")
    public ResponseEntity<String> register_authUser(
        @RequestBody AuthUserRegisterDto authUserRegisterDto) {
        if(authUserRepo.existsByUsername(authUserRegisterDto.getUsername())){
            return new ResponseEntity<>(authUserRegisterDto
                .getUsername() + " is already taken.", 
                HttpStatus.BAD_REQUEST);    
        }
            Auth_User auth_user = new Auth_User();
            auth_user.setUsername(authUserRegisterDto.getUsername());
            auth_user.setPassword(passwordEncoder
                     .encode(authUserRegisterDto.getPassword()));

            Auth_Role auth_role = authRoleRepo.findByName("USER")
                    .orElseThrow(()->
                    new RuntimeException("Role USER not found"));
            auth_user.setRoles(Collections.singletonList(auth_role));

            authUserRepo.save(auth_user);

            return new ResponseEntity<>(auth_user.getUsername() + 
                        " is successfully created", HttpStatus.OK);
       
    }

    /**
     * Logs in a user with a username and password.
     *
     * @param authLoginDto the login credentials
     * @return 200 with a success message if the credentials are valid
     */
    @PostMapping("/auth_login")
    public ResponseEntity<String>login(
        @RequestBody AuthLoginDto authLoginDto) {
        Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authLoginDto.getUsername(),
            authLoginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return new ResponseEntity<>("Sign in successfully!", HttpStatus.OK);
    }
    
    /**
     * Logs in a user and stores the security context in the HTTP session,
     * so later requests can be authenticated with the session cookie.
     *
     * @param authLoginDto the login credentials
     * @param request the HTTP request used to create the session
     * @return 200 with a JSON message if the credentials are valid
     */
    @PostMapping("/auth_login_session")
    public ResponseEntity<?> loginWithSession(@RequestBody 
        AuthLoginDto authLoginDto,
        HttpServletRequest request) {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                authLoginDto.getUsername(),
                authLoginDto.getPassword())
            );

            SecurityContextHolder.getContext()
            .setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", 
            SecurityContextHolder.getContext());

            Map<String, String> response = new HashMap<>();
            response.put("message",
            "Authenticated successfully via session cookie.");
            return ResponseEntity.ok(response);
    }

    /**
     * Logs out the current user by invalidating the HTTP session, if one
     * exists, and clearing the security context.
     *
     * @param request the HTTP request that holds the session
     * @return 401 with a logout message
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Logged out from session container.");
    }
    
    /**
     * Returns the authentication manager.
     *
     * @return the authentication manager
     */
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager (
        AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public AuthUserRepository getAuthUserRepo() {
        return authUserRepo;
    }

    public void setAuthUserRepo(AuthUserRepository authUserRepo) {
        this.authUserRepo = authUserRepo;
    }

    public AuthRoleRepository getAuthRoleRepo() {
        return authRoleRepo;
    }

    public void setAuthRoleRepo(AuthRoleRepository authRoleRepo) {
        this.authRoleRepo = authRoleRepo;
    }

    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
}
