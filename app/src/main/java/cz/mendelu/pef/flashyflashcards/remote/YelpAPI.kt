package cz.mendelu.pef.flashyflashcards.remote

import cz.mendelu.pef.flashyflashcards.model.YelpResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YelpAPI {

    @GET("businesses/search")
    suspend fun getBusinessesByQuery(
        @Query("location") locationName: String,
        @Query("categories") categories: List<String>
    ): Response<YelpResponse>
}