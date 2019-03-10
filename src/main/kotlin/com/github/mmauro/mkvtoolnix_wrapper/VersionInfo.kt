package com.github.mmauro.mkvtoolnix_wrapper

data class VersionInfo(
    val programName : String,
    val version : Version,
    val codename : String,
    val is64bit : Boolean
)