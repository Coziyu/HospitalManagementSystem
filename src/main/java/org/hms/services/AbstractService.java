package org.hms.services;

import org.hms.services.storage.IDataInterface;

// TODO: This will contain Storage class pattern for interaction with other services.
public abstract class AbstractService<T extends IDataInterface> {
    protected T storageServiceInterface;
}
