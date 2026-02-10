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

- Windows or macOS
- Java Development Kit (**JDK 23**)
- Oracle Database XE
- Git
- Command-line access (PowerShell on Windows, Terminal on macOS)

No IDE is required to build or run the project.

---

## Java Installation and Configuration

This project is compiled with **Java 23** (`--release 23`).
Using any earlier Java version (e.g. Java 17) will result in build failures.

All steps below are performed **exclusively via the command line**.

---

### Windows — Java 23 Setup (Command Line)

#### 1. Install Java 23

Open **PowerShell as Administrator** and run:

```powershell
winget install --id Oracle.JDK.23 -e
```

The JDK will be installed in a directory similar to:

```
C:\Program Files\Java\jdk-23
```

---

#### 2. Set JAVA_HOME

Still in **PowerShell as Administrator**, run:

```powershell
.\setx JAVA_HOME "C:\Program Files\Java\jdk-23" /M
```

---

#### 3. Restart the terminal

Close **all** open terminals and open a new **PowerShell** window.

Verify:

```powershell
java -version
echo $Env:JAVA_HOME
```

The output must indicate **Java 23**.

---

### macOS — Java 23 Setup (Command Line)

#### 1. Install Homebrew (if not installed)

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

---

#### 2. Install Java 23

```bash
brew install --cask oracle-jdk
```

---

#### 3. Determine the Java installation path

```bash
/usr/libexec/java_home -V
```

Identify the path corresponding to **Java 23**.

---

#### 4. Set JAVA_HOME

Replace `<JAVA_PATH>` with the path obtained above:

```bash
echo 'export JAVA_HOME="<JAVA_PATH>"' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc
```

Reload the shell configuration:

```bash
source ~/.zshrc
```

Verify:

```bash
java -version
echo $JAVA_HOME
```

The output must indicate **Java 23**.

---

## Oracle Database XE Setup

Oracle Database Express Edition is the **only component that requires a browser download**.

After installation, ensure that:

- The Oracle listener is running
- The pluggable database `XEPDB1` is available

---

## Database User Configuration

Open a terminal and connect as administrator:

```bash
sqlplus / as sysdba
```

Switch to the pluggable database:

```sql
ALTER SESSION SET CONTAINER = XEPDB1;
```

Create the application user:

```sql
CREATE USER edu IDENTIFIED BY 1234;
GRANT CONNECT, RESOURCE TO edu;
ALTER USER edu QUOTA UNLIMITED ON USERS;
```

Exit SQL*Plus:

```sql
EXIT;
```

---

## Running the Application

### Windows

```powershell
git clone <repository-url>
cd Sem3-Integrative-Project
.\run.bat
```

The script will:

- Validate the Java installation
- Build the project using Maven Wrapper
- Resolve all dependencies
- Launch the application with preview features enabled

---

### macOS

```bash
git clone <repository-url>
cd Sem3-Integrative-Project
chmod +x mvnw
./mvnw clean package -DskipTests
```

The application can then be executed using the generated artefacts.

---

## Notes

- The application must be built using Java 23.
- All build steps are performed via the command line.


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