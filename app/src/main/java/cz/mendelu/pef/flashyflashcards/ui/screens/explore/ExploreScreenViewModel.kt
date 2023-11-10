package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.architecture.CommunicationResult
import cz.mendelu.pef.flashyflashcards.remote.YelpAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreScreenViewModel @Inject constructor(
    private val yelpAPIRepository: YelpAPIRepository
) : BaseViewModel() {

    init {
        getBusinessesByQuery("Berlin", "")
    }

    fun getBusinessesByQuery(locationName: String, categories: String) {
        launch {
            val result = yelpAPIRepository.getBusinessesByQuery(locationName, categories)

            when (result) {
                CommunicationResult.ConnectionError -> {
                    println("CONNECTION ERROR")
                }

                is CommunicationResult.Error -> {
                    println("YELP API ERROR CODE: ${result.error.code}")
                }

                is CommunicationResult.Exception -> {
                    println("EXCEPTION OCCURRED")
                }

                is CommunicationResult.Success -> {
                    val businesses = result.data.businesses.map { dto ->
                        yelpAPIRepository.convertBusinessDTOToBusiness(dto)
                    }

                    println("DATA: $businesses")
                }
            }
        }
    }
}