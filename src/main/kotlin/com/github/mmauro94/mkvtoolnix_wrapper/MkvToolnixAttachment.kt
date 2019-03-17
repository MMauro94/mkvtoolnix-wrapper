package com.github.mmauro94.mkvtoolnix_wrapper

import com.beust.klaxon.Json
import com.github.mmauro94.mkvtoolnix_wrapper.json.KlaxonBigInteger
import java.math.BigInteger

data class MkvToolnixAttachment(

    @Json("content_type")
    val contentType: String? = null,

    @Json("description")
    val description: String? = null,

    @Json("file_name")
    val fileName: String,

    @Json("id")
    val id: Long,

    @Json("size")
    val size: Long,

    @Json("properties")
    val properties: Properties,

    @Json("type")
    val type: String? = null
) {

    data class Properties(
        @Json("uid")
        @KlaxonBigInteger
        val uid: BigInteger
    )
}