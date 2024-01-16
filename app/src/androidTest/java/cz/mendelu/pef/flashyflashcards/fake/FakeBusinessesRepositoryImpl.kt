package cz.mendelu.pef.flashyflashcards.fake

import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesRepository
import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBusinessesRepositoryImpl : BusinessesRepository {
    private val businessEntities = mutableListOf(
        BusinessEntity(
            id = 0,
            remoteId = "remote 0",
            name = "Vasamuseet",
            imageUrl = "",
            category = "Museums",
            displayAddress = "Stockholm",
            businessUrl = "",
            rating = "4.5",
            reviewCount = 39,
            latitude = 59.32811,
            longitude = 18.09139,
            whenAdded = 1705254833406
        ),
        BusinessEntity(
            id = 1,
            remoteId = "remote 1",
            name = "Moravska knihovna",
            imageUrl = "",
            category = "Libraries",
            displayAddress = "Brno",
            businessUrl = "",
            rating = "4",
            reviewCount = 2,
            latitude = 49.12,
            longitude = 16.35,
            whenAdded = 1705254833406
        ),
        BusinessEntity(
            id = 2,
            remoteId = "remote 2",
            name = "Narodni muzeum",
            imageUrl = "",
            category = "Museums",
            displayAddress = "Praha",
            businessUrl = "",
            rating = "4.5",
            reviewCount = 51,
            latitude = 50.4,
            longitude = 14.25,
            whenAdded = 1705254833406
        )
    )

    override fun getAllBusinesses(): Flow<List<BusinessEntity>> {
        return flow {
            emit(businessEntities)
        }
    }

    override suspend fun getBusinessByRemoteId(businessRemoteId: String): BusinessEntity? {
        return businessEntities.firstOrNull { it.remoteId == businessRemoteId }
    }

    override suspend fun addNewBusiness(businessEntity: BusinessEntity): Long? {
        if (businessEntity.id == null) {
            businessEntity.id = businessEntities.size.toLong()
        }
        businessEntities.add(businessEntity)

        return businessEntity.id
    }

    override suspend fun updateBusiness(businessEntity: BusinessEntity) {
        val businessToUpdate = businessEntities.firstOrNull { it.id == businessEntity.id }
        businessToUpdate?.let {
            it.remoteId = businessEntity.remoteId
            it.name = businessEntity.name
            it.imageUrl = businessEntity.imageUrl
            it.category = businessEntity.category
            it.displayAddress = businessEntity.displayAddress
            it.businessUrl = businessEntity.businessUrl
            it.rating = businessEntity.rating
            it.reviewCount = businessEntity.reviewCount
            it.latitude = businessEntity.latitude
            it.longitude = businessEntity.longitude
            it.whenAdded = businessEntity.whenAdded
        }
    }

    override suspend fun deleteBusiness(businessEntity: BusinessEntity) {
        for (index in businessEntities.indices) {
            if (businessEntities[index].id == businessEntity.id) {
                businessEntities.removeAt(index)
                break
            }
        }
    }
}