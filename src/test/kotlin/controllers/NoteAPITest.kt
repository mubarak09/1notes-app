package controllers

import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI()
    private var emptyNotes: NoteAPI? = NoteAPI()

    @BeforeEach
    fun setup(){
        learnKotlin = Note("Learning Kotlin", 5, "College", false)
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", false)
        codeApp = Note("Code App", 4, "Work", false)
        testApp = Note("Test App", 4, "Work", false)
        swim = Note("Swim - Pool", 3, "Hobby", false)

        //adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
    }

    @AfterEach
    fun tearDown(){
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedNotes = null
        emptyNotes = null
    }

    @Nested
    inner class AddNotes {
        @Test
        fun `adding a Note to a populated list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false)
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.add(newNote))
            assertEquals(1, emptyNotes!!.numberOfNotes())
            assertEquals(newNote, emptyNotes!!.findNote(emptyNotes!!.numberOfNotes() - 1))
        }
    }

    @Nested
    inner class ListNotes {

        @Test
        fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listAllNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `listAllNotes returns Notes when ArrayList has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listAllNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("swim"))
            assertTrue(notesString.contains("summer holiday"))
        }

        @Test
        fun `listActiveNotes() returns Notes that have isNoteArchived set to false`(){
            assertTrue(!populatedNotes!!.listActiveNotes().contains("No notes stored") || !populatedNotes!!.listActiveNotes().contains("No active notes stored"))
        }

        @Test
        fun `listArchivedNotes() returns Notes that have isNoteArchived set to true`() {
            assertEquals("No archived notes stored", populatedNotes!!.listArchivedNotes())

        }

        @Test
        fun `numberOfArchivedNotes() returns the amount of Notes that are Archived`() {
            assertEquals(0, populatedNotes!!.numberOfArchivedNotes())
            val newNote = Note("Study Lambdas", 1, "College", true)
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(1, populatedNotes!!.numberOfArchivedNotes())
        }

        @Test
        fun `numberOfActiveNotes() returns the amount of Notes that are Active`() {
            assertEquals(5, populatedNotes!!.numberOfActiveNotes())
            val newNote = Note("Study Lambdas", 1, "College", false)
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfActiveNotes())
        }

        @Test
        fun `listNotesBySelectedPriority() returns Notes based on the priority`(){
            assertTrue(populatedNotes!!.listNotesBySelectedPriority(1).contains("Summer Holiday to France"))
            val newNote = Note("Study Lambdas", 1, "College", false)
            assertTrue(populatedNotes!!.add(newNote))
            val notes = populatedNotes!!.listNotesBySelectedPriority(1)
            assertTrue(notes.contains("Summer Holiday to France"))
            assertTrue(notes.contains("Study Lambdas"))
        }

        @Test
        fun `numberOfNotesByPriority()returns the number of notes based on the priority passed`(){
            assertEquals(2, populatedNotes!!.numberOfNotesByPriority(4))
            val newNote = Note("Study Lambdas", 4, "College", false)
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(3, populatedNotes!!.numberOfNotesByPriority(4))
        }

    }

    @Nested
    inner class DeleteNotes {

        @Test
        fun `deleting a Note that does not exist, returns null`() {
            assertNull(emptyNotes!!.deleteNote(0))
            assertNull(populatedNotes!!.deleteNote(-1))
            assertNull(populatedNotes!!.deleteNote(5))
        }

        @Test
        fun `deleting a note that exists delete and returns deleted object`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(swim, populatedNotes!!.deleteNote(4))
            assertEquals(4, populatedNotes!!.numberOfNotes())
            assertEquals(learnKotlin, populatedNotes!!.deleteNote(0))
            assertEquals(3, populatedNotes!!.numberOfNotes())
        }
    }


}
