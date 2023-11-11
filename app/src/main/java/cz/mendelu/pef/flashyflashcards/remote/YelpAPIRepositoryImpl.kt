package cz.mendelu.pef.flashyflashcards.remote

import cz.mendelu.pef.flashyflashcards.architecture.CommunicationResult
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.BusinessDTO
import cz.mendelu.pef.flashyflashcards.model.YelpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YelpAPIRepositoryImpl @Inject constructor(
    private val yelpAPI: YelpAPI
) : YelpAPIRepository {

    override suspend fun getBusinessesByQuery(
        locationName: String,
        categories: String
    ): CommunicationResult<YelpResponse> {
        val categoriesList = categories.split(",")

        return withContext(Dispatchers.IO) {
            processResponse(yelpAPI.getBusinessesByQuery(locationName, categoriesList))
        }
    }

    override fun convertBusinessDTOToBusiness(businessDTO: BusinessDTO): Business {
        return Business(
            remoteId = businessDTO.id,
            name = businessDTO.name,
            category = businessDTO.categories?.joinToString(",") { it.title } ?: "",
            displayAddress = businessDTO.location.displayAddress,
            businessUrl = businessDTO.url ?: "",
            rating = businessDTO.rating ?: "",
            reviewCount = businessDTO.reviewCount ?: 0,
            latitude = businessDTO.coordinates.latitude.toDouble(),
            longitude = businessDTO.coordinates.longitude.toDouble()
        )
    }
}