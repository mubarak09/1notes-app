package controllers

import models.Note
import persistence.Serializer

class NoteAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType

    // ArrayList for notes
    private var notes = ArrayList<Note>()

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
        else notes.joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString() }




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
            else notes.filter { note -> !note.isNoteArchived }
                .joinToString(separator = "\n") {note ->
                    notes.indexOf(note).toString() + ": " + note.toString() }

    // method for listing all the Archived notes
    /*
    * First check if the note arraylist is empty or not; if so return No notes stored
    * Then loop through the ArrayList also checking the isNoteArchived variable
    * If isNoteArchived is true then add that note to the list that will be returned.
    * */
    fun listArchivedNotes(): String =
        if(numberOfArchivedNotes() == 0) "No archived notes stored"
         else notes.filter { note -> note.isNoteArchived }
             .joinToString(separator = "\n"){note ->
                notes.indexOf(note).toString() + ": " + note.toString()}

    fun numberOfArchivedNotes(): Int {
        return notes.stream()
            .filter{note: Note -> note.isNoteArchived}
            .count()
            .toInt()
    }

    fun numberOfActiveNotes(): Int {
        return notes.stream()
            .filter{note: Note -> !note.isNoteArchived}
            .count()
            .toInt()
    }


    fun listNotesBySelectedPriority(priority: Int): String =
        if (numberOfNotesByPriority(priority) == 0) "No notes stored with priority: $priority"
            else notes.filter { note -> note.notePriority == priority }
                .joinToString(separator = "\n") { note ->
                    notes.indexOf(note).toString() + ": " + note.toString()}

//    fun numberOfNotesByPriority(priority: Int): Int {
//        //helper method to determine how many notes there are of a specific priority
//        var numOfNotes: Int = 0
//
//        return if (notes.isEmpty()) {
//            0
//        } else {
//            for (i in notes.indices) {
//                if (notes[i].notePriority == priority) {
//                    numOfNotes++
//                }
//            }
//            return numOfNotes
//        }
//    }

    fun numberOfNotesByPriority(priority: Int): Int {
        //helper method to determine how many notes there are of a specific priority
        var numOfNotes: Int = 0

        return if (notes.isEmpty()) {
            0
        } else {
            numOfNotes = notes.stream().filter { it.notePriority ==  priority}
                .count()
                .toInt()
            return numOfNotes
        }
    }

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

}