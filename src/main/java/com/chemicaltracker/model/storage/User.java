package com.chemicaltracker.model.storage;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@DynamoDBTable(tableName="Users")
public class User implements UserDetails {

    private String username;
    private String password;
    private String role;

    private List<GrantedAuthority> authorities;

    public User() {
        authorities = new ArrayList<GrantedAuthority>();
    }

    @DynamoDBHashKey(attributeName="Username")
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @DynamoDBAttribute(attributeName="Password")
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @DynamoDBAttribute(attributeName="Role")
    public String getRole() { return role; }
    public void setRole(String role) { 
        this.role = role;
        authorities.add(new SimpleGrantedAuthority(role));
    }

    @DynamoDBIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    /** 
     *   Future Implementations:
     *      The following methods can be used to create
     *      a more robust user management system.
     **/

    @Override
    @DynamoDBIgnore
    public boolean isEnabled() { return true; }

    @Override
    @DynamoDBIgnore
    public boolean isAccountNonExpired() { return true; }

    @Override
    @DynamoDBIgnore
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    @DynamoDBIgnore
    public boolean isAccountNonLocked() { return true; }
}
