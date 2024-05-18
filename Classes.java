package com.mycompany.sql_dbms;

import java.io.*;
import java.util.*;

class Table implements Serializable {
    String tableName;
    List<Column> columns;
    List<Record> records;

    Table(String tableName) {
        this.tableName = tableName;
        columns = new ArrayList<>();
        records = new ArrayList<>();
    }

    void addColumn(Column column) {
        columns.add(column);
    }

    void addRecord(Record record) {
        records.add(record);
    }

    List<Record> getRecords() {
        return records;
    }

    void storeTable() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(tableName + ".bin")))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void deleteRecords(List<Record> recordsToDel) {
        records.removeAll(recordsToDel);
        this.storeTable();
    }

    @Override
    public String toString() {
        return "table name: " + tableName + "\nthe records are " + records.size() + "\n";
    }
}

class Column implements Serializable {
    String columnName;
    String dataType;

    Column(String columnName, String dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
    }
}

class Record implements Serializable {
    List<Object> values;

    Record(List<Object> values) {
        this.values = values;
    }
}

class Parser2 {
    List<String> tables;
    List<Record> batchRecords;

    Parser2() {
        tables = new ArrayList<>();
        batchRecords = new ArrayList<>();
        try {
            File file = new File("tables.bin");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                if (file.length() > 0) {
                    try (ObjectInputStream ois = new ObjectInputStream(
                            new BufferedInputStream(new FileInputStream("tables.bin")))) {
                        tables = (List<String>) ois.readObject();
                    }
                } else {
                    System.out.println("The file 'tables.bin' is empty.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void setTables() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream("tables.bin")))) {
            outputStream.writeObject(tables);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String parseCreateTable(String tableName, String[] columns) {
        Table table = new Table(tableName);
        for (String column : columns) {
            column = column.trim();
            String[] colParts = column.split(" ");
            table.addColumn(new Column(colParts[0], colParts[1]));
        }
        tables.add(table.tableName);
        setTables();
        table.storeTable();
        return "The table " + tableName + " is created.";
    }

    public String parseInsert(String statement) {
        String[] parts = statement.split("(?i)VALUES");
        String tableName = parts[0].split("(?i)INTO")[1].trim();
        Table table = getTable(tableName);
        if (table == null) {
            return "Error: Table '" + tableName + "' does not exist.";
        }
        String valuesPart = parts[1].trim().replaceAll("\\)", "");
        String[] values = valuesPart.substring(1, valuesPart.length() - 1).split(",");
        List<Object> recordValues = new ArrayList<>();

        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim().replaceAll("'", "");
            Column column = table.columns.get(i);
            if (!checkDataType(values[i], column.dataType)) {
                return "Error: Incorrect data type for column '" + column.columnName + "'. Expected: "
                        + column.dataType;
            }
            recordValues.add(values[i]);
        }
        batchRecords.add(new Record(recordValues));

        if (batchRecords.size() >= 1000) { // Adjust batch size as necessary
            table.records.addAll(batchRecords);
            table.storeTable();
            batchRecords.clear();

            File file = new File(tableName + ".bin");
            System.out.println("Batch written. Current file size: " + file.length() + " bytes");
        }
        return "The values are inserted.";
    }

    boolean checkDataType(String value, String dataType) {
        if (dataType.equals("INT")) {
            return value.matches("^-?\\d+$");
        } else if (dataType.startsWith("VARCHAR") || dataType.startsWith("CHAR")) {
            return checkVarcharDataType(value, dataType);
        } else {
            return false;
        }
    }

    boolean checkVarcharDataType(String value, String dataType) {
        int length = Integer.parseInt(dataType.replaceAll("\\D+", ""));
        return value.length() <= length && value.matches("^[a-zA-Z0-9]*$");
    }

    public void findMaxFileSize(String tableName, long maxFileSize) {
        Table table = getTable(tableName);
        if (table == null) {
            System.out.println("Error: Table '" + tableName + "' does not exist.");
            return;
        }

        int recordCount = 0;
        long startTime = System.currentTimeMillis();

        while (true) {
            List<Object> recordValues = new ArrayList<>();
            recordValues.add("data" + recordCount);
            // Adding more data to each record to increase size
            recordValues.add("more_data_" + recordCount);
            recordValues.add("even_more_data_" + recordCount);
            batchRecords.add(new Record(recordValues));
            recordCount++;

            if (recordCount % 1000 == 0) { // Adjust batch size as necessary
                table.records.addAll(batchRecords);
                table.storeTable();
                batchRecords.clear();

                File file = new File(tableName + ".bin");
                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println(
                        "Current file size: " + file.length() + " bytes, Time elapsed: " + elapsedTime + " ms");
                if (file.length() >= maxFileSize) {
                    System.out.println("Max file size reached: " + file.length() + " bytes");
                    System.out.println("Total records inserted: " + recordCount);
                    break;
                }
            }
        }

        // Flush any remaining records
        flushBatchRecords(table);
    }

    public void flushBatchRecords(Table table) {
        if (!batchRecords.isEmpty()) {
            table.records.addAll(batchRecords);
            table.storeTable();
            batchRecords.clear();
            System.out.println("Flushed remaining records to file.");
        }
    }

    public String parseSelect(String statement) {
        String result = "";
        String[] parts = statement.split("(?i)WHERE");
        String[] selectedColumns = parts[0].split("(?i)FROM")[0].substring(6).split(",");
        boolean isAll = false;
        if (selectedColumns[0].trim().equals("*"))
            isAll = true;

        String tableName = parts[0].split("(?i)FROM")[1].trim();
        tableName = tableName.replaceAll(";", "").trim();
        Table table = getTable(tableName);

        List<Record> selectedRecords;
        try {
            String condition = parts[1].trim();
            condition = condition.replaceAll(";", "").trim();
            selectedRecords = selectRecords(table, condition);
        } catch (Exception e) {
            selectedRecords = table.getRecords();
        }

        result += "Executing SELECT query on table: " + tableName + "\n";

        if (selectedRecords.isEmpty()) {
            result = "There was no such value";
            return result;
        }

        for (int j = 0; j < selectedRecords.size(); j++) {
            if (j == 0)
                result += "Selected Record:\n";

            for (int i = 0; i < table.columns.size(); i++) {
                if (isItHere(table.columns.get(i).columnName, selectedColumns) || isAll) {
                    result += table.columns.get(i).columnName + ": " + selectedRecords.get(j).values.get(i);

                    if (i != table.columns.size() - 1)
                        result += " || ";
                }
            }
            if (j < selectedRecords.size() - 1)
                result += "\n";
        }
        return result;
    }

    public String parseDelete(String statement) {
        String result = "";
        String[] parts = statement.split("(?i)WHERE");

        String tableName = parts[0].split("(?i)FROM")[1].trim();
        tableName = tableName.replaceAll(";", "").trim();
        Table table = getTable(tableName);

        List<Record> selectedRecords;

        String condition = parts[1].trim();
        condition = condition.replaceAll(";", "").trim();
        selectedRecords = selectRecords(table, condition);

        if (selectedRecords.isEmpty()) {
            result = "The condition does not match any values";
            return result;
        }

        table.deleteRecords(selectedRecords);
        result = "The record is deleted.";
        return result;
    }

    List<Record> selectRecords(Table table, String condition) {
        List<Record> selectedRecords = new ArrayList<>();
        condition = condition.replaceAll("=", " = ").replaceAll("<", " < ").replaceAll(">", " > ")
                .replaceAll("<>", " <> ").replaceAll(";", "").trim();

        String[] parts = condition.split("\\s+");
        String columnName = parts[0];
        String comparisonOperator = parts[1];
        Object value = getValueFromString(parts[2]);
        for (Record record : table.getRecords()) {
            int columnIndex = getColumnIndex(table, columnName);
            if (columnIndex != -1) {
                Object recordValue = record.values.get(columnIndex);
                if (compare(recordValue, comparisonOperator, value)) {
                    selectedRecords.add(record);
                }
            }
        }
        return selectedRecords;
    }

    int getColumnIndex(Table table, String columnName) {
        for (int i = 0; i < table.columns.size(); i++) {
            if (table.columns.get(i).columnName.equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    Object getValueFromString(String valueString) {
        return valueString;
    }

    boolean isItHere(String element, String[] array) {
        for (String s : array) {
            if (element.trim().equalsIgnoreCase(s.trim()))
                return true;
        }
        return false;
    }

    boolean compare(Object value1, String operator, Object value2) {
        String strValue1 = (String) value1;
        String strValue2 = (String) value2;

        switch (operator) {
            case "=" -> {
                return strValue1.equals(strValue2);
            }
            case ">" -> {
                try {
                    return Integer.parseInt(strValue1) > Integer.parseInt(strValue2);
                } catch (NumberFormatException e) {
                    return strValue1.compareTo(strValue2) > 0;
                }
            }
            case "<" -> {
                try {
                    return Integer.parseInt(strValue1) < Integer.parseInt(strValue2);
                } catch (NumberFormatException e) {
                    return strValue1.compareTo(strValue2) < 0;
                }
            }
            default -> {
                return false;
            }
        }
    }

    Table getTable(String tableName) {
        for (String tableNamet : tables) {
            if (tableNamet.equals(tableName)) {
                return getTableFromFile(tableName);
            }
        }
        return null;
    }

    Table getTableFromFile(String tableName) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(tableName + ".bin")))) {
            return (Table) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
