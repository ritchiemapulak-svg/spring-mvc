// package com.spring.spring_rest.service;

// import com.spring.spring_rest.entity.Auth_Role;
// import com.spring.spring_rest.entity.Auth_User;
// import com.spring.spring_rest.repository.AuthUserRepository;
// import java.util.Collection;
// import java.util.List;
// import java.util.stream.Collectors;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// /**
//  * Loads user accounts from the database for Spring Security, using
//  * {@link AuthUserRepository}. Used during login to check credentials.
//  */
// @Service
// public class CustomUserDetailsService implements UserDetailsService {

//     private final AuthUserRepository authUserRepo;

//     public CustomUserDetailsService(AuthUserRepository authUserRepo){
//         this.authUserRepo = authUserRepo;
//     }

//     /**
//      * Loads a user from the database for Spring Security.
//      *
//      * @param theUsername the username entered at login
//      * @return the user's details with encoded password and roles
//      * @throws UsernameNotFoundException if the user does not exist
//      */
//     public UserDetails loadUserByUsername(String theUsername){
//         Auth_User user = authUserRepo.findByUsername(theUsername)
//                 .orElseThrow(
//                     ()-> new UsernameNotFoundException("Username " + 
//                     theUsername + " does not exist")
//                 );
//         return new User(user.getUsername(),
//                         user.getPassword(),
//                         mapRolesToAuthorities(user.getRoles()));
//     }

//     /**
//      * Converts the user's roles into Spring Security authorities.
//      *
//      * @param roles the user's roles
//      * @return one authority per role name
//      */
//     private Collection<GrantedAuthority> mapRolesToAuthorities(
//         List<Auth_Role> roles) {
//         return roles.stream().map(role ->
//                     new SimpleGrantedAuthority(role.getName()))
//                     .collect(Collectors.toList());
//     }

// }
