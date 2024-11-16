package org.hms.services;

import org.hms.services.storage.IDataInterface;

import java.util.Optional;

// TODO: This will contain Storage class pattern for interaction with other services.
public abstract class AbstractService<T extends IDataInterface> {
    protected T storageServiceInterface;

}
