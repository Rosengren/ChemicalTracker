package com.chemicaltracker.persistence;

import com.chemicaltracker.model.Container;

import java.util.List;

public interface ContainerDataAccessObject {

    public List<Container> getAllContainersForUser(final String username);
    public Container getContainer(final String username, final String containerName);
    public List<Container> batchGetContainers(final String username, final List<String> containerNames);
    public void addContainer(final Container container);
    public void updateContainer(final Container container);
    public void deleteContainer(final Container container);
}
