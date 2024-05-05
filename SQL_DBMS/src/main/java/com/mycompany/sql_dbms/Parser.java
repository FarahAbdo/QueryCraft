/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sql_dbms;

import java.util.Stack;
/**
 *
 * @author Abdo humed
 */
public class Parser {
    // String queryStatment;
    private static String queryResponse;
    Parser2 nextStage;

    public String query(String queryStatment) {
        // this.queryStatment = queryStatment;

        String[] words = queryStatment.split(" ");
        if(validateStartOfQuery(queryStatment))
        if (words[0].equalsIgnoreCase("create")) {
            if (words[1].equalsIgnoreCase("table"))
                if(validateCreateTableQuery(queryStatment))
                    createTable(queryStatment.substring(13));
            else
                queryResponse = "You have an error in your SQL syntax; create should followed by table";
        } else if (words[0].equalsIgnoreCase("insert")) {
            if (words[1].equalsIgnoreCase("into")&&validateInsertQuery( queryResponse ))
                
                    nextStage.parseInsert(queryStatment);
            else
                queryResponse = "You have an error in your SQL syntax;";
        } else if (words[0].equalsIgnoreCase("select") && validateSelectQuery(queryStatment)) {
            nextStage.parseSelect(queryStatment);
        } else
            queryResponse = "You have an error in your SQL syntax;you should write create or insert or delete";
        return queryResponse;
    }
    
    private static boolean validateStartOfQuery(String query) {
        String trimmedQuery = query.trim(); // Trim whitespace for better matching
        // Regex to match "CREATE TABLE", "SELECT", or "INSERT INTO" at the start of the
        // query
        return trimmedQuery.matches("(?i)\\s*(CREATE TABLE|SELECT|INSERT INTO)\\s+.+");
    }
     public static boolean validateSelectQuery(String query) {
        String regex = "(?i)^SELECT\\s+(\\*|([\\w\\s,]+))\\s+FROM\\s+\\w+" + // Columns and FROM
                "(\\s+(WHERE|JOIN\\s+\\w+\\s+ON|GROUP\\s+BY|HAVING|ORDER\\s+BY)\\s+.+)*" + // Optional clauses
                "\\s*;$"; // End of query
        return query.trim().matches(regex);
    }

    public static boolean validateInsertQuery(String query) {
        // Using regex to match the structure of an INSERT INTO statement while allowing
        // flexible whitespace handling
        return query.trim().matches("(?i)^INSERT\\s+INTO\\s+\\w+\\s*" +
                "(\\(\\s*\\w+(\\s*,\\s*\\w+)*\\s*\\))?\\s*" +
                "VALUES\\s*\\(\\s*[^)]+\\s*\\)\\s*;$");
    }

    // Method to validate the structure of a CREATE TABLE query
    public static boolean validateCreateTableQuery(String query) {
        if (!query.trim().matches("(?i)^CREATE TABLE\\s+\\w+\\s*\\((.+)\\)\\s*;$")) {
            queryResponse ="Invalid CREATE TABLE syntax or format.";
            return false;
        }

        // Ensuring that the parentheses actually exist around column definitions
        int firstParenthesisIndex = query.indexOf('(');
        int lastParenthesisIndex = query.lastIndexOf(')');
        if (firstParenthesisIndex == -1 || lastParenthesisIndex == -1
                || firstParenthesisIndex >= lastParenthesisIndex) {
            queryResponse ="Error: Parentheses are missing or incorrectly placed.";
            return false;
        }

        // Extracting the contents inside the parentheses
        String contentInsideParentheses = query.substring(query.indexOf('(') + 1, query.lastIndexOf(')'));

        if (!isParenthesesBalanced(contentInsideParentheses)) {
            queryResponse ="Parentheses are not balanced.";
            return false;
        }

        if (!areColumnsValid(contentInsideParentheses)) {
            queryResponse ="Column definitions are not valid.";
            return false;
        }

       // System.out.println("CREATE TABLE structure is valid.");
        return true;
    }

    // Check for balanced parentheses
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
            // Trim each column definition and validate its format
            if (!column.trim().matches("\\w+\\s+(INT|CHAR\\([1-9][0-9]?\\)|VARCHAR\\([1-9][0-9]?\\))")) {
                return false; // Validate that each column consists of a name followed by a valid type with
                              // constraints as specified
            }
        }
        return true;
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
            // queryResponse = "the table created whith name " + tableName;
            passingCreateTableParameter(tableName, queryStatment);
        } catch (Exception e) {
            queryResponse = "You have an error in your SQL syntax;" + e.toString();
        }

    }

    private void passingCreateTableParameter(String tableName, String cloloms) {
        // cloloms = cloloms.replace('(', ' ');
        // cloloms = cloloms.replace(')', ' ');
        cloloms = cloloms.replaceAll("\\) ;", "");
        cloloms = cloloms.replaceAll("\\);", "");

        String[] colom = cloloms.split(",");
        
        queryResponse = nextStage.parseCreateTable(tableName, colom);
    }

    private void insert(String queryStatment) {
        // try {
        // string tableName;
        // if (queryStatment.indexOf('(') < queryStatment.indexOf(' ')) {
        // tableName = queryStatment.substring(0, queryStatment.indexOf('('));
        // queryStatment = queryStatment.substring(queryStatment.indexOf('('));
        // } else {
        // tableName = queryStatment.substring(0, queryStatment.indexOf(' '));
        // queryStatment = queryStatment.substring(queryStatment.indexOf(' ') + 1);
        // }
        // queryResponse = "the table created whith name" +
        // queryStatment.substring(index);
        // } catch (Exception e) {
        // queryResponse = "You have an error in your SQL syntax;";
        // }

    }

    private void select(String queryStatment) {
        // try {
        // string tableName;
        // if (queryStatment.indexOf('(') < queryStatment.indexOf(' ')) {
        // tableName = queryStatment.substring(0, queryStatment.indexOf('('));
        // queryStatment = queryStatment.substring(queryStatment.indexOf('('));
        // } else {
        // tableName = queryStatment.substring(0, queryStatment.indexOf(' '));
        // queryStatment = queryStatment.substring(queryStatment.indexOf(' ') + 1);
        // }
        // queryResponse = "the table created whith name" +
        // queryStatment.substring(index);
        // } catch (Exception e) {
        // queryResponse = "You have an error in your SQL syntax;";
        // }

    }
}
