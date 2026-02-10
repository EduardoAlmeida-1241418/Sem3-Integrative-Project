# Sem3-Integrative-Project — Logistics On Rails

This repository contains the development of the **Integrative Project for the 3rd semester of the 2025/26 academic year**.
The project focuses on the design and implementation of an **IT solution to support logistics and railway network operations**, combining algorithmic problem solving, database persistence, and structured software design.

The project is developed within the scope of the following courses:

- **ARQCP** – Computer Architectures
- **BDDAD** – Databases
- **ESINF** – Information Structures
- **FSIAP** – Applied Physics
- **LAPR3** – Laboratory Project III

The development follows an **iterative and incremental SCRUM-based approach**, in accordance with the official project specification.

---

## Project Description

The system models and manages logistics operations over railway networks.
It supports the representation and manipulation of infrastructures, stations, freight, routes, and datasets, while providing algorithmic solutions for spatial searches and network optimisation.

The application is primarily **console-based**, with a strong emphasis on:

- Correctness and robustness
- Efficient data structures (graphs, trees, spatial indexes)
- Database-backed persistence
- Modular and maintainable architecture

---

## Project Structure

The repository is organised as follows:

```
 Sem3-Integrative-Project/
 │
 ├─ ARQCP/                     # Artefacts and materials related to ARQCP
 │
 ├─ data/                      # Application data files
 │  └─ userData.csv            # User-related or input data
 │
 ├─ dataset/                   # Datasets used during development and testing
 │  ├─ sprint1/                # Dataset for Sprint 1
 │  ├─ sprint2/                # Dataset for Sprint 2
 │  └─ sprint3/                # Dataset for Sprint 3
 │
 ├─ doc/                       # Project documentation
 │  ├─ auth/                   # Authentication-related documentation
 │  ├─ global-artifacts/       # Global project artefacts
 │  ├─ interface-design/       # Interface and interaction design
 │  └─ USXX/                   # User stories and supporting documents
 │
 ├─ output/                    # Generated outputs and results
 │
 ├─ sql-scripts/               # SQL and PL/SQL scripts
 │
 ├─ src/
 │  ├─ main/
 │  │  ├─ java/                # Main application source code
 │  │  │  └─ pt/ipp/isep/dei   # Core project packages
 │  │  └─ resources/           # Application resources
 │  └─ test/                   # Automated tests
 │
 ├─ target/                    # Compiled artefacts (generated)
 ├─ run.bat                    # Execution script (Windows)
 ├─ pom.xml                    # Maven configuration
 ├─ LICENSE                    # Project license
 └─ README.md
```

---

## Requirements

- Windows operating system
- Java 23 (with preview features enabled)
- Oracle Database XE

---

## Database Configuration

The application requires an **Oracle Database XE** instance.

### Prerequisites

- Oracle Database XE installed and running
- A database user created

### Example Database Setup

```sql
 CREATE USER sem3pi IDENTIFIED BY pwd;
 GRANT CONNECT, RESOURCE TO sem3pi;
 ```

The Oracle service name can be confirmed using:

```sql
 SELECT name FROM v$services;
 ```

In this project, the service name typically used is:

```
 XEPDB1
 ```

---

## Execution Configuration ([`run.bat`](run.bat))

To execute the application, the database connection and Java environment must be configured in the `run.bat` file located at the root of the project.

### What to Configure in [`run.bat`](run.bat)

Open the [`run.bat`](run.bat) file and adjust the following variables if necessary:

```bat
 set DB_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1
 set DB_USER=sem3pi
 set DB_PASSWORD=pwd
 ```

- **DB_URL** – JDBC connection string pointing to the Oracle service name
- **DB_USER** – Oracle database username
- **DB_PASSWORD** – Oracle database password

Ensure that the Java executable path matches your local installation.
The application requires **Java 23 with preview features enabled**.

```bat
 "C:\Program Files\Java\jdk-23\bin\java"
```

---

## How to Run the Application (Windows)

After the application has been correctly configured, run:

```powershell
 .\run.bat
```

The execution script will:

- Launch the application using Java 23
- Enable preview features
- Load database configuration
- Establish a connection to Oracle Database XE

---

# Credits
This project was developed with the collaboration of the following team:
- [André Pinho](https://github.com/AndreSiPinho)
- [Bianca Almeida](https://github.com/Brma505)
- [Carlota Lemos](https://github.com/carlotalemos)
- [Eduardo Almeida](https://github.com/EduardoAlmeida-1241418)
- [Marcelo Carvalho](https://github.com/CarvalhoISEP)

---

## Licence

This project is licensed under the MIT Licence.

---

## Notes

- This project requires **Java preview features**.
- The Oracle service name must match the local database configuration.
- The application should be executed using the provided script to ensure correct setup.