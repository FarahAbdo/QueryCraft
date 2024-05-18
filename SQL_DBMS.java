package com.mycompany.sql_dbms;

public class SQL_DBMS {
    public static void main(String[] args) {
        Shell x = new Shell();

        // Test findMaxFileSize functionality for 10GB limit
        Parser2 parser = new Parser2();
        parser.parseCreateTable("TestTable",
                new String[] { "column1 VARCHAR(100)", "column2 VARCHAR(100)", "column3 VARCHAR(100)" });
        parser.findMaxFileSize("TestTable", 100 * 1024 * 1024); // 100 MB
    }
}
