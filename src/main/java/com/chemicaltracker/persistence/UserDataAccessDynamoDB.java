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

import org.apache.log4j.Logger;

public class UserDataAccessDynamoDB implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(UserDataAccessDynamoDB.class);

    private static final String USERS_TABLE_NAME = "Users";
    private static final String USERS_TABLE_INDEX = "Username";

    private AmazonDynamoDBClient amazonDynamoDBClient;

    public UserDataAccessDynamoDB() {
        try {
            initializeDBConnection();
        } catch (Exception e) {
            logger.error("Error occured while initializing DB Connection", e);
        }
    }

    public void initializeDBConnection() throws Exception {

        AWSCredentials credentials = null;

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Could not load credentials", e);
        }

        try {
            amazonDynamoDBClient = new AmazonDynamoDBClient(credentials);
        } catch (Exception e) {
            throw new AmazonClientException("Credentials were not valid when trying to connect to the user DB", e);
        }

        final Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        amazonDynamoDBClient.setRegion(usWest2);
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
        final PutItemResult putItemResult = amazonDynamoDBClient.putItem(putItemRequest);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put(USERS_TABLE_INDEX, new AttributeValue().withS(username));

        final GetItemRequest request = new GetItemRequest(USERS_TABLE_NAME, item);
        final GetItemResult result = amazonDynamoDBClient.getItem(request);

        if (result.getItem() == null || result.getItem().isEmpty()) { // TODO: replace item with AWS Item object
            throw new UsernameNotFoundException("Could not find username: " + username);
        }

        return convertItemToUserDetails(result.getItem());
    }

    private UserDetails convertItemToUserDetails(final Map<String, AttributeValue> item) {

        final String username = item.get("Username").getS();
        final String password = item.get("Password").getS();
        final String role = item.get("Role").getS();

        final boolean enabled = true;
        final boolean accountNonExpired = true;
        final boolean credentialsNonExpired = true;
        final boolean accountNonLocked = true;

        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));

        return new User(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);
    }
}
