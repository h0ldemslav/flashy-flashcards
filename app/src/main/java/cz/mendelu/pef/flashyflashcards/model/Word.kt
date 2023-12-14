package cz.mendelu.pef.flashyflashcards.model

import java.io.Serializable

// `Word` can be Serializable, since it won't be too big
data class Word(
    var id: Long? = null,
    var name: String,
    var translation: String,
    var wordCollectionId: Long?
) : Serializable {
    companion object {

        fun createFromWordEntity(wordEntity: WordEntity): Word {
            return Word(
                id = wordEntity.id,
                name = wordEntity.name,
                translation = wordEntity.translation,
                wordCollectionId = wordEntity.wordCollectionId
            )
        }
    }
}
