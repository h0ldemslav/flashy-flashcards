package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesRepository
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import cz.mendelu.pef.flashyflashcards.model.DataSourceType
import cz.mendelu.pef.flashyflashcards.model.DataSourceType.Local
import cz.mendelu.pef.flashyflashcards.model.DataSourceType.Remote
import cz.mendelu.pef.flashyflashcards.remote.YelpAPIRepository
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val yelpAPIRepository: YelpAPIRepository,
    private val businessesRepository: BusinessesRepository
): BaseViewModel(), DetailScreenActions {

    var uiState by mutableStateOf(UiState<Business?, ScreenErrors>())

    fun getBusiness(dataSourceType: DataSourceType) {
        uiState = UiState(
            loading = true
        )

        launch {
            businessesRepository.getBusinessByRemoteId(dataSourceType.remoteId).collect { entity ->
                var business: Business? = null
                val businessFromDb = if (entity != null) {
                    Business.createFromBusinessEntity(entity)
                } else {
                    null
                }

                when (dataSourceType) {
                    is Local -> {
                        business = businessFromDb
                    }

                    is Remote -> {
                        // Cached business comes from list of fetched businesses from the API
                        // Don't want to modify item from the list, so thus there is a copy
                        business = yelpAPIRepository.getCachedBusiness()?.copy()
                        // `whenAdded` tells, if business is bookmarked
                        business?.whenAdded = businessFromDb?.whenAdded
                        // Business must have the same values as entity, so user can delete the business
                        business?.id = businessFromDb?.id
                    }
                }

                val errors =  if (business == null) {
                    ScreenErrors(
                        imageRes = R.drawable.undraw_empty,
                        messageRes = R.string.something_went_wrong_error
                    )
                } else {
                    null
                }

                uiState = UiState(
                    data = business,
                    errors = errors
                )
            }
        }
    }

    override fun saveBusiness() {
        val business = uiState.data

        if (business != null) {
            val whenAdded = System.currentTimeMillis()

            business.whenAdded = whenAdded

            launch {
                val businessEntity = BusinessEntity.createFromBusiness(business)
                businessesRepository.addNewBusiness(businessEntity)
            }

            uiState = UiState(
                loading = true,
                data = business
            )
        }
    }

    override fun deleteBusiness() {
        val business = uiState.data

        if (business != null) {
            launch {
                val businessEntity = BusinessEntity.createFromBusiness(business)
                businessesRepository.deleteBusiness(businessEntity)
                business.whenAdded = null
                business.id = null

                uiState = UiState(
                    data = business
                )
            }
        }
    }
}