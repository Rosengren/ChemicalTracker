package com.chemicaltracker.controller;

import com.chemicaltracker.persistence.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService users = UserDAO.getInstance();

    protected void configure(HttpSecurity http) throws Exception {

        // Authentication for website
        http.authorizeRequests()
            .antMatchers("/css/**", "/js/**", "/img/**", "/", "/homepage",
                         "/api/test/**", "/signup").permitAll()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
        .logout()
            .permitAll();

        // Basic Authentication for Android devices
        http
            .authorizeRequests()
                .antMatchers("/api/**")
                    .hasRole("USER")
                    .and()
                .httpBasic();

        // Ignore CSRF Token requirement for Android
        http.csrf().ignoringAntMatchers("/api/**");
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(users);
    }
}
