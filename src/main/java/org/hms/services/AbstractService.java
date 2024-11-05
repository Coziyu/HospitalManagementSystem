package org.hms.services;

import org.hms.services.storage.IStorageAccess;

// TODO: This will contain Storage class pattern for interaction with other services.
public abstract class AbstractService<T extends IStorageAccess> {
    protected T storageService;
}
