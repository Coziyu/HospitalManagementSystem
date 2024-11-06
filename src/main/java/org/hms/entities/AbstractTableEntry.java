package org.hms.entities;

import java.io.Serializable;

public abstract class AbstractTableEntry implements Serializable {
    protected int id;

    public int getId() {
        return id;
    }

    /**
     * Concrete classes must define how to save CSV.
     * @return a csvLine representation of the data.
     */
    public abstract String toCSVString();

    /**
     * Concrete classes must define how to load CSV.
     * @param csvLine comma seperated entry values.
     */
    public abstract void loadFromCSVString(String csvLine);
}
