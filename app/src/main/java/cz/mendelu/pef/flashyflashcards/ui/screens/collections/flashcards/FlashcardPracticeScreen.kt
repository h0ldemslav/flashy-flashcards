package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.FlashcardPracticeType
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.navigation.graphs.CollectionsNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTextFieldElement
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicTimer
import cz.mendelu.pef.flashyflashcards.ui.elements.Flashcard
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.mediumMargin
import cz.mendelu.pef.flashyflashcards.ui.theme.smallMargin

@CollectionsNavGraph
@Destination
@Composable
fun FlashcardPracticeScreen(
    navController: NavController,
    viewModel: FlashcardPracticeScreenViewModel = hiltViewModel(),
    collectionId: Long?,
    flashcardPracticeType: FlashcardPracticeType
) {
    LaunchedEffect(Unit) {
        if (viewModel.uiState.data == null) {
            viewModel.getAllCollectionWords(collectionId)
        }
    }

    BasicScaffold(
        topAppBarTitle = if (flashcardPracticeType == FlashcardPracticeType.Test)
            stringResource(id = R.string.test_label)
        else
            stringResource(id = R.string.training_label)
        ,
        showLoading = viewModel.uiState.loading,
        onBackClick = { navController.popBackStack() }
    ) { paddingValues ->
        FlashcardPracticeScreenContent(
            paddingValues = paddingValues,
            uiState = viewModel.uiState,
            actions = viewModel,
            flashcardPracticeType = flashcardPracticeType
        )
    }
}

@Composable
fun FlashcardPracticeScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<FlashcardPracticeScreenData, ScreenErrors>,
    actions: FlashcardPracticeScreenActions,
    flashcardPracticeType: FlashcardPracticeType
) {
    var timer by remember {
        mutableStateOf(15000L)
    }

    val finishedText = if (flashcardPracticeType == FlashcardPracticeType.Test) {
        stringResource(id = R.string.test_finished)
    } else {
        stringResource(id = R.string.training_finished)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(mediumMargin()),
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(basicMargin())
            .padding(top = basicMargin())
    ) {
        if (uiState.data != null && uiState.data?.finish == false) {
            val answer = uiState.data!!.answer
            val flashcardText = uiState.data!!.flashcardText
            val currentWordNumber = uiState.data!!.currentWordNumber
            val totalWordsNumber = uiState.data!!.words.size

            if (flashcardPracticeType == FlashcardPracticeType.Test) {
                Practice(
                    uiState = null,
                    flashcardText = flashcardText,
                    onFlashcardClick = null,
                    answer = answer,
                    answerSupportingText = null,
                    onAnswerChange = { actions.setAnswer(it) },
                    timer = timer,
                    onTimerUpdate = { timer = it - 100L },
                    wordCount = "${currentWordNumber}/${totalWordsNumber}",
                    actionButtonLabel = stringResource(id = R.string.next_label)
                ) {
                    actions.setNextWord()
                    // Reset timer for new card (word)
                    timer = 15000L
                }
            } else {
                Practice(
                    uiState = uiState,
                    flashcardText = flashcardText,
                    onFlashcardClick = { actions.setFlashcardText() },
                    answer = answer,
                    answerSupportingText = stringResource(id = R.string.flashcard_hint),
                    onAnswerChange = { actions.setAnswer(it) },
                    timer = null,
                    onTimerUpdate = null,
                    actionButtonLabel = stringResource(id = R.string.next_label)
                ) {
                    // User should answer correctly
                    if (actions.isAnswerCorrect(answer)) {
                        actions.setNextWord()
                    }
                }
            }
        } else if (uiState.errors == null) {
            Text(
                color = MaterialTheme.colorScheme.primary,
                text = finishedText.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Button(onClick = {
                actions.resetFlashcard()
            }) {
                Text(text = stringResource(id = R.string.repeat_label))
            }
        } else {
            PlaceholderElement(
                imageRes = null,
                textRes = uiState.errors!!.messageRes
            )
        }
    }
}

@Composable
fun Practice(
    uiState: UiState<FlashcardPracticeScreenData, ScreenErrors>?,
    flashcardText: String,
    onFlashcardClick: (() -> Unit)?,
    answer: String,
    answerSupportingText: String?,
    onAnswerChange: (String) -> Unit,
    timer: Long?,
    onTimerUpdate: ((Long) -> Unit)?,
    wordCount: String? = null,
    actionButtonLabel: String,
    onActionButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (wordCount != null) {
            Text(
                text = wordCount,
                modifier = Modifier.weight(0.5f),
                style = MaterialTheme.typography.titleMedium,
            )
        }

        if (timer != null) {
            BasicTimer(
                totalTimeInMillis = timer,
                modifier = Modifier.weight(0.5f)
            ) {
                if (onTimerUpdate != null) {
                    onTimerUpdate(it)
                }
            }
        }
    }

    Flashcard(text = flashcardText) {
        if (onFlashcardClick != null) {
            onFlashcardClick()
        }
    }

    Spacer(modifier = Modifier.height(smallMargin()))

    BasicTextFieldElement(
        value = answer,
        onValueChange = onAnswerChange,
        label = stringResource(id = R.string.answer_label),
        supportingText = answerSupportingText,
        enabled = if (timer != null && timer <= 0) false else true,
        errorMessage = if (uiState?.errors != null)
            stringResource(id = uiState.errors!!.messageRes)
        else
            null
    )

    ElevatedButton(onClick = {
        onActionButtonClick()
    }) {
        Text(text = actionButtonLabel)
    }
}