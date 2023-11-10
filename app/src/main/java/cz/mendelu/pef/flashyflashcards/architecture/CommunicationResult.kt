package cz.mendelu.pef.flashyflashcards.architecture

sealed class CommunicationResult<out T : Any> {
    class Success<out T : Any>(val data: T) : CommunicationResult<T>()
    class Error(val error: CommunicationError) : CommunicationResult<Nothing>()
    object ConnectionError : CommunicationResult<Nothing>()
    class Exception(val exception: Throwable) : CommunicationResult<Nothing>()
}

