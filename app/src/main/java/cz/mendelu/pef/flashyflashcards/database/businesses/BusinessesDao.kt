package cz.mendelu.pef.flashyflashcards.database.businesses

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessesDao {

    @Query("SELECT * FROM businesses WHERE remoteId=:businessRemoteId")
    fun getBusinessByRemoteId(businessRemoteId: String): Flow<BusinessEntity?>

    @Query("SELECT * FROM businesses")
    fun getAllBusinesses(): Flow<List<BusinessEntity>>

    @Insert
    suspend fun addNewBusiness(businessEntity: BusinessEntity)

    @Delete
    suspend fun deleteBusiness(businessEntity: BusinessEntity)
}