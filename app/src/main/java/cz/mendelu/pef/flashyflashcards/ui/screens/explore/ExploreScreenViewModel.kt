package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.architecture.CommunicationResult
import cz.mendelu.pef.flashyflashcards.architecture.UiState
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.remote.YelpAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreScreenViewModel @Inject constructor(
    private val yelpAPIRepository: YelpAPIRepository
) : BaseViewModel(), ExploreScreenActions {

    var screenData by mutableStateOf(ExploreScreenData())
    var uiState by mutableStateOf(UiState<List<Business>, ExploreErrors>())

    fun getBusinessesByQuery(locationName: String, categories: String) {
        uiState = UiState(loading = true)

        launch {
            val result = yelpAPIRepository.getBusinessesByQuery(locationName, categories)

            when (result) {
                CommunicationResult.ConnectionError -> {
                    uiState = UiState(
                        loading = false,
                        errors = ExploreErrors(
                            imageRes = R.drawable.undraw_signal_searching,
                            messageRes = R.string.no_internet_connection
                        )
                    )
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

                    uiState = UiState(data = businesses)
                }
            }
        }
    }

    override fun updateScreenData(data: ExploreScreenData) {
        screenData = data
    }

    override fun getBusinesses(locationName: String, categories: String) {
        if (isScreenDataValid(screenData)) {
            screenData.isValid = true
            getBusinessesByQuery(locationName, categories)
        } else {
            screenData.isValid = false
        }
    }

    private fun isScreenDataValid(data: ExploreScreenData): Boolean {
        return data.name.isNotEmpty()
    }
}