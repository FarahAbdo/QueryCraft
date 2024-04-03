# QueryCraft DBMS Project

## Table of Contents
- [Introduction](#introduction)
- [Directory Structure](#directory-structure)
- [Documentation](#documentation)
- [Getting Started](#getting-started)
- [Database](#database)
- [Build and Deployment](#build-and-deployment)
- [Contributing](#contributing)
- [License](#license)

## Introduction
The QueryCraft DBMS project aims to develop a high-performance database management system that utilizes binary files for efficient data storage. Designed to support robust metadata management, SQL parsing, and user interaction through a command-line interface, this project covers the entire system including source code and documentation.

## Directory Structure
- `docs/`: Contains project documentation.
  - `README.md`: Overview and instructions for the project.
  - `API_Docs/`: Documentation related to the database operations.

- `src/`: Source code for the project.
  - `parser/`: Implementation of the parser.
    - `JavaCC/`: JavaCC files and configurations.
    - `SQLParser.java`: SQL parser code.
  - `metadata/`: Metadata handling.
    - `Metadata.java`: Metadata structure and handling.
  - `binary/`: Binary file operations.
    - `BinaryHandler.java`: Binary file operations code.
  - `cli/`: Command line interface code.
    - `CommandLineInterface.java`: CLI implementation.

  - `tests/`: Test code and resources.
    - `ParserTests.java`: Tests for the parser functionality.

- `database/`: Database-related files (if applicable).

- `build.gradle`: Build and configuration file (if using Gradle).
- `.gitignore`: Specifies files and directories to be ignored by version control.
- `LICENSE`: Licensing information.
- `deployment/`: Release and deployment scripts (if applicable).

## Documentation
Documentation for the QueryCraft DBMS project is located in the `docs/` directory. This includes a detailed overview of the project, installation instructions, usage guides, and API documentation.

## Getting Started
To get started with the QueryCraft DBMS project, refer to the `README.md` in the `docs/` directory for installation instructions and how to run the project.

## Database
Details on the database schema, configuration, and how to seed the database can be found under the `database/` directory (if applicable).

## Build and Deployment
Instructions for building and deploying the QueryCraft DBMS project can be found in the `build.gradle` file for Gradle projects, or consult the `deployment/` directory for custom deployment scripts.

## Contributing
Contributions to the QueryCraft DBMS project are welcome! Please refer to the `CONTRIBUTING.md` file for guidelines on how to make contributions (note: create this file if it doesn't exist).

## License
This project is licensed under the MIT License - see the `LICENSE` file for details.

