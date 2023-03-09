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

    fun listAllNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                listOfNotes += "${i}: ${notes[i]} \n"
            }
            listOfNotes
        }
    }

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
    fun listActiveNotes(): String {
        return if(notes.isEmpty()){
            "No notes stored"
        } else {
            var listOfActiveNotes = ""
            for(i in notes.indices){
                if(!notes[i].isNoteArchived){
                    listOfActiveNotes += "${i}: ${notes[i]} \n"
                }
            }
            if(listOfActiveNotes.isEmpty()){
                return "No active notes stored"
            } else {
                return listOfActiveNotes
            }
        }
    }

    // method for listing all the Archived notes
    /*
    * First check if the note arraylist is empty or not; if so return No notes stored
    * Then loop through the ArrayList also checking the isNoteArchived variable
    * If isNoteArchived is true then add that note to the list that will be returned.
    * */
    fun listArchivedNotes(): String {
        return if(notes.isEmpty()){
            "No notes stored"
        } else {
            var listOfArchivedeNotes = ""
            for(i in notes.indices){
                if(notes[i].isNoteArchived){
                    listOfArchivedeNotes += "${i}: ${notes[i]} \n"
                }
            }
            if(listOfArchivedeNotes.isEmpty()){
                return "No archived notes stored"
            } else {
                return listOfArchivedeNotes
            }
        }
    }

    fun numberOfArchivedNotes(): Int {
        //helper method to determine how many archived notes there are
        var numOfArchivedNotes: Int = 0
        for(i in notes.indices){
            if(notes[i].isNoteArchived){
                numOfArchivedNotes++
            }
        }
        return numOfArchivedNotes
    }

    fun numberOfActiveNotes(): Int {
        //helper method to determine how many active notes there are
        var numOfActiveNotes: Int = 0
        for(i in notes.indices){
            if(!notes[i].isNoteArchived){
                numOfActiveNotes++
            }
        }
        return numOfActiveNotes
    }

    fun listNotesBySelectedPriority(priority: Int): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfPriorityNotes = ""
            for (i in notes.indices) {
                if (notes[i].notePriority == priority) {
                    listOfPriorityNotes += "${i}: ${notes[i]} \n"
                }
            }
            if (listOfPriorityNotes.isEmpty()) {
                return "No notes stored with priority: $priority"
            } else {
                return listOfPriorityNotes
            }
        }
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        //helper method to determine how many notes there are of a specific priority
        var numOfNotes: Int = 0

        return if (notes.isEmpty()) {
            0
        } else {
            for (i in notes.indices) {
                if (notes[i].notePriority == priority) {
                    numOfNotes++
                }
            }
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


}