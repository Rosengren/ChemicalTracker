package com.chemicaltracker.persistence;

import java.io.File;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

public class ImageDataAccessS3 implements ImageDataAccessObject {

    private static final Logger logger = Logger.getLogger(ImageDataAccessS3.class);

    private static final String IMAGE_BUCKET_NAME = "chemical-images";

    private AmazonS3Client s3client;

    public ImageDataAccessS3() {

        try {
            initializeDBConnection();
        } catch (Exception e) {
            logger.error("Error occured while initializing DB Connection", e);
        }
    }

    public void initializeDBConnection() throws AmazonClientException {

        AWSCredentials credentials = null;

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Could not load credentials", e);
        }

        try {
            s3client = new AmazonS3Client(credentials);
        } catch (Exception e) {
            throw new AmazonClientException("The provided credentials were not valid", e);
        }
    }

    @Override
    public void uploadImage(final String filePath, final String filename) {

        File imageFile = null;

        try {
            imageFile = new File(filePath);
        } catch(Exception e) {
            logger.error("Error occurred while trying to fetch file: " + filePath);
        }

        try {
            s3client.putObject(new PutObjectRequest(IMAGE_BUCKET_NAME, filename, imageFile));
        } catch (AmazonServiceException ase) {
            logger.error("The image was rejected from S3", ase);
        } catch (AmazonClientException ace) {
            logger.error("Error occurred while trying to put file: " + filePath +
                    " with filename: " + filename + " into bucket: " + IMAGE_BUCKET_NAME, ace);
        }
    }
}
