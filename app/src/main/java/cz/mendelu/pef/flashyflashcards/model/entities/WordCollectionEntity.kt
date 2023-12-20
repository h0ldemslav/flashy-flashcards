package cz.mendelu.pef.flashyflashcards.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.mendelu.pef.flashyflashcards.model.WordCollection

@Entity(tableName = "word_collections")
data class WordCollectionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String,
    var sourceLanguage: String,
    var targetLanguage: String
) {
    companion object {

        fun createFromCollection(collection: WordCollection): WordCollectionEntity {
            return WordCollectionEntity(
                id = collection.id,
                name = collection.name,
                sourceLanguage = collection.sourceLanguage,
                targetLanguage = collection.targetLanguage
            )
        }
    }
}