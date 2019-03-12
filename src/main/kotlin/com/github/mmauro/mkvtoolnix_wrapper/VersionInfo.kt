package com.github.mmauro.mkvtoolnix_wrapper

/**
 * Holds the complete version info
 */
data class VersionInfo(
    /** The name of the program. Should be either `mkvmerge` or `mkvpropedit` */
    val programName : String,
    /** The version of the program */
    val version : Version,
    /** The codename of the program */
    val codename : String,
    /** Whether the program is compiled for 64-bit processors */
    val is64bit : Boolean
)