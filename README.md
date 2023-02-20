# Note Application
[![Development](https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?style=for-the-badge&logo=IntelliJ-IDEA&logoColor=white)](https://www.jetbrains.com/idea/)
[![Language](https://img.shields.io/badge/Kotlin-7F52FF.svg?style=for-the-badge&logo=Kotlin&logoColor=white)](https://kotlinlang.org/)

This application was built using Intellij and the Kotlin programming language.

## Description

As part of our Software Development Tools we were tasked with building a note taking application with Kotlin. This included CRUD functionality.


## Getting Started

### Dependencies

* kotlin-logging (2.1.23)
* slf4j-simple (1.7.36)
* JUnit 5 (5.8.1)

### Installing

* Clone repo from Intellij
* Build within Intellij

### Executing program

* After the program has been built successfully
* Click the run button within intellij

## Basic menu for the application.
```
 |        NOTE KEEPER APP         |
 ----------------------------------
 | NOTE MENU                      |
 |   1) Add a note                |
 |   2) List all notes            |
 |   3) Update a note             |
 |   4) Delete a note             |
 ----------------------------------
 |   0) Exit                      |
 ----------------------------------
 ==>> 

```



Any advice for common problems or issues.
```
command to run if program contains helper info
```

## Authors

Contributors names and contact info

Josh Crotty
[@Zaradin](https://github.com/Zaradin)

## Version History
* 2.0
  * **Archived and Non-Archived Notes functionality implemented**
    * listActiveNotes(): String
    * listArchivedNotes(): String
    * numberOfArchivedNotes(): Int
    * numberOfActiveNotes(): Int
  * **List Notes by Note priority level (1-5)**
    * listNotesBySelectedPriority(priority: Int): String
    * numberOfNotesByPriority(priority: Int): Int
  * **JUnit Tests passing for each of the implemented functions for current release**
* 1.0
  * Menu items for Adding, Listing, Updating and Deleting a Note. No Note model is added yet; the menu structure is a skeleton.
  * Logging capabilities are added via MicroUtils Kotlin-Logging.
  * ScannerInput utility is included for rebust user I/O.
