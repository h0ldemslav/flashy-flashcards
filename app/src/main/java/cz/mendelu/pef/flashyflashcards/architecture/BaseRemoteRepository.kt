package cz.mendelu.pef.flashyflashcards.architecture

import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface BaseRemoteRepository {
    fun <T: Any> processResponse(call: Response<T>): CommunicationResult<T> {
        try {
            if (call.isSuccessful) {
                call.body()?.let {
                    return CommunicationResult.Success(it)
                } ?: kotlin.run {
                    return CommunicationResult.Error(
                        CommunicationError(
                            call.code(),
                            call.errorBody().toString()
                        )
                    )
                }
            } else if (call.code() == NO_INTERNET_CONNECTION_CODE) {
                return CommunicationResult.ConnectionError
            } else {
                return CommunicationResult.Error(
                    CommunicationError(
                        call.code(),
                        call.errorBody().toString()
                    )
                )
            }

        } catch (ex: UnknownHostException) {
            return CommunicationResult.Exception(ex)
        } catch (socketException: SocketTimeoutException) {
            return CommunicationResult.ConnectionError
        } catch (ex: Exception) {
            return CommunicationResult.Exception(ex)
        }
    }
}