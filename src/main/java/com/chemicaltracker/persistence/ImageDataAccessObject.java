package com.chemicaltracker.persistence;

public interface ImageDataAccessObject {

    public void uploadImage(final String filePath,
            final String filename, final String bucketName);
}
