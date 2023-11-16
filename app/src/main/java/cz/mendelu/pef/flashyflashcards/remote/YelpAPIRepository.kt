package cz.mendelu.pef.flashyflashcards.remote

import cz.mendelu.pef.flashyflashcards.architecture.BaseRemoteRepository
import cz.mendelu.pef.flashyflashcards.architecture.CommunicationResult
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.BusinessDTO
import cz.mendelu.pef.flashyflashcards.model.YelpResponse

interface YelpAPIRepository : BaseRemoteRepository {

    suspend fun getBusinessesByQuery(locationName: String, categories: String, offset: Int):
            CommunicationResult<YelpResponse>

    fun convertBusinessDTOToBusiness(businessDTO: BusinessDTO): Business
}