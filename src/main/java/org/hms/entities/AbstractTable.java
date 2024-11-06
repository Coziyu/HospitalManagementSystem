package org.hms.entities;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractTable<T extends AbstractTableEntry> implements Serializable {
    protected List<T> entries;
    protected String[] headers;

    public AbstractTable() {
        entries = new ArrayList<T>();
    }

    public void addEntry(T entry) {
        entries.add(entry);
    }

    public void removeEntry(T entry) {
        entries.remove(entry);
    }

    /**
     * @return a COPY of the entries.
     */
    public List<T> getEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * TODO: FILL THIS IN
     * @param keyExtractor todo
     * @param <U> todo
     */
    public <U extends Comparable<U>> void sortBy(Function<T, U> keyExtractor){
        entries.sort(Comparator.comparing(keyExtractor));
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write headers
            writer.write(String.join(",", getHeaders()));
            writer.newLine();

            // Write entries
            for (T entry : entries) {
                writer.write(entry.toCSVString());
                writer.newLine();
            }
        }
    }

    // Load from CSV file
    public void loadFromFile(String filename) throws IOException {
        entries.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Skip header line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                T entry = createEntry();
                entry.loadFromCSVString(line);
                entries.add(entry);
            }
        }
    }

    protected abstract String[] getHeaders();
    protected abstract T createEntry();
}
