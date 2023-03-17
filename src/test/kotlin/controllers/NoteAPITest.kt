package controllers

import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.CBORSerializer
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))
    private var emptyNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))

    @BeforeEach
    fun setup(){
        learnKotlin = Note("Learning Kotlin", 5, "College", false, "09-02-2023 21:10")
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", false, "16-03-2023 02:45")
        codeApp = Note("Code App", 4, "Work", false, "05-01-2023 22:59")
        testApp = Note("Test App", 4, "Work", false, "13-03-2023 14:34")
        swim = Note("Swim - Pool", 3, "Hobby", false, "15-03-2023 15:00")

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
            val newNote = Note("Study Lambdas", 1, "College", false, "11-03-2023 23:10")
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false, "11-03-2023 23:56")
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.add(newNote))
            assertEquals(1, emptyNotes!!.numberOfNotes())
            assertEquals(newNote, emptyNotes!!.findNote(emptyNotes!!.numberOfNotes() - 1))
        }
    }

    @Nested
    inner class SearchNotes {

        @Test
        fun `search notes by title returns no notes when no notes with that title exist`() {
            //Searching a populated collection for a title that doesn't exist.
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val searchResults = populatedNotes!!.searchByTitle("no results expected")
            assertTrue(searchResults.isEmpty())

            //Searching an empty collection
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search notes by title returns notes when notes with that title exist`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())

            //Searching a populated collection for a full title that exists (case matches exactly)
            var searchResults = populatedNotes!!.searchByTitle("Code App")
            assertTrue(searchResults.contains("Code App"))
            assertFalse(searchResults.contains("Test App"))

            //Searching a populated collection for a partial title that exists (case matches exactly)
            searchResults = populatedNotes!!.searchByTitle("App")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))

            //Searching a populated collection for a partial title that exists (case doesn't match)
            searchResults = populatedNotes!!.searchByTitle("aPp")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))
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
            val newNote = Note("Study Lambdas", 1, "College", true, "16-03-2023 21:23")
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(1, populatedNotes!!.numberOfArchivedNotes())
        }

        @Test
        fun `numberOfActiveNotes() returns the amount of Notes that are Active`() {
            assertEquals(5, populatedNotes!!.numberOfActiveNotes())
            val newNote = Note("Study Lambdas", 1, "College", false, "16-03-2023 21:18")
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfActiveNotes())
        }

        @Test
        fun `listNotesBySelectedPriority() returns Notes based on the priority`(){
            assertTrue(populatedNotes!!.listNotesBySelectedPriority(1).contains("Summer Holiday to France"))
            val newNote = Note("Study Lambdas", 1, "College", false, "16-03-2023 20:07")
            assertTrue(populatedNotes!!.add(newNote))
            val notes = populatedNotes!!.listNotesBySelectedPriority(1)
            assertTrue(notes.contains("Summer Holiday to France"))
            assertTrue(notes.contains("Study Lambdas"))
        }

        @Test
        fun `numberOfNotesByPriority()returns the number of notes based on the priority passed`(){
            assertEquals(2, populatedNotes!!.numberOfNotesByPriority(4))
            val newNote = Note("Study Lambdas", 4, "College", false,"16-03-2023 18:11")
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

    @Nested
    inner class UpdateNotes {
        @Test
        fun `updating a note that does not exist returns false`(){
            assertFalse(populatedNotes!!.updateNote(6, Note("Updating Note", 2, "Work", false, "07-03-2023 23:14")))
            assertFalse(populatedNotes!!.updateNote(-1, Note("Updating Note", 2, "Work", false,"07-03-2023 21:15")))
            assertFalse(emptyNotes!!.updateNote(0, Note("Updating Note", 2, "Work", false, "07-03-2023 23:25")))
        }

        @Test
        fun `updating a note that exists returns true and updates`() {
            //check note 5 exists and check the contents
            assertEquals(swim, populatedNotes!!.findNote(4))
            assertEquals("Swim - Pool", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(3, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("Hobby", populatedNotes!!.findNote(4)!!.noteCategory)

            //update note 5 with new information and ensure contents updated successfully
            assertTrue(populatedNotes!!.updateNote(4, Note("Updating Note", 2, "College", false, "07-03-2023 23:17")))
            assertEquals("Updating Note", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(2, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("College", populatedNotes!!.findNote(4)!!.noteCategory)
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfNotesCalculatedCorrectly() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(0, emptyNotes!!.numberOfNotes())
        }

        @Test
        fun numberOfArchivedNotesCalculatedCorrectly() {
            assertEquals(0, populatedNotes!!.numberOfArchivedNotes())
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
        }

        @Test
        fun numberOfActiveNotesCalculatedCorrectly() {
            assertEquals(5, populatedNotes!!.numberOfActiveNotes())
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
        }

        @Test
        fun numberOfNotesByPriorityCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(1))
            assertEquals(0, populatedNotes!!.numberOfNotesByPriority(2))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(3))
            assertEquals(2, populatedNotes!!.numberOfNotesByPriority(4))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(5))
            assertEquals(0, emptyNotes!!.numberOfNotesByPriority(1))
        }
    }


    /*
    Inner class for testing XML persistence
     */
    @Nested
    inner class PersistenceTests {

        /*
        XML FORMAT TESTS
         */
        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.store()

            //Loading the empty notes.xml file into a new object
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.xml into a different collection
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }

        /*
        JSON FORMAT TESTS
         */
        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.store()

            //Loading the empty notes.json file into a new object
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 notes to the notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.json into a different collection
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }

        /*
        CBOR FORMAT TESTS
         */

        @Test
        fun `saving and loading an empty collection in CBOR doesn't crash app`() {
            // Saving an empty notes.CBOR file.
            val storingNotes = NoteAPI(CBORSerializer(File("notes.cbor")))
            storingNotes.store()

            //Loading the empty notes.cbor file into a new object
            val loadedNotes = NoteAPI(CBORSerializer(File("notes.cbor")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the CBOR loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in CBOR doesn't loose data`() {
            // Storing 3 notes to the notes.CBOR file.
            val storingNotes = NoteAPI(CBORSerializer(File("notes.cbor")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.xml into a different collection
            val loadedNotes = NoteAPI(CBORSerializer(File("notes.cbor")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }
    }

    @Nested
    inner class ArchiveNoteTests {
        @Test
        fun `archive note by index`(){
            val archiveNotes = NoteAPI(JSONSerializer(File("notes.json")))
            archiveNotes.add(testApp!!)
            archiveNotes.add(swim!!)
            archiveNotes.add(summerHoliday!!)

            assertEquals(true, archiveNotes.archiveNoteByIndex(0))
            assertEquals(true, archiveNotes.archiveNoteByIndex(1))
            assertEquals(true, archiveNotes.archiveNoteByIndex(2))
        }
    }


}
