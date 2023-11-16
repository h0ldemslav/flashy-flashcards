package cz.mendelu.pef.flashyflashcards.remote

import cz.mendelu.pef.flashyflashcards.model.YelpResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val MAX_OFFSET = 1000

interface YelpAPI {

    @GET("businesses/search")
    suspend fun getBusinessesByQuery(
        @Query("location") locationName: String,
        @Query("categories") categories: List<String>,
        @Query("offset") offset: Int
    ): Response<YelpResponse>
}