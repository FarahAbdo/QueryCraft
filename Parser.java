package com.mycompany.sql_dbms;

import java.util.Stack;

public class Parser {
    private static String queryResponse;
    Parser2 nextStage;

    public Parser() {
        nextStage = new Parser2();
    }

    public String query(String queryStatment) {
        String[] words = queryStatment.split(" ");
        if (validateStartOfQuery(queryStatment)) {
            switch (words[0].toLowerCase()) {
                case "create" -> {
                    if (words[1].equalsIgnoreCase("table")) {
                        if (validateCreateTableQuery(queryStatment)) {
                            createTable(queryStatment.substring(13));
                        } else {
                            queryResponse = "You have an error in your SQL syntax for CREATE TABLE.";
                        }
                    } else {
                        queryResponse = "You have an error in your SQL syntax; CREATE should be followed by TABLE.";
                    }
                }
                case "insert" -> {
                    if (words[1].equalsIgnoreCase("into") && validateInsertQuery(queryStatment)) {
                        queryResponse = nextStage.parseInsert(queryStatment);
                    } else {
                        queryResponse = "You have an error in your SQL syntax for INSERT.";
                    }
                }
                case "select" -> {
                    if (validateSelectQuery(queryStatment)) {
                        queryResponse = nextStage.parseSelect(queryStatment);
                    } else {
                        queryResponse = "You have an error in your SQL syntax for SELECT.";
                    }
                }
                case "delete" -> {
                    if (words[1].equalsIgnoreCase("from") && validateDeleteQuery(queryStatment)) {
                        queryResponse = nextStage.parseDelete(queryStatment);
                    } else {
                        queryResponse = "You have an error in your SQL syntax for DELETE.";
                    }
                }
                default -> {
                    queryResponse = "You have an error in your SQL syntax; valid commands are CREATE, INSERT, SELECT, DELETE.";
                    System.out.print(queryStatment);
                }
            }
        } else {
            queryResponse = "You have an error in your SQL syntax; valid commands are CREATE, INSERT, SELECT, DELETE.";
            System.out.print(queryStatment);
        }
        return queryResponse;
    }

    private static boolean validateStartOfQuery(String query) {
        String trimmedQuery = query.trim();
        return trimmedQuery.toUpperCase().matches("(?i)\\s*(CREATE TABLE|SELECT|INSERT INTO|DELETE FROM)\\s+.+");
    }

    public static boolean validateSelectQuery(String query) {
        String regex = "(?i)^SELECT\\s+(\\*|([\\w\\s,]+))\\s+FROM\\s+\\w+" + // Columns and FROM
                "(\\s+(WHERE|JOIN\\s+\\w+\\s+ON|GROUP\\s+BY|HAVING|ORDER\\s+BY)\\s+.+)*" + // Optional clauses
                "\\s*;$"; // End of query
        return query.trim().toUpperCase().matches(regex);
    }

    public static boolean validateInsertQuery(String query) {
        query = query.trim().replaceAll("\\s+", " ");

        if (!query.matches("(?i)^INSERT INTO\\s+\\w+\\s*(\\(\\s*\\w+(\\s*,\\s*\\w+)*\\s*\\))?\\s*VALUES\\s*\\(.*\\)\\s*;$")) {
            System.out.println("Invalid INSERT INTO syntax or format.");
            System.out.println(query);
            return false;
        }

        String tableName;
        String[] columns = null;
        String valuesSection;

        int startCols = query.indexOf('(');
        int endCols = query.indexOf(')');
        int startValues = query.lastIndexOf('(');
        int endValues = query.lastIndexOf(')');

        if (startCols < endValues && endCols < startValues) {
            tableName = query.substring(11, startCols).trim();
            String columnsPart = query.substring(startCols + 1, endCols).trim();
            columns = columnsPart.split(",");
            valuesSection = query.substring(startValues + 1, endValues).trim();
        } else {
            String[] part = query.split("(?i)VALUES");
            tableName = part[0].substring(11).trim();
            valuesSection = query.substring(startValues + 1, endValues).trim();
        }

        if (startValues >= endValues || startCols >= endCols) {
            System.out.println("Error: Incorrect positioning or syntax near VALUES clause.");
            return false;
        }

        System.out.println("INSERT INTO structure is valid for table: " + tableName + " with values: " + valuesSection);
        return true;
    }

    public static boolean validateCreateTableQuery(String query) {
        if (!query.trim().matches("(?i)^CREATE TABLE\\s+\\w+\\s*\\((.+)\\)\\s*;$")) {
            queryResponse = "Invalid CREATE TABLE syntax or format.";
            System.out.println(query);
            return false;
        }

        int firstParenthesisIndex = query.indexOf('(');
        int lastParenthesisIndex = query.lastIndexOf(')');
        if (firstParenthesisIndex == -1 || lastParenthesisIndex == -1 || firstParenthesisIndex >= lastParenthesisIndex) {
            queryResponse = "Error: Parentheses are missing or incorrectly placed.";
            return false;
        }

        String contentInsideParentheses = query.substring(query.indexOf('(') + 1, query.lastIndexOf(')'));

        if (!isParenthesesBalanced(contentInsideParentheses)) {
            queryResponse = "Parentheses are not balanced.";
            return false;
        }

        if (!areColumnsValid(contentInsideParentheses)) {
            queryResponse = "Column definitions are not valid.";
            return false;
        }

        return true;
    }

    public static boolean isParenthesesBalanced(String str) {
        Stack<Character> stack = new Stack<>();
        for (char ch : str.toCharArray()) {
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                if (stack.isEmpty() || stack.pop() != '(') {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static boolean areColumnsValid(String columns) {
        String[] columnList = columns.split(",");
        for (String column : columnList) {
            if (!column.trim().toUpperCase().matches("\\w+\\s+(INT|CHAR\\([1-9][0-9]?\\)|VARCHAR\\([1-9][0-9]?\\))")) {
                return false;
            }
        }
        return true;
    }

    private boolean validateDeleteQuery(String query) {
        String regex = "(?i)^DELETE\\s+FROM\\s+\\w+" + // Columns and FROM
                "(\\s+WHERE\\s+.+)*" + // Optional WHERE clause
                "\\s*;$"; // End of query
        return query.trim().toUpperCase().matches(regex);
    }

    private void createTable(String queryStatment) {
        try {
            String tableName;
            if (queryStatment.indexOf('(') < queryStatment.indexOf(' ')) {
                tableName = queryStatment.substring(0, queryStatment.indexOf('('));
                queryStatment = queryStatment.substring(queryStatment.indexOf('(') + 1);
            } else {
                tableName = queryStatment.substring(0, queryStatment.indexOf(' '));
                queryStatment = queryStatment.substring(queryStatment.indexOf(' ') + 2);
            }
            passingCreateTableParameter(tableName, queryStatment);
        } catch (Exception e) {
            queryResponse = "You have an error in your SQL syntax; " + e.toString();
        }
    }

    private void passingCreateTableParameter(String tableName, String columns) {
        columns = columns.replaceAll("\\) ;", "").replaceAll("\\);", "");

        String[] columnArray = columns.split(",");
        queryResponse = nextStage.parseCreateTable(tableName, columnArray);
    }
}
