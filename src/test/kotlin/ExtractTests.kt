import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnix
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixTrackType
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class ExtractTests {

    companion object {
        private val OUTPUT_FILE = File("extracted")
    }

    @Test
    fun testExtractTrack() {
        OUTPUT_FILE.deleteAfter { of ->
            val sub = MkvToolnix.identify(TEST_FILE).tracks.first { it.type == MkvToolnixTrackType.subtitles }
            MkvToolnix.extract(TEST_FILE)
                .extractTracks {
                    addTrack(sub, of)
                }
                .executeAndPrint(true)

            assertEquals(
                65279.toChar() + "1\n00:00:00,000 --> 00:00:01,000\ntest 1\n\n2\n00:00:01,500 --> 00:00:02,500\ntest 2\n\n",
                of.readText().replace("\r\n", "\n")
            )
        }
    }

    @Test
    fun testExtractAttachment() {
        OUTPUT_FILE.deleteAfter { of ->
            val att = MkvToolnix.identify(TEST_FILE).attachments.first()
            MkvToolnix.extract(TEST_FILE)
                .extractAttachments {
                    addAttachment(att, of)
                }
                .executeAndPrint(true)

            assertEquals("attachment 1", of.readText())
        }
    }
}
