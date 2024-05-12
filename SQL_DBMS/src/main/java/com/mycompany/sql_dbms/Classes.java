package com.mycompany.sql_dbms;

import java.io.Serializable;
import java.util.*;
import java.io.*;

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
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(tableName+".bin"))) {
            outputStream.writeObject(this);
            // System.out.println("Table " + tableName + " stored in binary file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void deleteRecords(List<Record> recordsToDel){
        records.removeAll(recordsToDel);
        this.storeTable();
    }

    @Override
    public String toString() {
        String r = "table name: " + tableName + "\nthe records is " + records.size() + "\n";

        return r;
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
// **************************************************************************//

class Parser2 {
    List<String> tables;

    Parser2() {
        tables = new ArrayList<>();
        try {
            File file = new File("tables.bin");
            if (!file.exists()) {
                // Create the file if it doesn't exist
                file.createNewFile();
            } else {
                // Read the list of tables from the file
                if (file.length() > 0) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tables.bin"))) {
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

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("tables.bin"))) {
            outputStream.writeObject(tables);
            // System.out.println("Table " + tableName + " stored in binary file: " +
            // filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    void parse(String query) {
//        // validation code
//    }

    public String parseCreateTable(String tableName, String[] columns) {
        /*
         * String[] parts = statement.split("\\(");
         * String tableName = parts[0].substring("CREATE TABLE".length()).trim();
         * String columnsPart = parts[1].substring(0, parts[1].length() - 1);
         * String[] columns = columnsPart.split(",");
         */
        Table table = new Table(tableName);
        for (String column : columns) {
            column = column.trim();
            String[] colParts = column.split(" ");
            table.addColumn(new Column(colParts[0], colParts[1]));
        }
        tables.add(table.tableName);
        setTables();
        table.storeTable();
        return "the table " + tableName + " is created";
    }

    public String parseInsert(String statement) {
        String[] parts = statement.split("(?i)VALUES");
        //String[] with = parts[0].split(" ");

        String tableName = parts[0].split("(?i)INTO")[1].trim(); // Extracting the table name from the INSERT INTO statement
        Table table = getTable(tableName);
        if (table == null) {
            return "Error: Table '" + tableName + "' does not exist.";
        }
        String valuesPart = parts[1].trim().replaceAll("\\)", "");
        String[] values = valuesPart.substring(1, valuesPart.length() - 1).split(",");
        List<Object> recordValues = new ArrayList<>();
        int count = 0;

        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim().replaceAll("'", "");
            // Check if the data type matches for the column
            Column column = table.columns.get(i);
            if (!checkDataType(values[i], column.dataType)) {
                return "Error: Incorrect data type for column '" + column.columnName + "'. Expected: " + column.dataType;
            }
            recordValues.add(values[i]);
        }
        table.addRecord(new Record(recordValues));
        table.storeTable();

        System.out.println();

            return "The values are inserted";

    }

    boolean checkDataType(String value, String dataType) {
        if (dataType .equals("INT")){

            return value.matches("^-?\\d+$");
        }
        else if (dataType.startsWith("VARCHAR")){
            return checkVarcharDataType(value, dataType);
        }
        else if (dataType.startsWith("CHAR")){
            return checkVarcharDataType(value, dataType);
        }
        else {
            return false;
        }

    }

    boolean checkVarcharDataType(String value, String dataType) {
        // Extract the length from the VARCHAR type
        int length = Integer.parseInt(dataType.replaceAll("\\D+", ""));
        return value.length() <= length && value.matches("^[a-zA-Z0-9]*$");
    }


    public String parseSelect(String statement) {
        String ruselt = "";
        String[] parts = statement.split("(?i)WHERE");
        String [] selectedColomns = parts[0].split("(?i)FROM")[0].substring(6).split(",");
        boolean isAll = false ;
        if (selectedColomns[0].trim().equals("*"))
            isAll = true;

        String tableName = parts[0].split("(?i)FROM")[1].trim();
        tableName = tableName.replaceAll(";", "").trim();
        // System.out.println(tableName);
        Table table = getTable(tableName);

        // System.out.println(table.toString());
        List<Record> selectedRecords;
        try {
            String condition = parts[1].trim();
            condition = condition.replaceAll(";", "").trim();
            selectedRecords = selectRecords(table, condition);
        } catch (Exception e) {
            selectedRecords = table.getRecords();
        }

        ruselt += "Executing SELECT query on table: " + tableName + "\n";


        if (selectedRecords.isEmpty()) { // when searching for a nonexistent value.
            ruselt = "there was no such value";
            return ruselt;
        }

        for (int j = 0; j < selectedRecords.size(); j++) {
            // System.out.println("Selected Record:");
            if (j == 0)
                ruselt += "Selected Record:\n";

            for (int i = 0; i < table.columns.size(); i++) {
                // System.out.println(table.columns.get(i).columnName + ": " +
                // record.values.get(i));
                if(isItHere(table.columns.get(i).columnName,selectedColomns )||isAll){
                    ruselt += table.columns.get(i).columnName + ": " + selectedRecords.get(j).values.get(i);

                    if (i != table.columns.size() - 1)
                        ruselt += " || ";
                }
            }
            if (j < selectedRecords.size() - 1)
                ruselt += "\n";
        }
        //ruselt = ruselt.replaceAll(" || \n", "\n");
        return ruselt;
    }
    public String parseDelete(String statement) {
        String ruselt = "";
        String[] parts = statement.split("(?i)WHERE");
        //String [] selectedColomns = parts[0].split("(?i)FROM")[0].substring(6).split(",");
        //boolean isAll = false ;
        //if (selectedColomns[0].trim().equals("*"))
        //isAll = true;

        String tableName = parts[0].split("(?i)FROM")[1].trim();
        tableName = tableName.replaceAll(";", "").trim();

        // System.out.println(tableName);
        Table table = getTable(tableName);

        // System.out.println(table.toString());
        List<Record> selectedRecords;

        String condition = parts[1].trim();
        condition = condition.replaceAll(";", "").trim();
        selectedRecords = selectRecords(table, condition);


        //ruselt += "Executing SELECT query on table: " + tableName + "\n";


        if (selectedRecords.isEmpty()) { // when searching for a nonexistent value.
            ruselt = "the condition do not match any values";
            return ruselt;
        }

        table.deleteRecords(selectedRecords);
        ruselt = "the record is deleted.";
        return ruselt;
    }
    List<Record> selectRecords(Table table, String condition) {
        List<Record> selectedRecords = new ArrayList<>();
        // Assuming condition is in the form "columnName comparisonOperator value"
        condition = condition.replaceAll("=", " = ");
        condition = condition.replaceAll("<", " < ");
        condition = condition.replaceAll(">", " > ");
        condition = condition.replaceAll("<>", " <> ");
        condition = condition.replaceAll(";", "");
        condition = condition.trim();

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
        // For simplicity, assuming all values are strings
        return valueString;
    }
    boolean  isItHere(String element, String [] array){
        for(int i=0;i<array.length;i++){
            if(element.trim().equalsIgnoreCase(array[i].trim()))
                return true;
        }

        return false ;
    }
    boolean compare(Object value1, String operator, Object value2) {
        // For simplicity, assuming value1 and value2 are strings
        String strValue1 = (String) value1;
        String strValue2 = (String) value2;


        System .out.println(strValue1+" "+strValue2);
        switch (operator) {
            case "=" -> {
                return strValue1.equals(strValue2);
            }
            case ">" -> {
                try {
                    return Integer.parseInt(strValue1) > Integer.parseInt(strValue2);
                } catch (NumberFormatException e) {
                    // Handle non-integer comparison
                    return strValue1.compareTo(strValue2) > 0;
                }
            }
            case "<" -> {
                try {
                    return Integer.parseInt(strValue1) < Integer.parseInt(strValue2);
                } catch (NumberFormatException e) {
                    // Handle non-integer comparison
                    return strValue1.compareTo(strValue2) < 0;
                }
            }
            default -> {
                return false;
            }
        }
        // Adding other comparison operators as needed
    }

    public String parseUpdate(String updateStatement) {
        try {
            String[] parts = updateStatement.split("SET");
            String tableName = extractTableName(parts[0].trim());
            String[] afterSet = parts[1].split("WHERE");
            String updatePortion = afterSet[0].trim();
            String wherePortion = afterSet.length > 1 ? afterSet[1].trim() : null;

            Map<String, String> columnValues = parseColumnValues(updatePortion);
            Table table = getTable(tableName);
            if (table == null) {
                return "Error: Table '" + tableName + "' does not exist.";
            }

            List<Record> recordsToUpdate = (wherePortion != null) ? filterRecords(table, wherePortion) : table.getRecords();
            int updatedCount = updateRecords(recordsToUpdate, columnValues, table);

            if (updatedCount > 0) {
                table.storeTable(); // Ensure to store only if updates were made
                return "Update successful. " + updatedCount + " record(s) updated.";
            } else {
                return "No records updated. Check your WHERE clause or data.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing the update statement: " + e.getMessage();
        }
    }



    // Extracts the table name from the part of the statement before 'SET'
    private String extractTableName(String part) {
        return part.replace("UPDATE", "").trim();
    }

    // Parses the column-value pairs from the update portion
    private Map<String, String> parseColumnValues(String updatePortion) {
        Map<String, String> columnValues = new HashMap<>();
        String[] pairs = updatePortion.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            columnValues.put(keyValue[0].trim(), keyValue[1].trim().replaceAll("'", ""));
        }
        return columnValues;
    }
    private List<Record> filterRecords(Table table, String whereCondition) {
        List<Record> filteredRecords = new ArrayList<>();

        // Splitting the condition manually
        String[] parts = whereCondition.split(" ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid WHERE clause format.");
        }

        String columnName = parts[0].trim();
        String operator = parts[1].trim();
        String value = whereCondition.substring(whereCondition.indexOf(operator) + operator.length()).trim();
        value = value.replaceAll("'", "").trim(); // Clean quotes if value is a string
        value = value.replaceAll(";", "").trim(); // Clean quotes if value is a string
        System.out.println(value);


        int columnIndex = getColumnIndex(table, columnName);
        if (columnIndex == -1) {
            System.err.println("Error: Column '" + columnName + "' not found in table '" + table.tableName + "'.");
            return filteredRecords; // Returning empty list if column not found
        }

        // Evaluating each record against the parsed condition

        for (Record record : table.getRecords()) {
            Object recordValue = record.values.get(columnIndex);

            if (compare(recordValue, operator, value)) {

                filteredRecords.add(record);
            }
        }

        return filteredRecords;
    }

    private int updateRecords(List<Record> records, Map<String, String> columnValues, Table table) {
        int updatedCount = 0;
        System.out.println(records.size());
        for (Record record : records) {
            boolean isUpdated = false;
            System.out.println("Checking record: " + record.values);
            for (Map.Entry<String, String> entry : columnValues.entrySet()) {
                int columnIndex = getColumnIndex(table, entry.getKey());
                if (columnIndex != -1) {
                    Object currentValue = record.values.get(columnIndex);
                    Object newValue = entry.getValue();
                    System.out.println("Current value: " + currentValue + ", New value: " + newValue);
                    if (!currentValue.equals(newValue)) {
                        record.values.set(columnIndex, newValue);
                        isUpdated = true;
                        System.out.println("Updated column: " + entry.getKey() + " from " + currentValue + " to " + newValue);
                    }
                } else {
                    System.out.println("Column " + entry.getKey() + " not found.");
                }
            }
            if (isUpdated) {
                updatedCount++;
            }
        }
        return updatedCount;
    }

    Table getTable(String tableName) {
        for (String tableNamet : tables) {
            if (tableNamet.equals(tableName)) {
                return getTableFromfile(tableName);
            }
        }
        return null;
    }

    Table getTableFromfile(String tableName) {
        Table table;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tableName + ".bin"))) {

            table = (Table) ois.readObject();
            // System.out.println(table.toString());
            return table;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

/*
 * public class Classes {
 * public static void main(String[] args) {
 * //testing
 * String query = "CREATE TABLE Students (id INT, name STRING, age INT);" +
 * "INSERT INTO Students VALUES (1, 'Alice', 20);" +
 * "INSERT INTO Students VALUES (2, 'Bob', 22);" +
 * "SELECT * FROM Students WHERE age > 20;";
 * Parser parser = new Parser();
 * parser.parse(query);
 * }
 * }
 */
