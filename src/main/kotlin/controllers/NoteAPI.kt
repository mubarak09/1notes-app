package controllers

import models.Note

class NoteAPI {
    private var notes = ArrayList<Note>()

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


    // method for listing all the active notes
    /*
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

}