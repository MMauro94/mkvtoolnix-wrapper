import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnix
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixAttachment
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixLanguage
import com.github.mmauro94.mkvtoolnix_wrapper.MkvToolnixTrackType
import com.github.mmauro94.mkvtoolnix_wrapper.propedit.*
import org.junit.Test
import java.io.File
import java.math.BigInteger
import java.nio.file.Files
import kotlin.test.assertEquals

class PropEditTest {

    @Test fun testPropEditTracks() {
        copyFileForEdit { file ->
            MkvPropEditCommand(file)
                .globalOptions {
                    verbose = true
                }
                .editTrackPropertiesByUid(1.toBigInteger()) {
                    setLanguage("ita")
                    setName("coolvid")
                }
                .editTrackPropertiesByNumber(2) {
                    deleteName()
                }
                .editTrackByPropertiesByPosition(3) {
                    setIsForced(true)
                }
                .editTrackByPropertiesByPosition(1, MkvToolnixTrackType.subtitles) {
                    setUndeterminedLanguage()
                    setIsDefault(true)
                }
                .editTrackByPropertiesByPosition(5) {
                    setIsDefault(false)
                    setIsEnabled(false)
                }
                .executeAndAssert()

            assertEquals(EXPECTED_IDENTIFICATION.copy(
                fileName = file.absoluteFile,
                tracks = listOf(
                    EXPECTED_IDENTIFICATION.tracks[0].run {
                        copy(
                            properties = properties?.copy(
                                language = MkvToolnixLanguage.all.getValue("ita"),
                                trackName = "coolvid"
                            )
                        )
                    },
                    EXPECTED_IDENTIFICATION.tracks[1].run {
                        copy(
                            properties = properties?.copy(
                                trackName = null
                            )
                        )
                    },
                    EXPECTED_IDENTIFICATION.tracks[2].run {
                        copy(
                            properties = properties?.copy(
                                forcedTrack = true
                            )
                        )
                    },
                    EXPECTED_IDENTIFICATION.tracks[3].run {
                        copy(
                            properties = properties?.copy(
                                language = MkvToolnixLanguage.all.getValue("und"),
                                defaultTrack = true
                            )
                        )
                    },
                    EXPECTED_IDENTIFICATION.tracks[4].run {
                        copy(
                            properties = properties?.copy(
                                defaultTrack = false,
                                enabledTrack = false
                            )
                        )
                    }
                )
            ), MkvToolnix.identify(file))
        }
    }

    @Test fun testAttachmentUpdate() {
        val NAME = "hello"
        val DESCRIPTION = "a cool json"
        val MIME_TYPE = "application/json"
        val UID = BigInteger("707")

        copyFileForEdit { file ->
            MkvPropEditCommand(file)
                .globalOptions {
                    verbose = true
                }
                .updateAttachmentByMimeType("text/plain") {
                    mimeType = MIME_TYPE
                }
                .updateAttachmentById(1) {
                    description = DESCRIPTION
                }
                .updateAttachmentByUid(BigInteger("17773189110686623810")) {
                    uid = UID
                }
                .updateAttachmentByName("attach.txt") {
                    name = NAME
                }
                .executeAndAssert()

            assertEquals(EXPECTED_IDENTIFICATION.copy(
                fileName = file.absoluteFile,
                attachments = listOf(
                    EXPECTED_IDENTIFICATION.attachments[0].run {
                        copy(
                            fileName = NAME,
                            description = DESCRIPTION,
                            contentType = MIME_TYPE,
                            properties = properties.copy(
                                uid = UID
                            )
                        )
                    }
                )
            ), MkvToolnix.identify(file))
        }
    }

    @Test fun testAttachmentReplace() {
        val DESCRIPTION = "new attach"

        copyFileForEdit { file ->
            createDummyAttachment { att ->
                MkvPropEditCommand(file)
                    .globalOptions {
                        verbose = true
                    }
                    .replaceAttachmentById(1, att) {
                        description = DESCRIPTION
                    }
                    .executeAndAssert()


                assertEquals(EXPECTED_IDENTIFICATION.copy(
                    fileName = file.absoluteFile,
                    attachments = listOf(
                        EXPECTED_IDENTIFICATION.attachments[0].run {
                            copy(
                                description = DESCRIPTION,
                                size = Files.size(att.toPath())
                            )
                        }
                    )
                ), MkvToolnix.identify(file))
            }
        }
    }

    @Test fun testAttachmentAdd() {
        val NAME = "newattach"
        val DESCRIPTION = "descattach"
        val MIME_TYPE = "application/json"
        val UID = BigInteger("696969")

        copyFileForEdit { file ->
            createDummyAttachment { att ->
                MkvPropEditCommand(file)
                    .globalOptions {
                        verbose = true
                    }
                    .addAttachment(att) {
                        name = NAME
                        description = DESCRIPTION
                        mimeType = MIME_TYPE
                        uid = UID
                    }
                    .executeAndAssert()


                val actual = MkvToolnix.identify(file)
                assertEquals(
                    EXPECTED_IDENTIFICATION.copy(
                        fileName = file.absoluteFile,
                        attachments = listOf(
                            EXPECTED_IDENTIFICATION.attachments[0],
                            MkvToolnixAttachment(
                                contentType = MIME_TYPE,
                                size = Files.size(att.toPath()),
                                description = DESCRIPTION,
                                fileName = NAME,
                                id = actual.attachments[1].id, //Don't know the id it will have
                                properties = MkvToolnixAttachment.Properties(
                                    uid = UID
                                )
                            )
                        )
                    ), actual
                )
            }
        }
    }

    @Test fun testAttachmentDelete() {
        copyFileForEdit { file ->
            MkvPropEditCommand(file)
                .deleteAttachmentByMimeType("text/plain")
                .executeAndAssert()

            assertEquals(
                EXPECTED_IDENTIFICATION.copy(
                    fileName = file.absoluteFile,
                    attachments = emptyList()
                ), MkvToolnix.identify(file)
            )
        }
    }


    /**
     * Copies the test file, calls the callback and then deletes it
     */
    private fun <R> copyFileForEdit(f: (File) -> R): R {
        val fileToEdit = File("test_copy.mkv")
        Files.copy(TEST_FILE.toPath(), fileToEdit.toPath())
        return fileToEdit.deleteAfter(f)
    }
}