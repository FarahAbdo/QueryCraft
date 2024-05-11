# QueryCraft DBMS Project

## Table of Contents

1. [Introduction](#introduction)
2. [Project Objectives](#project-objectives)
3. [Technical Overview](#technical-overview)
4. [Directory Structure](#directory-structure)
5. [Features](#features)
6. [Installation and Setup](#installation-and-setup)
7. [Usage Guide](#usage-guide)
8. [Development Process](#development-process)
9. [Testing](#testing)
10. [Future Enhancements](#future-enhancements)
11. [Contributing](#contributing)
12. [License](#license)
13. [Appendix](#appendix)

## Introduction

The QueryCraft DBMS project aims to develop a high-performance database management system that utilizes binary files for efficient data storage. Designed to support robust metadata management, SQL parsing, and user interaction through a command-line interface, this project covers the entire system including source code and documentation.

## Project Objectives

- **Efficiency**: Utilize binary files for fast and efficient data storage.
- **Usability**: Provide a user-friendly command-line interface for database operations.
- **Scalability**: Design the system to handle large datasets and complex queries.
- **Robustness**: Ensure the system is resilient and can handle errors gracefully.

## Technical Overview

### Technologies Used

- **Programming Language**: Java
- **Build Tool**: Maven
- **Database Storage**: Binary files
- **Interface**: Command-Line Interface (CLI)

### Key Components

- **SQL Parser**: Parses and validates SQL commands.
- **Binary Handler**: Manages binary data operations.
- **Metadata Manager**: Handles metadata for database tables and columns.
- **CLI**: Facilitates user interaction with the DBMS.


## Features

- **SQL Parsing and Execution**
  - Supports `CREATE`, `INSERT`, `SELECT`, `UPDATE`, and `DELETE` operations.
- **Metadata Management**
  - Manages information about tables and columns.
- **Binary Data Storage**
  - Efficiently stores data in binary format.
- **Interactive CLI**
  - Provides an easy-to-use interface for executing SQL commands.

## Installation and Setup

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven

### Steps to Setup

1. **Clone the Repository**
```bash
git clone https://github.com/FarahAbdo/QueryCraft.git
```
```bash
cd QueryCraft
```

### Example Commands
Create a Table
```bash
CREATE TABLE students (id INT, name VARCHAR(100), age INT);
```
Insert Data
```bash
INSERT INTO students VALUES (1, 'John Doe', 22);
```
Select Data
```bash
SELECT * FROM students;
```

### Development Process
#### Version Control
- Repository: Hosted on GitHub
- Branching Strategy: Feature branching
  
#### Contribution Guidelines
- Fork the repository.
- Create a new branch for each feature or bug fix.
- Follow coding standards and document your code.
- Submit a pull request for review.
  
#### Testing
- Test Strategy
- Unit Tests: Validate individual components.
- Integration Tests: Ensure components work together as expected.


# Future Enhancements

- **Advanced Query Optimization**
- **Support for Additional Data Types**
- **Improved Error Handling and Logging**
- **GUI Interface**

# Contributing

Thank you for considering contributing to QueryCraft DBMS! We welcome contributions from the community to help improve the project. Please follow the guidelines below to ensure a smooth contribution process.

## How to Contribute

1. **Fork the Repository**
   - Fork the repository to your own GitHub account. This will allow you to make changes without affecting the original project.

2. **Clone the Repository**
   - Clone the forked repository to your local machine:
     ```bash
     git clone https://github.com/your-username/QueryCraft.git
     ```

3. **Create a New Branch**
   - Create a new branch for your feature or bug fix:
     ```bash
     git checkout -b feature/your-feature-name
     ```

4. **Make Changes**
   - Make the necessary changes to the codebase. Ensure your code adheres to the project’s coding standards and conventions.

5. **Commit Changes**
   - Commit your changes with a clear and concise commit message:
     ```bash
     git commit -m "Add detailed description of your changes"
     ```

6. **Push to Your Fork**
   - Push the changes to your forked repository:
     ```bash
     git push origin feature/your-feature-name
     ```

7. **Create a Pull Request**
   - Create a pull request from your forked repository to the main repository. Provide a detailed description of your changes and any related issues.

## Code Review Process

- **Review**: All submitted pull requests will be reviewed by project maintainers.
- **Feedback**: You may be asked to make changes based on feedback.
- **Merge**: Once approved, your changes will be merged into the main branch.

## Coding Standards

- **Code Style**: Follow the existing code style and formatting.
- **Documentation**: Ensure your code is well-documented.
- **Testing**: Add tests for any new features or bug fixes.

## Reporting Issues

If you find a bug or have a feature request, please open an issue in the GitHub repository with a clear description of the problem or suggestion.

## Community Guidelines

- **Be Respectful**: Treat others with respect and professionalism.
- **Be Collaborative**: Help others and share knowledge.
- **Be Patient**: Understand that everyone is volunteering their time.

## Getting Help

If you need help with your contribution or have any questions, feel free to open an issue or reach out to the project maintainers.

# License

This project is licensed under the MIT License - see the `LICENSE.md` file for details.

## Appendix

### Useful Links

- **Project Repository**: [QueryCraft on GitHub](https://github.com/FarahAbdo/QueryCraft)
- **Documentation**: Available in the `docs/` directory
