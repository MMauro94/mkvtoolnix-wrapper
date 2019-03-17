import com.github.mmauro94.mkvtoolnix_wrapper.*
import java.awt.Dimension
import java.io.File
import java.math.BigInteger
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import kotlin.test.assertTrue

val TEST_FILE = File("src/test/resources/test.mkv")
/** The expected identification information given by the test file */
val EXPECTED_IDENTIFICATION by lazy {
    MkvToolnixFileIdentification(
        attachments = listOf(
            MkvToolnixAttachment(
                contentType = "text/plain",
                description = "my attachment",
                fileName = "attach.txt",
                id = 1,
                properties = MkvToolnixAttachment.Properties(
                    uid = BigInteger("17773189110686623810")
                ),
                size = 12
            )
        ),
        chapters = emptyList(),
        container = MkvToolnixContainer(
            recognized = true,
            supported = true,
            type = "Matroska",
            properties = MkvToolnixContainer.Properties(
                containerType = 17,
                dateLocal = ZonedDateTime.parse("2019-03-17T15:33:35+01:00"),
                dateUtc = Instant.parse("2019-03-17T14:33:35Z"),
                duration = Duration.ofSeconds(9) + Duration.ofMillis(90),
                isProvidingTimestamps = true,
                muxingApplication = "libebml v1.3.7 + libmatroska v1.5.0",
                segmentUid = "c083eb6e1801e0fc197bb829495130eb",
                writingApplication = "mkvmerge v32.0.0 ('Astral Progressions') 64-bit"
            )
        ),
        errors = emptyList(),
        fileName = TEST_FILE.absoluteFile,
        tracks = listOf(
            MkvToolnixTrack(
                codec = "MPEG-H/HEVC/h.265",
                id = 0,
                type = MkvToolnixTrackType.video,
                properties = MkvToolnixTrack.Properties(
                    codecId = "V_MPEGH/ISO/HEVC",
                    codecPrivateData = "01022000000090000000000078f000fffdfafa00000f03a00001001940010c01ffff022000000300900000030000030078998a0240a10001002b420101022000000300900000030000030078a003c08010e4d96662a490846bc0404000001900000302ed42a2000100074401c176b66240",
                    codecPrivateLength = 113,
                    defaultTrack = true,
                    displayDimensions = Dimension(1920, 1080),
                    displayUnit = 0,
                    enabledTrack = true,
                    forcedTrack = false,
                    language = MkvToolnixLanguage.all["und"],
                    minimumTimestamp = Duration.ofMillis(67),
                    number = 1,
                    packetizer = "mpegh_p2_video",
                    pixelDimensions = Dimension(1920, 1080),
                    trackName = "test video",
                    uid = BigInteger("1")
                )
            ),
            MkvToolnixTrack(
                codec = "MP3",
                id = 1,
                type = MkvToolnixTrackType.audio,
                properties = MkvToolnixTrack.Properties(
                    audioChannels = 1,
                    audioSamplingFrequency = 22050,
                    codecId = "A_MPEG/L3",
                    codecPrivateLength = 0,
                    defaultTrack = true,
                    enabledTrack = true,
                    forcedTrack = false,
                    language = MkvToolnixLanguage.all["eng"],
                    minimumTimestamp = Duration.ZERO,
                    number = 2,
                    trackName = "test audio 1",
                    uid = BigInteger("10592032113306683033")
                )
            ),
            MkvToolnixTrack(
                codec = "MP3",
                id = 2,
                type = MkvToolnixTrackType.audio,
                properties = MkvToolnixTrack.Properties(
                    audioChannels = 2,
                    audioSamplingFrequency = 44100,
                    codecId = "A_MPEG/L3",
                    codecPrivateLength = 0,
                    defaultTrack = false,
                    enabledTrack = true,
                    forcedTrack = false,
                    language = MkvToolnixLanguage.all["ita"],
                    minimumTimestamp = Duration.ZERO,
                    number = 3,
                    trackName = "test audio 2",
                    uid = BigInteger("853947814993401840")
                )
            ),
            MkvToolnixTrack(
                codec = "SubRip/SRT",
                id = 3,
                type = MkvToolnixTrackType.subtitles,
                properties = MkvToolnixTrack.Properties(
                    codecId = "S_TEXT/UTF8",
                    codecPrivateLength = 0,
                    defaultTrack = false,
                    enabledTrack = true,
                    encoding = "UTF-8",
                    forcedTrack = false,
                    language = MkvToolnixLanguage.all["eng"],
                    minimumTimestamp = Duration.ZERO,
                    textSubtitles = true,
                    number = 4,
                    trackName = "test sub 1",
                    uid = BigInteger("5591647768829850670")
                )
            ),
            MkvToolnixTrack(
                codec = "SubRip/SRT",
                id = 4,
                type = MkvToolnixTrackType.subtitles,
                properties = MkvToolnixTrack.Properties(
                    codecId = "S_TEXT/UTF8",
                    codecPrivateLength = 0,
                    defaultTrack = true,
                    enabledTrack = true,
                    encoding = "UTF-8",
                    forcedTrack = false,
                    language = MkvToolnixLanguage.all["ita"],
                    minimumTimestamp = Duration.ofSeconds(1),
                    textSubtitles = true,
                    number = 5,
                    trackName = "test sub 2",
                    uid = BigInteger("16685070085689897945")
                )
            )
        ),
        warnings = emptyList()
    )
}

fun MkvToolnixCommand<*>.executeAndAssert() {
    executeAndPrint(true).apply {
        assertTrue(success, "Command exited with code: $exitCode")
    }

}