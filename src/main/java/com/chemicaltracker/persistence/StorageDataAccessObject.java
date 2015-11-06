package com.chemicaltracker.persistence;

import com.chemicaltracker.model.Storage;

import java.util.List;

public interface StorageDataAccessObject {

    public List<Storage> getAllStoragesForUser(final String username);
    public Storage getStorage(final String username, final String storageID);
    public List<Storage> batchGetStorages(final String username, final List<String> storageIDs);
    public void addStorage(final Storage storage);
    public void updateStorage(final Storage storage);
    public void deleteStorage(final String username, final String storageID);
}
