package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.architecture.CommunicationResult
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import cz.mendelu.pef.flashyflashcards.model.AppPreferenceConstants
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.BusinessCategory
import cz.mendelu.pef.flashyflashcards.model.Pagination
import cz.mendelu.pef.flashyflashcards.remote.CS_LOCALE
import cz.mendelu.pef.flashyflashcards.remote.EN_LOCALE
import cz.mendelu.pef.flashyflashcards.remote.INVALID_LATLNG
import cz.mendelu.pef.flashyflashcards.remote.MAX_OFFSET
import cz.mendelu.pef.flashyflashcards.remote.YelpAPIRepository
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreScreenViewModel @Inject constructor(
    private val yelpAPIRepository: YelpAPIRepository,
    private val dataStoreRepository: DataStoreRepository
) : BaseViewModel(), ExploreScreenActions {

    var screenData by mutableStateOf(ExploreScreenData())
    var uiState by mutableStateOf(UiState<MutableList<Business>, ScreenErrors>())
    private var pagination = Pagination()
    private var searchLocale = EN_LOCALE

    init {
        launch {
            dataStoreRepository.getAppPreferences().collect { preferences ->
                val language = preferences
                    .find { it.name == AppPreferenceConstants.LANG }
                    ?.value
                
                searchLocale = when (language) {
                    AppPreferenceConstants.LANG_CS -> CS_LOCALE
                    else -> EN_LOCALE
                }
            }
        }
    }

    override fun getBusinessCategoryDisplayNames(): List<Int> {
        // Don't change the order of list elements
        return BusinessCategory.values().map { it.displayName }
    }

    override fun updateScreenData(data: ExploreScreenData) {
        screenData = ExploreScreenData(
            name = data.name,
            businessCategory = data.businessCategory,
            errorMessage = data.errorMessage
        )
    }

    override fun searchPlaces() {
        if (isScreenDataValid(screenData)) {
            screenData = screenData.copy(errorMessage = null)

            pagination.offset = 0
            pagination.isEndOfPagination = false

            uiState = UiState(
                // `data` must be set to `mutableListOf` in order to merge on each successful request
                data = mutableListOf(),
                loading = true
            )

            getBusinesses(pagination.offset)
        } else {
            screenData = screenData.copy(errorMessage = R.string.explore_screen_city_input_error)
        }
    }

    override fun getAnotherPlaces() {
        if (!pagination.isEndOfPagination && pagination.offset <= MAX_OFFSET) {
            uiState = UiState(
                // `data` must be set to previous value in order to save results from last request
                data = uiState.data,
                loading = true
            )

            getBusinesses(pagination.offset)
        }
    }

    override fun cacheBusiness(business: Business) {
        yelpAPIRepository.cacheBusiness(business)
    }

    private fun getBusinesses(offset: Int) {
        launch {
            val result = yelpAPIRepository.getBusinessesByQuery(
                screenData.name,
                screenData.businessCategory.alias,
                offset,
                searchLocale
            )

            when (result) {
                CommunicationResult.ConnectionError -> {
                    uiState = UiState(
                        errors = ScreenErrors(
                            imageRes = R.drawable.undraw_signal_searching,
                            messageRes = R.string.no_internet_connection
                        )
                    )
                }

                is CommunicationResult.Error -> {
                    val errors = getExploreErrorsByResponseStatusCode(result.error.code)

                    uiState = UiState(errors = errors)
                }

                is CommunicationResult.Exception -> {
                    uiState = UiState(
                        errors = ScreenErrors(
                            imageRes = R.drawable.undraw_warning,
                            messageRes = R.string.exception
                        )
                    )
                }

                is CommunicationResult.Success -> {
                    val businesses = mutableListOf<Business>()

                    if (result.data.businesses.isNotEmpty()) {
                        pagination.offset += (result.data.businesses.size + 1)

                        result.data.businesses.forEach { dto ->
                            val business = yelpAPIRepository.convertBusinessDTOToBusiness(dto)

                            if (isBusinessValid(business)) {
                                businesses.add(business)
                            }
                        }
                    } else {
                        pagination.isEndOfPagination = true
                    }

                    uiState.data?.addAll(businesses)
                    uiState = UiState(data = uiState.data)
                }
            }
        }
    }

    private fun isScreenDataValid(data: ExploreScreenData): Boolean {
        return data.name.isNotEmpty()
    }

    private fun getExploreErrorsByResponseStatusCode(statusCode: Int): ScreenErrors {
        val codesToErrors = mapOf(
            400 to ScreenErrors(R.drawable.undraw_empty, R.string.city_not_found),
            401 to ScreenErrors(R.drawable.undraw_security, R.string.unauthorized_access),
            403 to ScreenErrors(R.drawable.undraw_security, R.string.unable_to_query),
            429 to ScreenErrors(R.drawable.undraw_progress_data, R.string.too_many_requests),
            500 to ScreenErrors(R.drawable.undraw_server_down, R.string.server_error),
            503 to ScreenErrors(R.drawable.undraw_server_down, R.string.server_down)
        )

        return codesToErrors[statusCode] ?: ScreenErrors(
            R.drawable.undraw_exploring,
            R.string.unknown_status_code
        )
    }

    private fun isBusinessValid(business: Business): Boolean {
        val emptyName = business.name.isEmpty()
        val invalidLatLng = business.latitude == INVALID_LATLNG || business.longitude == INVALID_LATLNG
        val emptyDisplayAddress = business.displayAddress.isEmpty()
        val emptyRemoteId = business.remoteId.isEmpty()

        if (emptyName || invalidLatLng || emptyDisplayAddress || emptyRemoteId) {
            return false
        }

        return true
    }
}