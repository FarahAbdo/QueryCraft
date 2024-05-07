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

    void storeTable(String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(this);
            System.out.println("Table " + tableName + " stored in binary file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tables.bin"));
                    tables = (List<String>) ois.readObject();
                    ois.close();
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

    void parse(String query) {
        // validation code
    }

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
        table.storeTable(tableName + ".bin");
        return "the table " + tableName + " is created";
    }

    public String parseInsert(String statement) {
        String[] parts = statement.split("(?i)VALUES");
        String tableName = parts[0].split("(?i)INTO")[1].trim(); // Extracting the table name from the INSERT INTO
                                                                 // statement
        Table table = getTable(tableName);
        if (table == null) {
            // System.err.println("Error: Table '" + tableName + "' does not exist.");
            return "Error: Table '" + tableName + "' does not exist.";
        }
        String valuesPart = parts[1].trim().replaceAll("\\)", "");

        String[] values = valuesPart.substring(1, valuesPart.length() - 1).split(",");
        List<Object> recordValues = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim().replaceAll("'", "");
            recordValues.add(values[i]);
        }
        table.addRecord(new Record(recordValues));
        table.storeTable(tableName + ".bin");
        return "the values are inserted";
    }

    public String parseSelect(String statement) {
        String ruselt = "";
        String[] parts = statement.split("(?i)WHERE");

        String tableName = parts[0].split("(?i)FROM")[1].trim();
        tableName = tableName.replaceAll(";", "").trim();
        System.out.println(tableName);
        Table table = getTable(tableName);

        // System.out.println(table.toString());
        List<Record> selectedRecords;
        try {
            String condition = parts[1].trim();
            selectedRecords = selectRecords(table, condition);
        } catch (Exception e) {
            selectedRecords = table.getRecords();
        }

        ruselt += "Executing SELECT query on table: " + tableName + "\n";

        if (selectedRecords.size() == 0) { // when searching for a nonexistent value.
            ruselt = "there was no such value";
            return ruselt;
        }

        for (int j = 0; j < selectedRecords.size(); j++) {
            // System.out.println("Selected Record:");
            if (j == 0)
                ruselt += "Selected Record:" + "\n";

            for (int i = 0; i < table.columns.size(); i++) {
                // System.out.println(table.columns.get(i).columnName + ": " +
                // record.values.get(i));
                ruselt += table.columns.get(i).columnName + ": " + selectedRecords.get(j).values.get(i);
                if (i != table.columns.size() - 1)
                    ruselt += " || ";
            }
            if (j < selectedRecords.size() - 1)
                ruselt += "\n";
        }
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

    boolean compare(Object value1, String operator, Object value2) {
        // For simplicity, assuming value1 and value2 are strings
        String strValue1 = (String) value1;
        String strValue2 = (String) value2;
        switch (operator) {
            case "=":
                return strValue1.equals(strValue2);
            case ">":
                try {
                    return Integer.parseInt(strValue1) > Integer.parseInt(strValue2);
                } catch (NumberFormatException e) {
                    // Handle non-integer comparison
                    return strValue1.compareTo(strValue2) > 0;
                }
            case "<":
                try {
                    return Integer.parseInt(strValue1) < Integer.parseInt(strValue2);
                } catch (NumberFormatException e) {
                    // Handle non-integer comparison
                    return strValue1.compareTo(strValue2) < 0;
                }
                // Adding other comparison operators as needed
            default:
                return false;
        }
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
