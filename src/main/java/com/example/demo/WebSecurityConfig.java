package com.example.demo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private HashMap<String, String> userAndPasswords = new HashMap<>();
    private DBConnection myConnection = new DBConnection();

    public WebSecurityConfig(){
        this.updatePasswords();
    }

    // obtaining the pairs user-password which can be allowed for the login and storing them into a Map
    public void updatePasswords(){
        userAndPasswords = myConnection.getPairUserNameAndPasswordFromAccountDatabase();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home", "/createaccount", "/users", "/searchUserByName").permitAll() // allowing which links could be accessed by not-logged-in users
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }


    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        List<UserDetails> listOfAllowedUser = new ArrayList<>();
        // Iterating over the Map and storing the accepted logIn values into the ListOfAllowedUsers
        for (Map.Entry<String, String> entry : userAndPasswords.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            UserDetails user =
                    User.withDefaultPasswordEncoder()
                            .username(key)
                            .password(value)
                            .roles("USER")
                            .build();
            listOfAllowedUser.add(user);
        }

        // returning the informations of all the userName-password pairs accepted combinations for logIn
        return new InMemoryUserDetailsManager(listOfAllowedUser);
    }

}