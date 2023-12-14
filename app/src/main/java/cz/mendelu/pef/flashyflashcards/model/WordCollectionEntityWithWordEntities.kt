package cz.mendelu.pef.flashyflashcards.model

import androidx.room.Embedded
import androidx.room.Relation

data class WordCollectionEntityWithWordEntities(
    @Embedded val wordCollectionEntity: WordCollectionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "wordCollectionId"
    )
    val wordEntities: List<WordEntity>
)
