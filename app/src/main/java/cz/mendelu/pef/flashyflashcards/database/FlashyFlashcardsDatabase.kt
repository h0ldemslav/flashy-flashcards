package cz.mendelu.pef.flashyflashcards.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesDao
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsDao
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordsDao
import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import cz.mendelu.pef.flashyflashcards.model.entities.WordCollectionEntity
import cz.mendelu.pef.flashyflashcards.model.entities.WordEntity

@Database(
    entities = [
        WordCollectionEntity::class,
        WordEntity::class,
        BusinessEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class FlashyFlashcardsDatabase : RoomDatabase() {

    abstract fun wordCollectionsDao(): WordCollectionsDao
    abstract fun wordsDao(): WordsDao
    abstract fun businessesDao(): BusinessesDao

    companion object {

        private var INSTANCE: FlashyFlashcardsDatabase? = null

        fun getDatabase(context: Context): FlashyFlashcardsDatabase {
            if (INSTANCE == null) {
                synchronized(FlashyFlashcardsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            FlashyFlashcardsDatabase::class.java,
                            "flashy_flashcards_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }

            return INSTANCE!!
        }
    }
}