package com.github.mmauro94.mkvtoolnix_wrapper

import com.github.mmauro94.mkvtoolnix_wrapper.merge.MkvMergeCommand
import com.github.mmauro94.mkvtoolnix_wrapper.propedit.MkvPropEditCommand

abstract class MkvToolnixException internal constructor(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

sealed class MkvToolnixCommandException(open val result: MkvToolnixCommandResult<*>, message: String? = null, cause: Throwable? = null) : MkvToolnixException(message, cause){

    class MkvPropEditException internal constructor(
        override val result: MkvToolnixCommandResult<MkvPropEditCommand>,
        message: String? = null,
        cause: Throwable? = null
    ) : MkvToolnixCommandException(result, message, cause)

    class MkvMergeException internal constructor(
        override val result: MkvToolnixCommandResult<MkvMergeCommand>,
        message: String? = null,
        cause: Throwable? = null
    ) : MkvToolnixCommandException(result, message, cause)

}

internal typealias ExceptionInitializer<COMMAND> = (MkvToolnixCommandResult<COMMAND>, String?, Throwable?) -> MkvToolnixCommandException