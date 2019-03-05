package com.github.mmauro.mkvtoolnix_wrapper

import com.beust.klaxon.Json
import java.io.File
import java.math.BigInteger

data class MkvToolnixAttachment(

    @Json("content_type")
    val contentType: String? = null,

    @Json("description")
    val description: String? = null,

    @Json("file_name")
    val fileName: File,

    @Json("id")
    val id: Long,

    @Json("size")
    val size: Int,

    @Json("properties")
    val properties: Properties,

    @Json("type")
    val type: String? = null
) {

    data class Properties(
        @Json("uid")
        val uid: BigInteger
    )
}