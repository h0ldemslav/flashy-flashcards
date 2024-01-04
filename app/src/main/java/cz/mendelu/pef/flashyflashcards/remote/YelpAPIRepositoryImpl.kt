package cz.mendelu.pef.flashyflashcards.remote

import cz.mendelu.pef.flashyflashcards.architecture.CommunicationResult
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.BusinessDTO
import cz.mendelu.pef.flashyflashcards.model.YelpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val INVALID_LATLNG: Double = -1.0

class YelpAPIRepositoryImpl @Inject constructor(
    private val yelpAPI: YelpAPI
) : YelpAPIRepository {

    private var lastCachedBusiness: Business? = null

    override suspend fun getBusinessesByQuery(
        locationName: String,
        categories: String,
        offset: Int,
        locale: String
    ): CommunicationResult<YelpResponse> {
        lastCachedBusiness = null

        val categoriesList = categories.split(", ")

        return withContext(Dispatchers.IO) {
            processResponse(yelpAPI.getBusinessesByQuery(
                locationName,
                categoriesList,
                offset,
                locale
            ))
        }
    }

    override fun convertBusinessDTOToBusiness(businessDTO: BusinessDTO): Business {
        return Business(
            remoteId = businessDTO.id,
            name = businessDTO.name ?: "",
            imageUrl = businessDTO.imageUrl ?: "",
            category = businessDTO.categories?.joinToString(", ") { it.title ?: "" } ?: "",
            displayAddress = businessDTO.location?.displayAddress ?: emptyList(),
            businessUrl = businessDTO.url ?: "",
            rating = businessDTO.rating ?: "",
            reviewCount = businessDTO.reviewCount ?: 0,
            latitude = businessDTO.coordinates?.latitude?.toDoubleOrNull() ?: INVALID_LATLNG,
            longitude = businessDTO.coordinates?.longitude?.toDoubleOrNull() ?: INVALID_LATLNG
        )
    }

    override fun cacheBusiness(business: Business) {
        lastCachedBusiness = business
    }

    override fun getCachedBusiness(): Business? = lastCachedBusiness
}