import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistence.CBORSerializer
import persistence.JSONSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}


// Persistence formats
//private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
//private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))
private val noteAPI = NoteAPI(CBORSerializer(File("notes.cbor")))



fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
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
         > ==>> """.trimMargin(">"))
}

fun subMenu() : Int {
    return ScannerInput.readNextInt(""" 
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
         > ==>> """.trimMargin(">"))
}

fun addNote(){
    //logger.info { "addNote() function invoked" }
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    val noteTimeStamp = LocalDateTime.now().format(formatter)

    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false, noteTimeStamp))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}


fun listNotes(){
    //logger.info{ "listNotes() function invoked"}
    println(noteAPI.listAllNotes())
}

fun updateNote() {
    //logger.info { "updateNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")

            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            val noteTimeStamp = LocalDateTime.now().format(formatter)

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false, noteTimeStamp))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun SearchNotes() {
    val searchTitle = readNextLine("Enter the title to search by: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if(searchTitle.isEmpty()){
        println("No notes found")
    } else {
        println(searchResults)
    }
}


fun deleteNote(){
    //logger.info { "deleteNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}


fun exitApp(){

}


fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addNote()
            2  -> listNotesSubmenu()
            3  -> updateNote()
            4  -> deleteNote()
            5  -> archiveNote()
            6 -> SearchNotes()
            0  -> exitApp()
            20 -> save()
            21 -> load()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

/*
SUB-MENU for listing notes
3 options:
Listing all notes that are saved within the system
Listing only active notes
Listing archived notes
 */
fun listNotesSubmenu(){
    do {
        val option = subMenu()
        when (option) {
            1 -> listNotes()
            2 -> println(noteAPI.listActiveNotes())
            3 -> println(noteAPI.listArchivedNotes())
            4 -> println(noteAPI.listNotesBySelectedPriority(readNextInt("Please Enter a Note Priority to List: ")))
            5 -> println(noteAPI.listNotesByMonth(readNextLine("Please enter a month to search notes, example 'march': ")))
            6 -> println(noteAPI.listNotesByYear(readNextInt("Please enter a year to search notes, example '2023': ")))
            7 -> listNotesByDate()
            0 -> runMenu()
            else -> println("Invalid option entered: ${option}")
        }
    } while(true)
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun listNotesByDate(){
    val notes = noteAPI.sortNoteByDate()
    println(notes)
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

/*
Archive a note by index specified by user
 */
fun archiveNote(){
    // only list active notes, as we don't need to archive notes that have been done so already
    println(noteAPI.listActiveNotes())
    if(noteAPI.numberOfActiveNotes() > 0){
        // The index of the note to be archived
        var indexToArchive: Int = readNextInt("Please enter an index of the note: ")
        if(noteAPI.isValidIndex(indexToArchive)){
            noteAPI.archiveNoteByIndex(indexToArchive)
            println("The note has successfully been archived")
        } else {
            println("Index of note is invalid")
        }
    } else {
        println("There is no active notes")
    }
}