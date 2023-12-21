package cz.mendelu.pef.flashyflashcards.model.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cz.mendelu.pef.flashyflashcards.database.typeconverters.FlashcardAnswersConverter
import cz.mendelu.pef.flashyflashcards.model.FlashcardAnswer
import cz.mendelu.pef.flashyflashcards.model.TestHistory

@Entity(
    tableName = "test_history",
    foreignKeys = [
        ForeignKey(
            entity = WordCollectionEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("wordCollectionId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(FlashcardAnswersConverter::class)
data class TestHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var answers: List<FlashcardAnswer>,
    var dateOfCompletion: Long,
    var timeTaken: Long,
    var wordCollectionId: Long?
) {
    companion object {

        fun createFromTestHistory(testHistory: TestHistory): TestHistoryEntity {
            return TestHistoryEntity(
                id = testHistory.id,
                answers = testHistory.answers,
                dateOfCompletion = testHistory.dateOfCompletion,
                timeTaken = testHistory.timeTaken,
                wordCollectionId = testHistory.wordCollectionId
            )
        }
    }
}
