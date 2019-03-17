import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnix
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixBinary
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixLanguage
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BaseTests {

    /**
     * Tests the language parsing
     */
    @Test fun test1_Languages() {
        println("Testing languages...")
        System.out.flush()
        MkvToolnixLanguage.all.apply {
            assertTrue(size > 250, "Less than 250 languages? Hmmm...")
            assertTrue(containsKey("und"), "Not found language 'und'")
            assertTrue(containsKey("eng"), "Not found language 'eng'")

            forEach { (k, l) ->
                assertEquals(k, l.iso639_2, "Language not mapped correctly")
            }

            getValue("eng").let { eng ->
                assertEquals("en", eng.iso639_1)
                assertEquals("English", eng.name)
            }

            println("Found ${this.size} languages")
            println()
        }
    }

    /**
     * Tests that the identification. The test file should the same as it is at the start of the test
     */
    @Test fun test2_ExpectedIdentification() {
        println("Testing file identification...")
        MkvToolnix.identify(TEST_FILE).apply {
            tracks.forEachIndexed { i, t ->
                assertEquals(i + 1, t.trackPosition, "Invalid track position")
                assertEquals(this, t.fileIdentification, "Invalid file identification")
            }
            assertEquals(EXPECTED_IDENTIFICATION, this)
        }
        println("OK")
    }

    @Test fun testVersionInfo() {
        MkvToolnixBinary.values().forEach {
            val vi = it.getVersionInfo()
            assertEquals(it.binaryName, vi.programName)
            //All other infos are system dependant
            println("Version is $vi")
        }
    }
}