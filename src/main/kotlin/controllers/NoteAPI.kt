package controllers

import models.Note
import persistence.Serializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter




class NoteAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType

    // ArrayList for notes
    private var notes = ArrayList<Note>()

    // Helper function for formatting notes
    private fun formatListString(notesToFormat : List<Note>) : String =
        notesToFormat
            .joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString() }


    // Load notes
    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    // Store notes
    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }


    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun listAllNotes(): String =
        if (notes.isEmpty()) "No notes stored"
        else formatListString(notes)

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }
    // method to check if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    /*
    function that will return true if the index passed to it is valid in the notes collection
     */
    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }

    /*
    function for listing all the active notes

    * First check if the note arraylist is empty or not
    * Then loop through the ArrayList also checking the isNoteArchived variable
    * If isNoteArchived is false then add that note to the list that will be returned.
    * */
    fun listActiveNotes(): String =
        if(numberOfActiveNotes() == 0) "No active notes stored"
            else formatListString(notes.filter { note -> !note.isNoteArchived })

    // method for listing all the Archived notes
    /*
    * First check if the note arraylist is empty or not; if so return No notes stored
    * Then loop through the ArrayList also checking the isNoteArchived variable
    * If isNoteArchived is true then add that note to the list that will be returned.
    * */
    fun listArchivedNotes(): String =
        if(numberOfArchivedNotes() == 0) "No archived notes stored"
         else formatListString(notes.filter { note -> note.isNoteArchived })

    fun numberOfArchivedNotes(): Int = notes.count {note : Note -> note.isNoteArchived}

    fun numberOfActiveNotes(): Int = notes.count{ note: Note -> !note.isNoteArchived}


    fun searchByTitle(searchString: String) =
        formatListString(notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) })

    fun listNotesBySelectedPriority(priority: Int): String =
        if (notes.isEmpty()) "No notes stored"
            else {
                val listOfNotes = formatListString(notes.filter { note -> note.notePriority == priority })
                if (listOfNotes.equals("")) "No notes stored with priority: $priority"
                else "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes\""
        }

    fun numberOfNotesByPriority(priority: Int): Int =
        if (notes.isEmpty()) 0
        else notes.count { it.notePriority ==  priority}

    // Delete a note from the collection of notes, while also checking if the index is valid
    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }

    /*
    Update a note by index
    Index of the note to be updated is passed
    And the new note is also passed which will be stored in the same index
     */
    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        //find the note object by the index number
        val foundNote = findNote(indexToUpdate)

        //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    /*
    Archive a Note
    get index of note passed, get note by the index and set isNoteArchived = true
     */
    fun archiveNoteByIndex(indexToArchive: Int): Boolean{
        val noteToArchive = findNote(indexToArchive)
        if (noteToArchive != null) {
            noteToArchive.isNoteArchived = true
            return true
        }
        return false
    }

    fun sortNoteByDate(): String {
        val sortedNotes = notes.sortedByDescending { it.noteTimeStamp }
        return formatListString(sortedNotes)
    }



    /**
     * Returns a string representation of all notes filtered by month.
     */
    fun listNotesByMonth(month: String): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val filteredNotes = notes.filter { note ->
            LocalDateTime.parse(note.noteTimeStamp, formatter).month.toString().equals(month, ignoreCase = true)
        }
        if (notes.isEmpty()) {
            return "No notes stored"
        }
        if (filteredNotes.isEmpty()) {
            return "There are no notes available for the specified month"
        }
        val stringBuilder = StringBuilder()
        filteredNotes.forEachIndexed { index, note ->
            stringBuilder.append("$index : $note")
        }
        return stringBuilder.toString()
    }

    /**
     * Returns a string representation of notes sorted by year for a given year.
     */
    fun listNotesByYear(year: Int): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

        // Check if there are any notes available
        if (notes.isEmpty()) {
            return "No notes stored"
        }
        // Filter notes by year using the filter function with a lambda expression
        val filteredNotes = notes.filter { note ->
            LocalDateTime.parse(note.noteTimeStamp, formatter).year == year
        }
        // Check if there are any notes available for the specified year
        if (filteredNotes.isEmpty()) {
            return "There are no notes available for the specified year"
        }
        // Sort filtered notes by year using the sortedBy function with a lambda expression
        val sortedNotes = filteredNotes.sortedBy { note ->
            LocalDateTime.parse(note.noteTimeStamp, formatter).year
        }
        // Build a string representation of the sorted notes using a StringBuilder
        val stringBuilder = StringBuilder()
        sortedNotes.forEachIndexed { index, note ->
            stringBuilder.append("$index : $note")
        }
        // Return the string representation of the sorted notes
        return stringBuilder.toString()
    }

}