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
    fun testMerge1() {
        val SUB_NAME = "I'M IN SPANISH, ADJUSTED TO START"
        val TITLE = "COOL LOOKING TITLE"

        OUTPUT_FILE.deleteAfter { of ->
            MkvToolnix.merge(of).apply {
                globalOptions {
                    title = TITLE
                }
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
                outputControl {
                    trackOrder.add(0 to 1)
                }
            }.executeAndAssert()

            val actual = MkvToolnix.identify(of)
            assertEquals(
                EXPECTED_IDENTIFICATION.copy(
                    container = EXPECTED_IDENTIFICATION.container.run {
                        copy(
                            properties = properties!!.copy(
                                title = TITLE
                            )
                        )
                    },
                    tracks = listOf(
                        EXPECTED_IDENTIFICATION.tracks[1].run {
                            copy(
                                id = 0,
                                properties = properties!!.copy(
                                    number = 1
                                )
                            )
                        },
                        EXPECTED_IDENTIFICATION.tracks[0].run {
                            copy(
                                id = 1,
                                properties = properties!!.copy(
                                    number = 2,
                                    defaultTrack = false,
                                    forcedTrack = true
                                )
                            )
                        },
                        EXPECTED_IDENTIFICATION.tracks[4].run {
                            copy(
                                id = 2,
                                properties = properties!!.copy(
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

    @Test
    fun testMerge2() {
        OUTPUT_FILE.deleteAfter { of ->
            MkvToolnix.merge(of).apply {
                addTrack(EXPECTED_IDENTIFICATION.tracks[2]) {
                    isForced = true
                }
                addTrack(EXPECTED_IDENTIFICATION.tracks[3]) {
                    language("ger")
                }
            }.executeAndAssert()

            val actual = MkvToolnix.identify(of)
            assertEquals(
                EXPECTED_IDENTIFICATION.copy(
                    container = EXPECTED_IDENTIFICATION.container.run {
                        copy(
                            properties = properties!!.copy(
                                duration = Duration.ofNanos(3605234022)
                            )
                        )
                    },
                    tracks = listOf(
                        EXPECTED_IDENTIFICATION.tracks[2].run {
                            copy(
                                id = 0,
                                properties = properties!!.copy(
                                    number = 1,
                                    forcedTrack = true
                                )
                            )
                        },
                        EXPECTED_IDENTIFICATION.tracks[3].run {
                            copy(
                                id = 1,
                                properties = properties!!.copy(
                                    number = 2,
                                    language = MkvToolnixLanguage.all.getValue("ger")
                                )
                            )
                        }
                    )
                ).editWithActual(actual), actual
            )

        }
    }

    /**
     * Edits the expected value of the merged file with some actual values that cannot be predicted (e.g. date, segment uid, etc.)
     */
    private fun MkvToolnixFileIdentification.editWithActual(actual: MkvToolnixFileIdentification) = this.copy(
        fileName = OUTPUT_FILE.absoluteFile,
        container = container.copy(
            properties = container.properties!!.copy(
                dateLocal = actual.container.properties?.dateLocal,
                dateUtc = actual.container.properties?.dateUtc,
                segmentUid = actual.container.properties?.segmentUid
            )
        )
    )
}
