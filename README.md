# QueryCraft DBMS Project

## Table of Contents

1. [Introduction](#introduction)
2. [Directory Structure](#directory-structure)
3. [Documentation](#documentation)
4. [Getting Started](#getting-started)
5. [Database](#database)
6. [Build and Deployment](#build-and-deployment)
7. [Contributing](#contributing)
8. [License](#license)

## Introduction

The QueryCraft DBMS project aims to develop a high-performance database management system that utilizes binary files for efficient data storage. Designed to support robust metadata management, SQL parsing, and user interaction through a command-line interface, this project covers the entire system including source code and documentation.

## Directory Structure

```vbnet
QueryCraft/
├── docs/
│   ├── README.md              ' Overview and instructions for the project
│   └── API_Docs/              ' Documentation related to the database operations
├── src/
│   ├── main/java/com/mycompany/sql_dbms/
│   │   ├── Classes.java
│   │   ├── Parser.java
│   │   ├── SQL_DBMS.java
│   │   ├── Shell.form
│   │   └── Shell.java
│   └── binary/
│       └── BinaryHandler.java ' Binary file operations code
├── target/
│   ├── classes/com/mycompany/sql_dbms/
│   │   ├── Column.class
│   │   ├── Parser.class
│   │   ├── Parser2.class
│   │   ├── Record.class
│   │   ├── SQL_DBMS.class
│   │   ├── Shell$1.class
│   │   ├── Shell$2.class
│   │   ├── Shell.class
│   │   └── Table.class
├── .gitignore                 ' Specifies files and directories to be ignored by version control
├── LICENSE.md                 ' Licensing information
├── README.md                  ' Overview and instructions for the project
└── CONTRIBUTING.md
``` 

## Documentation

Documentation for the QueryCraft DBMS project is located in the `docs/` directory. This includes a detailed overview of the project, installation instructions, usage guides, and API documentation.

## Getting Started

To get started with the QueryCraft DBMS project, refer to the `README.md` for installation instructions and how to run the project.

## Database

Details on the database schema, configuration, and how to seed the database can be found in the source code and binary files within the `src/` and `target/` directories.

## Build and Deployment

To build and deploy the QueryCraft DBMS project, follow these step-by-step instructions:

### Prerequisites

Ensure you have the following installed on your system:
- Java Development Kit (JDK) 8 or higher
- Maven

### Steps

1. **Clone the Repository**

Clone the QueryCraft repository from GitHub:
```bash
git clone https://github.com/FarahAbdo/QueryCraft.git
```
2. **Navigate to the Project DirectoryChange your working directory to the cloned repository**
```bash
cd QueryCraft
```

## Contributing

Contributions to the QueryCraft DBMS project are welcome! Please refer to the `CONTRIBUTING.md` file for guidelines on how to make contributions.

## License

This project is licensed under the MIT License - see the `LICENSE` file for details.

