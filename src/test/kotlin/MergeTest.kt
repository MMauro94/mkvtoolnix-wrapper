import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnix
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixFileIdentification
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixLanguage
import com.github.mmauro94.mkvtoolnix_wrapper.merge.MkvMergeCommand
import org.junit.Test
import java.io.File
import java.time.Duration
import kotlin.test.assertEquals

class MergeTest {

    companion object {
        private val OUTPUT_FILE = File("output.mkv")
    }

    @Test
    fun testMerge() {
        val SUB_NAME = "I'M IN SPANISH, ADJUSTED TO START"

        OUTPUT_FILE.deleteAfter { of ->
            MkvMergeCommand(of).apply {
                addInputFile(TEST_FILE) {
                    subtitleTracks.excludeAll()
                    audioTracks.include {
                        addById(1)
                    }
                    editTrackById(0) {
                        isForced = true
                        isDefault = false
                    }
                }
                addInputFile(TEST_FILE) {
                    audioTracks.excludeAll()
                    videoTracks.excludeAll()
                    subtitleTracks.include {
                        addByLanguage("ita")
                    }
                    editTrackById(4) {
                        name = SUB_NAME
                        language("spa")
                        sync(Duration.ofSeconds(-1))
                    }
                }
            }.executeAndAssert()

            val actual = MkvToolnix.identify(of)
            assertEquals(
                EXPECTED_IDENTIFICATION.copy(
                    tracks = listOf(
                        EXPECTED_IDENTIFICATION.tracks[0].run {
                            copy(
                                properties = properties?.copy(
                                    defaultTrack = false,
                                    forcedTrack = true
                                )
                            )
                        },
                        EXPECTED_IDENTIFICATION.tracks[1],
                        EXPECTED_IDENTIFICATION.tracks[4].run {
                            copy(
                                id = 2L,
                                properties = properties?.copy(
                                    trackName = SUB_NAME,
                                    number = 3,
                                    language = MkvToolnixLanguage.all.getValue("spa"),
                                    minimumTimestamp = Duration.ZERO
                                )
                            )
                        }
                    )
                ).editWithActual(actual), actual
            )

        }
    }

    private fun MkvToolnixFileIdentification.editWithActual(actual: MkvToolnixFileIdentification) = this.copy(
        fileName = OUTPUT_FILE.absoluteFile,
        container = container.copy(
            properties = container.properties?.copy(
                dateLocal = actual.container.properties?.dateLocal,
                dateUtc = actual.container.properties?.dateUtc,
                segmentUid = actual.container.properties?.segmentUid
            )
        )
    )
}
