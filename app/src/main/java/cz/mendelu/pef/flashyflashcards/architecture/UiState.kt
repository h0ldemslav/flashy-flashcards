package cz.mendelu.pef.flashyflashcards.architecture

import java.io.Serializable

open class UiState<T, E>(
    var loading: Boolean = false,
    var data: T? = null,
    var errors: E? = null
) : Serializable