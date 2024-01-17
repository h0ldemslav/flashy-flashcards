package cz.mendelu.pef.flashyflashcards.fake

import cz.mendelu.pef.flashyflashcards.architecture.CommunicationResult
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.BusinessDTO
import cz.mendelu.pef.flashyflashcards.model.Coordinate
import cz.mendelu.pef.flashyflashcards.model.Location
import cz.mendelu.pef.flashyflashcards.model.Region
import cz.mendelu.pef.flashyflashcards.model.YelpResponse
import cz.mendelu.pef.flashyflashcards.remote.INVALID_LATLNG
import cz.mendelu.pef.flashyflashcards.remote.YelpAPIRepository

class FakeYelpAPIRepositoryImpl : YelpAPIRepository {

    private var cachedBusiness: Business? = null
    private var fakeResponse: YelpResponse = YelpResponse(
        total = 0,
        region = Region(null),
        businesses = listOf(
            BusinessDTO(
                id = "3o6ujl4k6klj45l633",
                alias = "Meststka_knihovna",
                coordinates = Coordinate(latitude = "49.16", longitude = "16.12"),
                displayPhone = null,
                hours = null,
                isClosed = false,
                location = Location(
                    displayAddress = listOf("Ceska 12", "61000", "Brno"),
                    crossStreets = ""
                ),
                name = "Mestska knihovna",
                phone = "",
                rating = "5",
                reviewCount = 1
            ),
            BusinessDTO(
                id = "4jhkl5jkl3lkjdg089",
                alias = "Muzeum_romske_kultury",
                coordinates = Coordinate(latitude = "49.18", longitude = "16"),
                displayPhone = null,
                hours = null,
                isClosed = false,
                location = Location(
                    displayAddress = listOf("Namesti Svobody 11", "61000", "Brno"),
                    crossStreets = ""
                ),
                name = "Muzeum romske kultury",
                phone = "",
                rating = "3.5",
                reviewCount = 10
            ),
            BusinessDTO(
                id = "ljk35jlk33jk53",
                alias = "kulturni_stredisko_brno",
                coordinates = Coordinate(latitude = "49", longitude = "16"),
                displayPhone = null,
                hours = null,
                isClosed = false,
                location = Location(
                    displayAddress = listOf("Ceska 12", "61000", "Brno"),
                    crossStreets = ""
                ),
                name = "Kulturni stredisko Brno",
                phone = "",
                reviewCount = 0
            )
        )
    )
    private var fakeResult: CommunicationResult<YelpResponse> =
        CommunicationResult.Success(fakeResponse)

    fun setFakeResult(result: CommunicationResult<YelpResponse>) {
        fakeResult = result
    }

    override suspend fun getBusinessesByQuery(
        locationName: String,
        categories: String,
        offset: Int,
        locale: String
    ): CommunicationResult<YelpResponse> {
        // Workaround for not getting data twice (can happen because of lazycolumn isAtBottom extension)
        if (fakeResult is CommunicationResult.Success && offset > 0) {
            return CommunicationResult.Success(
                YelpResponse(
                    businesses = listOf(),
                    total = 0,
                    region = Region(null)
                )
            )
        }

        return fakeResult
    }

    override suspend fun getBusinessByRemoteID(
        remoteId: String,
        locale: String
    ): CommunicationResult<BusinessDTO> {
        if (fakeResult is CommunicationResult.Success) {
            val index = (0..2).random()

            return CommunicationResult.Success(fakeResponse.businesses[index])
        }

        return CommunicationResult.Exception(
            Throwable("CommunicationResult must be Success or Error")
        )
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
        cachedBusiness = business
    }

    override fun getCachedBusiness(): Business? {
        if (cachedBusiness == null) {
            val index = (0..2).random()

           cachedBusiness = convertBusinessDTOToBusiness(fakeResponse.businesses[index])
        }

        return cachedBusiness
    }
}