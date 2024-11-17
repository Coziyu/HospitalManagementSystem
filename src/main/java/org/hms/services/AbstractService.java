package org.hms.services;

import org.hms.services.storage.IDataInterface;

import java.util.Optional;

public abstract class AbstractService<T extends IDataInterface> {
    protected T storageServiceInterface;

}
