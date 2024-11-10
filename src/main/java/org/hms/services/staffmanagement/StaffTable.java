package org.hms.services.staffmanagement;

import org.hms.entities.AbstractTable;

import java.io.IOException;

public class StaffTable extends AbstractTable<Staff> {

    private static final String DEFAULT_FILENAME = "staff.csv";

    @Override
    protected String[] getHeaders() {
        return new String[]{"staffId", "age", "name", "role", "status", "gender"};
    }

    @Override
    protected Staff createValidEntryTemplate() {
        return new Staff(0, 0, "", "", "", "");
    }

    @Override
    protected AbstractTable<Staff> createEmpty() {
        return new StaffTable();
    }

    // Override saveToFile to use default filename
    public void saveToFile() throws IOException {
        super.saveToFile(DEFAULT_FILENAME);
    }

    // Override loadFromFile to use default filename
    public void loadFromFile() throws IOException {
        super.loadFromFile(DEFAULT_FILENAME);
    }
}
