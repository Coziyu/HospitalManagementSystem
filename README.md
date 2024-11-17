<p align="center">
    <img src="https://raw.githubusercontent.com/PKief/vscode-material-icon-theme/main/logo.png" align="center" width="30%">
</p>
<p align="center"><h1 align="center"> SC2002 HOSPITAL MANAGEMENT SYSTEM</h1></p>
<p align="center">
	<em><code>> SCSI Group 3 < </code></em>
</p>
<p align="center">
	<img src="https://img.shields.io/github/last-commit/Coziyu/HospitalManagementSystem?style=default&logo=git&logoColor=white&color=0080ff" alt="last-commit">
	<img src="https://img.shields.io/github/languages/top/Coziyu/HospitalManagementSystem?style=default&color=0080ff" alt="repo-top-language">
	<img src="https://img.shields.io/github/languages/count/Coziyu/HospitalManagementSystem?style=default&color=0080ff" alt="repo-language-count">
</p>
<p align="center"><!-- default option, no dependency badges. -->
</p>
<p align="center">
	<!-- default option, no dependency badges. -->
</p>
<br>

##  Table of Contents

- [ Overview](#overview)
- [ Features](#features)
- [ Project Structure](#project-structure)
    - [ Project Index](#project-index)
- [ Getting Started](#-getting-started)
    - [ Prerequisites](#-prerequisites)
    - [ Installation](#-installation)
    - [ Usage](#-usage)
    - [ Testing](#-testing)
- [ Contributing](#contributing)

---

##  Overview
This project was done as part of a graded assignment in our SC2002 Object-Oriented Programming Module. Hospital Management System or HMS for short, is a Java Object-Oriented application that is designed to automate the management of hospital operations like patient or staff management, appointment scheduling and drug inventory management.

---

##  Features

- Authentication Service
  - Handles user authentication and validation for role-based access control
- Staff Management Service
  - Managing manpower related actions like adding, removing, archiving of staff details and assignment of role.
- Patient Management Service
  - Medical Record Service
   -Responsible for storing and retrieving patient medical histories + details, also allows patients and doctors to update role-specific details
  - Appointment Service
   - Managing appointment scheduling and outcomes such as doctor schedule management and time slot allocation
- Drug Dispensary Service
  - Manages drug inventory and stock levels as well as requests to dispense and replenish 


---

##  Project Structure

```sh
└── HospitalManagementSystem/
    ├── .github
    │   └── workflows
    ├── README.md
    ├── build.gradle.kts
    ├── data
    │   ├── Appointment
    │   ├── PatientList.csv
    │   ├── contact_information.csv
    │   ├── drugInventory.csv
    │   ├── drugReplenishRequests.csv
    │   ├── medical_records.csv
    │   ├── staff.csv
    │   └── users.csv
    ├── gradle
    │   └── wrapper
    ├── gradlew
    ├── gradlew.bat
    ├── settings.gradle.kts
    └── src
        ├── main
        └── test
```
###  Project Index

 Folder Name 	      | Purpose |
| ------------- | ------------- |
| HospitalManagementSystem | HMS Application source code, data files and libraries |
| UMLClassDiag  | Class Diagram of our HMS Application  |
| documents  | Report and javadocs of our HMS Application  |

<b>TODO PUT THE JAVADOC LINK</b>

---
##  Getting Started

###  Prerequisites

Before getting started with HospitalManagementSystem, ensure your runtime environment meets the following requirements:

- **Programming Language:** Java
- **Package Manager:** Gradle


###  Installation
**Build from source:**

1. Clone the HospitalManagementSystem repository:
```sh
❯ git clone https://github.com/Coziyu/HospitalManagementSystem
```

2. Navigate to the project directory:
```sh
❯ cd HospitalManagementSystem
```

3. Install the project dependencies:


**Using `gradle`** &nbsp; [<img align="center" src="https://img.shields.io/badge/Gradle-02303A.svg?style={badge_style}&logo=gradle&logoColor=white" />](https://gradle.org/)

```sh
❯ gradle build
```




###  Usage
Run HospitalManagementSystem using the following command:
**Using `gradle`** &nbsp; [<img align="center" src="https://img.shields.io/badge/Gradle-02303A.svg?style={badge_style}&logo=gradle&logoColor=white" />](https://gradle.org/)

```sh
❯ java -classpath java -classpath build\classes\java\main org.hms.Main
```



---

##  Contributing

<details closed>
<summary>Contributing Guidelines</summary>

1. **Fork the Repository**: Start by forking the project repository to your GitHub account.
2. **Clone Locally**: Clone the forked repository to your local machine using a git client.
   ```sh
   git clone https://github.com/Coziyu/HospitalManagementSystem
   ```
3. **Create a New Branch**: Always work on a new branch, giving it a descriptive name.
   ```sh
   git checkout -b new-feature-x
   ```
4. **Make Your Changes**: Develop and test your changes locally.
5. **Commit Your Changes**: Commit with a clear message describing your updates.
   ```sh
   git commit -m 'Implemented new feature x.'
   ```
6. **Push to GitHub**: Push the changes to your forked repository.
   ```sh
   git push origin new-feature-x
   ```
7. **Submit a Pull Request**: Create a PR against the original project repository. Clearly describe the changes and their motivations.
8. **Review**: Once your PR is reviewed and approved, it will be merged into the main branch. Congratulations on your contribution!
</details>


# Contributors 
<br>
<p align="left">
   <a href="https://github.com{/Coziyu/HospitalManagementSystem/}graphs/contributors">
      <img src="https://contrib.rocks/image?repo=Coziyu/HospitalManagementSystem">
   </a>
</p>
Lab Group : SCSI
Group 3

 Name 	      | Github Username |
| ------------- | ------------- |
| Chong Zhi Yu  | Coziyu  |
| Tang Yingjie  | Tyingjie  |
| Amos Ang Chee Tiong  | mosc0de  |
| Ng Yuhang Dilon  | dillydecoded  |
| Yeo Elijah Xuan-Ye  | Tricktail  |

---
