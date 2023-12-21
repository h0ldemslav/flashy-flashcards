package cz.mendelu.pef.flashyflashcards.model

import cz.mendelu.pef.flashyflashcards.model.entities.TestHistoryEntity

data class TestHistory(
    var id: Long?,
    var answers: List<FlashcardAnswer>,
    var dateOfCompletion: Long,
    var timeTaken: Long,
    var wordCollectionId: Long?
) {
    companion object {

        fun createFromTestHistoryEntity(testHistoryEntity: TestHistoryEntity): TestHistory {
            return TestHistory(
                id = testHistoryEntity.id,
                answers = testHistoryEntity.answers,
                dateOfCompletion = testHistoryEntity.dateOfCompletion,
                timeTaken = testHistoryEntity.timeTaken,
                wordCollectionId = testHistoryEntity.wordCollectionId
            )
        }
    }
}
