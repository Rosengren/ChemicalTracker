package com.chemicaltracker.persistence.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="ReductionAgents")
public class ReductionAgent {

    private String name;

    @DynamoDBHashKey(attributeName="Name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}