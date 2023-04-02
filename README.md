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

## Main menu for the application.
```
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List notes                |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > |   5) Archive a note            |
         > |   6) Search Notes              |
         > |   20) Save notes               |
         > |   21) Load notes               |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>>  
```

## List Notes Sub-Menu
```
         > ----------------------------------
         > |        LIST NOTES MENU         |
         > ----------------------------------
         > | LIST NOTE SUB-MENU             |
         > |   1) List all notes            |
         > |   2) List active notes         |
         > |   3) List archived notes       |
         > |   4) List notes by Priority    |
         > |--------------------------------|
         > |        Extra Features          |
         > |   5) List notes by month       |
         > |   6) List notes by year        |
         > |   7) List notes by newest date |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> 
```


Any advice for common problems or issues.
```
command to run if program contains helper info
```

## Authors

Contributors names and contact info

Mubarak Al
[@Mubarak09](https://github.com/mubarak09)

## Version History
* 4.0
  * New timestamps for all new notes that are created with the format "dd-MM-yyyy HH:mm".
  * CBOR persist functionality whereby notes can now be saved as Concise Binary Object Representation. 
  * Minor bug fix 
    * fixed minor bug with the sub-menu for listing notes, when trying to exit to the main menu, the main menu doesn't take user input and
      directs it back to the sub-menu even after trying to exit, (infinite loop).
  * Refactoring
    * Counting functions to utilize lamdas 
    * Listing functions to utilize lamdas 
    * Searching functions to utilize lamdas
  * New listing functions
    * List Notes by Date (Newest to Oldest)
    * List Notes by Month
    * List Notes by Year
* 3.0
  * Delete Notes functionality implemented
    * deleteNote()
  * Updating Notes
    * updateNote()
  * Persistence with JSON & XML
    * Used serializer for (JSON & XML) that inherits different implementations
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
