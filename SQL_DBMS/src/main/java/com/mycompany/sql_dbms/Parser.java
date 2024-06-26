/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sql_dbms;

import java.util.Stack;
//import java.util.logging.Level;
//import java.util.logging.Logger;
/**
 *
 * @author Abdo humed
 */
public class Parser {
    // String queryStatment;
    private static String queryResponse;
    Parser2 nextStage;
    public Parser(){
    nextStage = new Parser2(); 
    }
    
     

    public String query(String queryStatment) {
        // this.queryStatment = queryStatment;

        String[] words = queryStatment.split(" ");
        if(validateStartOfQuery(queryStatment)){
        if (words[0].equalsIgnoreCase("create")) {
            System.out.println(words[1]);
            if (words[1].equalsIgnoreCase("table")){
                if(validateCreateTableQuery(queryStatment))
                    createTable(queryStatment.substring(13));}
            else
                queryResponse = "You have an error in your SQL syntax; create should followed by table";
        }
        else if (words[0].equalsIgnoreCase("insert")) {
            if (words[1].equalsIgnoreCase("into")&&validateInsertQuery( queryStatment ))

                   queryResponse = nextStage.parseInsert(queryStatment);
            else
                queryResponse = "You have an error in your SQL syntax;";
        }
        else if (words[0].equalsIgnoreCase("select") && validateSelectQuery(queryStatment)) {
           queryResponse = nextStage.parseSelect(queryStatment);
        }
        else if (words[0].equalsIgnoreCase("delete")) {
            if (words[1].equalsIgnoreCase("from")&&validatedeleteQuery( queryStatment ))
                queryResponse = nextStage.parseDelete(queryStatment);
            else
                queryResponse = "You have an error in your SQL syntax;";
        }
        else{
            queryResponse = "You have an error in your SQL syntax;you should write create or insert or delete";
            System.out.print(queryStatment);}
        }
        else{
            queryResponse = "You have an error in your SQL syntax;you should write create or insert or delete";
            System.out.print(queryStatment);}
       
       
        return queryResponse;
    }
    
    private static boolean validateStartOfQuery(String query) {
        String trimmedQuery = query.trim(); // Trim whitespace for better matching
        // Regex to match "CREATE TABLE", "SELECT", or "INSERT INTO" at the start of the
        // query
        return trimmedQuery.toUpperCase().matches("(?i)\\s*(CREATE TABLE|SELECT|INSERT INTO|DELETE FROM)\\s+.+");
    }
     public static boolean validateSelectQuery(String query) {
        String regex = "(?i)^SELECT\\s+(\\*|([\\w\\s,]+))\\s+FROM\\s+\\w+" + // Columns and FROM
                "(\\s+(WHERE|JOIN\\s+\\w+\\s+ON|GROUP\\s+BY|HAVING|ORDER\\s+BY)\\s+.+)*" + // Optional clauses
                "\\s*;$"; // End of query
        return query.trim().toUpperCase().matches(regex);
    }

public static boolean validateInsertQuery(String query) {
        // Normalize the query by trimming and removing excessive internal spaces for easier processing
        query = query.trim().replaceAll("\\s+", " ");

        // Basic syntax check
        if (!query.matches("(?i)^INSERT INTO\\s+\\w+\\s*(\\(\\s*\\w+(\\s*,\\s*\\w+)*\\s*\\))?\\s*VALUES\\s*\\(.*\\)\\s*;$")) {
            System.out.println("Invalid INSERT INTO syntax or format.");
             System.out.println(query);
            return false;
        }

        // Extracting details if the syntax is correct
        String tableName;
        String[] columns = null;
        String valuesSection;

        // Determine if column names are provided
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
            String [] part= query.split("(?i)VALUES");
            tableName = part[0].substring(11).trim();
            valuesSection = query.substring(startValues + 1, endValues).trim();
        }

        // Check for logical errors in substring indices
        if (startValues >= endValues || startCols >= endCols) {
            System.out.println("Error: Incorrect positioning or syntax near VALUES clause.");
            return false;
        }

        // Optional: Validate column names against a schema (not implemented here)
        // Optional: Validate values according to column types (not implemented here)

        System.out.println("INSERT INTO structure is valid for table: " + tableName + " with values: " + valuesSection);
        return true;
    }

    // Method to validate the structure of a CREATE TABLE query
    public static boolean validateCreateTableQuery(String query) {
        if (!query.trim().matches("(?i)^CREATE TABLE\\s+\\w+\\s*\\((.+)\\)\\s*;$")) {
            queryResponse ="Invalid CREATE TABLE syntax or format.";
                    System.out.println(query );

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
            if (!column.trim().toUpperCase().matches("\\w+\\s+(INT|CHAR\\([1-9][0-9]?\\)|VARCHAR\\([1-9][0-9]?\\))")) {
                return false; // Validate that each column consists of a name followed by a valid type with
                              // constraints as specified
            }
        }
        return true;
    }
    private boolean validatedeleteQuery(String query) {
        String regex = "(?i)^delete\\s+FROM\\s+\\w+" + // Columns and FROM
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

  
}
