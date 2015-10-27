package com.chemicaltracker.persistence;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.Item;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UsersDataAccessDynamoDB implements UserDetailsService {

    private static final String USERS_TABLE_NAME = "Users";
    private static final String USERS_TABLE_INDEX = "Username";

    private AmazonDynamoDBClient dynamoDB;

    public UsersDataAccessDynamoDB() {
        try {
            initializeDBConnection();
        } catch (Exception e) {
            System.out.println("Error occured while initializing DB Connection");
        }
    }

    public void initializeDBConnection() throws Exception {

        AWSCredentials credentials = null;

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Could not load credentials", e);
        }

        dynamoDB = new AmazonDynamoDBClient(credentials);
        final Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        dynamoDB.setRegion(usWest2);
    }

    //@Override
    public void createUser(final String username, final String password, final String role) {
        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        // TODO: REMOVE ALL HARD CODED VALUES
        item.put("Username", new AttributeValue().withS(username));
        item.put("Password", new AttributeValue().withS(password));

        if (role == "") {
            item.put("Role", new AttributeValue().withS("USER"));
        } else {
            item.put("Role", new AttributeValue().withS(role));
        }

        final PutItemRequest putItemRequest = new PutItemRequest(USERS_TABLE_NAME, item);
        final PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put(USERS_TABLE_INDEX, new AttributeValue().withS(username));

        final GetItemRequest request = new GetItemRequest(USERS_TABLE_NAME, item);
        final GetItemResult result = dynamoDB.getItem(request);

        if (result.getItem() == null || result.getItem().isEmpty()) { // TODO: replace item with AWS Item object
            throw new UsernameNotFoundException("Could not find username: " + username);
        }

        return convertItemToUserDetails(result.getItem());
    }

    private UserDetails convertItemToUserDetails(final Map<String, AttributeValue> item) {

        String username = item.get("Username").getS();
        String password = item.get("Password").getS();
        String role = item.get("Role").getS();

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));

        return new User(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);
    }
}
