package org.hms.services.staffmanagement;

import org.hms.entities.AbstractTable;
import java.util.List;

public abstract class StaffTable extends AbstractTable<Staff> {

    @Override
    protected String[] getHeaders() {
        return new String[]{"staffId", "age", "name", "role", "status", "gender"};
    }

    @Override
    protected Staff createValidEntryTemplate() {
        // Provide a basic template for loading entries
        return new Staff(0, 0, "", "", "", "");
    }
}
