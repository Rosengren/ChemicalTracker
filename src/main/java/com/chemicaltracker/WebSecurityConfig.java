package com.chemicaltracker;

import com.chemicaltracker.persistence.dao.UserDao;

import com.chemicaltracker.web.RestAuthenticationEntryPoint;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.userdetails.UserDetailsService;

/*
 * For more information on how the config file works, visit:
 * http://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    protected UserDetailsService users = UserDao.getInstance();

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(users);
    }

    @Configuration
    @EnableWebSecurity
    @Order(1)
    public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable() // Disable CSRF Tokens for API users
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic()
                    .authenticationEntryPoint(new RestAuthenticationEntryPoint());
        }
    }

    @Configuration
    @EnableWebSecurity
    @Order(2)
    public static class FormWebSecurityConfig extends WebSecurityConfigurerAdapter{

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/", "/register", "/homepage", "/signup", "/logout", "/welcome").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login").permitAll()
                    .and()
                    .logout().permitAll();
        }
    }
}
