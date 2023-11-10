package cz.mendelu.pef.flashyflashcards.architecture

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import cz.mendelu.pef.flashyflashcards.BuildConfig

const val NO_INTERNET_CONNECTION_CODE = 9999

class NetworkConnectionInterceptor : Interceptor {
    // https://stackoverflow.com/questions/58697459/handle-exceptions-thrown-by-a-custom-okhttp-interceptor-in-kotlin-coroutines

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
            .newBuilder()
            .header("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .build()

        try {
            val response = chain.proceed(request)
            val bodyString = response.body!!.string()

            return response
                .newBuilder()
                .body(bodyString.toResponseBody(response.body?.contentType()))
                .build()
        } catch (e: Exception) {
            e.printStackTrace()

            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(NO_INTERNET_CONNECTION_CODE)
                .message(e.message ?: "Unknown exception")
                .body("{${e}}".toResponseBody(null))
                .build()
        }
    }
}