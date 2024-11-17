package org.hms.services;

import org.hms.services.storage.IDataInterface;

import java.util.Optional;

/**
 * The AbstractService class is a generic abstract class designed to provide a basic
 * interface for service classes which interact with a storage service.
 *
 * @param <T> a type parameter that extends the IDataInterface, ensuring that any
 *            specific service class implementation will work with a defined
 *            storage service interface.
 */
public abstract class AbstractService<T extends IDataInterface> {
    /**
     * The storageServiceInterface is a protected member representing the actual storage service interface
     * that the specific service class will interact with.
     * This interface is of a generic type T that extends IDataInterface, ensuring type safety and
     * consistency in the methods used for data storage operations.
     */
    protected T storageServiceInterface;
}
