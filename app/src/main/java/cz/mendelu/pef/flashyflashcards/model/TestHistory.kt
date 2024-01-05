package cz.mendelu.pef.flashyflashcards.model

import cz.mendelu.pef.flashyflashcards.model.entities.TestHistoryEntity

data class TestHistory(
    var id: Long? = null,
    var numberOfCorrectAnswers: Int = 0,
    var answers: MutableList<FlashcardAnswer> = mutableListOf(),
    var dateOfCompletion: Long = 0L,
    var timeTaken: Long = 0L,
    var wordCollectionId: Long?
) {
    companion object {

        fun createFromTestHistoryEntity(testHistoryEntity: TestHistoryEntity): TestHistory {
            return TestHistory(
                id = testHistoryEntity.id,
                numberOfCorrectAnswers = testHistoryEntity.numberOfCorrectAnswers,
                answers = testHistoryEntity.answers.toMutableList(),
                dateOfCompletion = testHistoryEntity.dateOfCompletion,
                timeTaken = testHistoryEntity.timeTaken,
                wordCollectionId = testHistoryEntity.wordCollectionId
            )
        }
    }
}
