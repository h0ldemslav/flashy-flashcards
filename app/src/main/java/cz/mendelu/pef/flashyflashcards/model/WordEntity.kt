package cz.mendelu.pef.flashyflashcards.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "word_collection_words",
    foreignKeys = [
        ForeignKey(
            entity = WordCollectionEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("wordCollectionId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String,
    var translation: String,
    var wordCollectionId: Long?
) {
    companion object {

        fun createFromWord(word: Word): WordEntity {
            return WordEntity(
                id = word.id,
                name = word.name,
                translation = word.translation,
                wordCollectionId = word.wordCollectionId
            )
        }
    }
}
