package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsRepository
import cz.mendelu.pef.flashyflashcards.mlkit.MLKitTranslator
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Word
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordsScreenViewModel @Inject constructor(
    private val wordCollectionsRepository: WordCollectionsRepository,
    private val mlKitTranslator: MLKitTranslator
) : BaseViewModel() {

    var uiState by mutableStateOf(UiState<List<Word>, ScreenErrors>(loading = true))

    fun getAllWordCollectionWords(wordCollectionId: Long?) {
        launch {
            wordCollectionsRepository.getWordCollectionAndWordsById(wordCollectionId)
                .collect { entity ->
                    if (entity != null) {
                        mlKitTranslator.setLanguages(
                            entity.wordCollectionEntity.sourceLanguage,
                            entity.wordCollectionEntity.targetLanguage
                        )

                        val data = entity.wordEntities.map { Word.createFromWordEntity(it) }

                        uiState = UiState(
                            data = data,
                        )
                    }
            }
        }
    }
}