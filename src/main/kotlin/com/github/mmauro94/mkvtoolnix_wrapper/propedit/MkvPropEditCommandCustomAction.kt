package com.github.mmauro94.mkvtoolnix_wrapper.propedit

class MkvPropEditCommandCustomAction(val args : List<String>) : MkvPropEditCommandAction {
    override fun commandArgs() = args
}