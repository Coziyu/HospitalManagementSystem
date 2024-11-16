package org.hms.entities;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractTable<T extends AbstractTableEntry> implements Serializable {
    protected List<T> entries;
    protected String filePath;

    public AbstractTable() {
        entries = new ArrayList<T>();
        filePath = "";
    }

    /**
     * Adds an entry to the table. Note that this will update the file associated with the table automatically.
     * @param entry the entry to add
     * @throws Exception if the table already contains an entry with the same ID
     */
    public void addEntry(T entry) throws Exception {
        // If duplicate IDs, throw exception
        if (getEntry(entry.getTableEntryID()) != null){
            throw new Exception("Duplicate ID inside the table!");
        }
        entries.add(entry);
        saveToFile();
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean removeEntry(int tableEntryID) throws Exception {
        entries.remove(searchByAttribute(AbstractTableEntry::getTableEntryID, tableEntryID).getFirst());
        saveToFile();
        return false;
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
    public boolean replaceEntry(T newEntry) throws Exception{
        if (newEntry == null) {
            throw new IllegalArgumentException("New entry cannot be null");
        }

        int replacedTableEntryID = newEntry.getTableEntryID();
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getTableEntryID() == replacedTableEntryID) {
                entries.set(i, newEntry);
                saveToFile();
                return true;
            }
        }
        return false;
    }


    public T getEntry(int tableEntryID) {
        // id's are unique, so only the 1st entry is required.
        for (T entry : entries) {
            if (entry.getTableEntryID() == tableEntryID) {
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

    /**
     * TODO. Fill in
     * Note that saveToFile will always sort the tableEntries by TableEntryID before saving.
     * @param filename
     * @throws IOException
     */
    public void saveToFile(String filename) throws IOException {
        sortBy(AbstractTableEntry::getTableEntryID);
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
        catch (Exception e){
            if(!Objects.equals(filePath, "")){
                e.printStackTrace();
            }
        }
    }

    /**
     * TODO: Fill in
     * Note that saveToFile will always sort the tableEntries by TableEntryID before saving.
     * @throws IOException
     */
    public void saveToFile() throws IOException {
        sortBy(AbstractTableEntry::getTableEntryID);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write headers
            writer.write(String.join(",", getHeaders()));
            writer.newLine();

            // Write entries
            for (T entry : entries) {
                writer.write(entry.toCSVString());
                writer.newLine();
            }
        }
        catch (FileNotFoundException e) {
            if(!Objects.equals(filePath, "")){
                e.printStackTrace();
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
    public <U> ArrayList<T> searchByAttribute(Function<T, U> keyExtractor, U keyValue) {
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

    /**
     * <p>
     * Filters the table entries by a specified attribute using a key extractor function.
     * Returns a new table containing only entries where the extracted key matches the provided value.
     * </p>
     * <p>
     * Adding or deleting the entries in the result table will not affect the original table.
     * However, mutating the entries in the result table  <b>WILL MUTATE THE ENTRIES IN THE ORIGINAL TABLE</b>.
     * </p>
     *
     * @param keyExtractor a function that extracts the search key from an entry
     * @param keyValue the value to match against
     * @param <U> the type of the key used for searching
     * @return a new table containing only entries where the extracted key matches the keyValue
     * @throws IllegalArgumentException if keyExtractor or keyValue is null
     * @throws RuntimeException if an error occurs while creating the new table
     */
    public <U> AbstractTable<T> filterByAttribute(Function<T, U> keyExtractor, U keyValue) {
        if (keyExtractor == null || keyValue == null) {
            throw new IllegalArgumentException("Key extractor and key value cannot be null");
        }

        AbstractTable<T> results = createEmpty();

        entries.stream()
                .filter(entry -> {
                    U extractedValue = keyExtractor.apply(entry);
                    return keyValue.equals(extractedValue);
                })
                .forEach(entry -> {
                    try {
                        results.addEntry(entry);
                    } catch (Exception e) {
                        // This shouldn't happen as we are copying from a valid table.
                        throw new RuntimeException("Error adding entry to result table.", e);
                    }
                });
        return results;
    }

    /**
     * Filters the table entries by a specified condition using a key extractor function and a predicate.
     * Returns a new table containing only entries where the condition is met.
     *
     * @param <U>           the type of the key used for filtering
     * @param keyExtractor  a function that extracts the key from an entry
     * @param pred          the predicate function to test the extracted key against the provided value
     * @param predValue     the value to match against using the predicate
     * @return              a new table containing only entries where the condition is met
     * @throws IllegalArgumentException if keyExtractor, pred, or predValue is null
     * @throws RuntimeException        if an error occurs while creating the new table
     */
    public <U> AbstractTable<T> filterByCondition(Function<T, U> keyExtractor, BiPredicate<U, U> pred, U predValue) {
        if (keyExtractor == null || pred == null || predValue == null) {
            throw new IllegalArgumentException("Key extractor, predicate, and predicate value cannot be null");
        }

        AbstractTable<T> results = createEmpty();

        entries.stream()
                .filter(entry -> {
                    U extractedValue = keyExtractor.apply(entry);
                    return pred.test(extractedValue, predValue);
                })
                .forEach(entry -> {
                    try {
                        results.addEntry(entry);
                    } catch (Exception e) {
                        // This shouldn't happen as we are copying from a valid table.
                        throw new RuntimeException("Error adding entry to result table.", e);
                    }
                });

        return results;
    }

    public <U, V> AbstractTable<T> filterByCondition(Function<T, U> keyExtractorA, Function<T, V> keyExtractorB, BiPredicate<U,V> pred) {
        if (keyExtractorA == null || keyExtractorB == null || pred == null) {
            throw new IllegalArgumentException("Key extractors and predicate cannot be null");
        }

        AbstractTable<T> results = createEmpty();

        entries.stream()
                .filter(entry ->{
                    U extractedValueA = keyExtractorA.apply(entry);
                    V extractedValueB = keyExtractorB.apply(entry);
                    return pred.test(extractedValueA, extractedValueB);
                })
                .forEach(entry -> {
                    try {
                        results.addEntry(entry);
                    } catch (Exception e) {
                        // This shouldn't happen as we are copying from a valid table.
                        throw new RuntimeException("Error adding entry to result table.", e);
                    }
                });

        return results;
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
        catch (Exception e){
            if(!Objects.equals(filePath, "")){
                e.printStackTrace();
            }
        }
    }

    public void loadFromFile() throws IOException {
        entries.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip header line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                T entry = createValidEntryTemplate();
                entry.loadFromCSVString(line);
                entries.add(entry);
            }
        }
        catch (Exception e){
            if(!Objects.equals(filePath, "")){
                e.printStackTrace();
            }
        }
    }

    public int getUnusedID() {
        // Start from 0, increment until 1 unused ID encountered.
        sortBy(AbstractTableEntry::getTableEntryID);
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getTableEntryID() != i) {
                return i;
            }
        }
        return entries.size();
    }

    protected abstract String[] getHeaders();

    /**
     * This creates an empty T extends AbstractTableEntry object
     * Only it's ID is set. Other fields to be instantiated.
     * Note that this SHOULD NOT ADD the entry. Call addEntry() for that.
     * @return
     */
    protected abstract T createValidEntryTemplate();

    /**
     * Creates a new instance of the concrete table type.
     * This method must be implemented by concrete subclasses.
     * Create empty SHOULD NOT inherit the same file link!
     * @return a new empty table of the same concrete type
     */
    protected abstract AbstractTable<T> createEmpty();
}
