package org.hms.entities;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractTable<T extends AbstractTableEntry> implements Serializable {
    protected List<T> entries;
    protected String[] headers;

    public AbstractTable() {
        entries = new ArrayList<T>();
    }

    public void addEntry(T entry) throws Exception {
        // If duplicate IDs, throw exception
        if (getEntry(entry.getID()) != null){
            throw new Exception("Duplicate ID inside the table!");
        }
        entries.add(entry);
    }

    public void removeEntry(int id) throws Exception {
        entries.remove(searchByAttribute(AbstractTableEntry::getID, id).getFirst());
    }

    /**
     * @return a COPY of the entries.
     * Replaces an existing entry with a new entry based on ID matching.
     * If no entry with the matching ID exists, the new entry is not added.
     *
     * @param newEntry the new entry to replace an existing entry
     * @return true if the entry was replaced, false if no matching entry was found
     * @throws IllegalArgumentException if newEntry is null
     */
    public boolean replaceEntry(T newEntry) {
        if (newEntry == null) {
            throw new IllegalArgumentException("New entry cannot be null");
        }

        int replacedID = newEntry.getID();
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getID() == replacedID) {
                entries.set(i, newEntry);
                return true;
            }
        }
        return false;
    }


    public T getEntry(int id) {
        // id's are unique, so only the 1st entry is required.
        for (T entry : entries) {
            if (entry.getID() == id) {
                return entry;
            }
        }
        return null;
    }

    /**
     * @return A list of the entries.
     */
    public List<T> getEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * Sorts the entries based on a specified key extractor function.
     * The sorting is done in natural order of the extracted keys.
     *
     * @param keyExtractor a function that extracts a comparable key from an entry
     * @param <U> the type of the
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

    /**
     * Searches the table entries by a specified attribute using a key extractor function.
     * Returns all entries where the extracted key matches the provided value.
     *
     * @param keyExtractor a function that extracts the search key from an entry
     * @param keyValue the value to match against
     * @param <U> the type of the key used for searching
     * @return a list of entries where the extracted key matches the keyValue
     * @throws IllegalArgumentException if keyExtractor or keyValue is null
     */
    public <U>  ArrayList<T> searchByAttribute(Function<T, U> keyExtractor, U keyValue) {
        if (keyExtractor == null || keyValue == null) {
            throw new IllegalArgumentException("Key extractor and key value cannot be null");
        }
        List<T> temp = entries.stream()
                .filter(entry -> {
                    U extractedValue = keyExtractor.apply(entry);
                    return keyValue.equals(extractedValue);
                }).collect(Collectors.toList());
        return new ArrayList<>(temp);
    }

    // Load from CSV file
    public void loadFromFile(String filename) throws IOException {
        entries.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Skip header line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                T entry = createValidEntryTemplate();
                entry.loadFromCSVString(line);
                entries.add(entry);
            }
        }
    }
    public int getUnusedID() {
        // Start from 0, increment until 1 unused ID encountered.
        sortBy(AbstractTableEntry::getID);
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getID() != i) {
                return i;
            }
        }
        return entries.size();
    }

    protected abstract String[] getHeaders();

    /**
     * This creates an empty T extends AbstractTableEntry object
     * Only it's ID is set. Other fields to be instantiated.
     * @return
     */
    protected abstract T createValidEntryTemplate();
}
