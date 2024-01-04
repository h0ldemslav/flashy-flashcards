package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.FlashcardAnswer
import cz.mendelu.pef.flashyflashcards.model.FlashcardPracticeType
import cz.mendelu.pef.flashyflashcards.model.TestHistory
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
import cz.mendelu.pef.flashyflashcards.utils.DateUtils

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

        if (flashcardPracticeType is FlashcardPracticeType.Test) {
            viewModel.setTestHistory(collectionId)
        }
    }

    BasicScaffold(
        topAppBarTitle = stringResource(id = flashcardPracticeType.name),
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
    if (uiState.data != null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(mediumMargin()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(basicMargin())
                .padding(top = basicMargin())
        ) {
            if (uiState.data?.finish == false) {

                when (flashcardPracticeType) {
                    is FlashcardPracticeType.Training -> {
                        FlashcardPracticeTraining(
                            data = uiState.data!!,
                            actions = actions
                        )
                    }

                    is FlashcardPracticeType.Test -> {
                        FlashcardPracticeTest(
                            data = uiState.data!!,
                            actions = actions,
                            initialTestTimer = flashcardPracticeType.initialTimer
                        )
                    }
                }

            } else {
                val testHistory = actions.getTestHistory()

                FlashcardPracticeResult(
                    title = if (testHistory != null)
                        stringResource(id = R.string.test_finished)
                    else
                        stringResource(id = R.string.training_finished),
                    actions = actions,
                    testHistory = testHistory
                )
            }
        }

    } else if (uiState.errors != null) {
        PlaceholderElement(
            paddingValues = paddingValues,
            fillMaxSize = true,
            imageRes = null,
            textRes = uiState.errors!!.messageRes
        )
    }
}

@Composable
fun FlashcardPracticeTest(
    data: FlashcardPracticeScreenData,
    actions: FlashcardPracticeScreenActions,
    initialTestTimer: Long
) {
    var testTimer by remember {
        mutableStateOf(initialTestTimer)
    }
    // This is needed! Without this variable, testTimer may not be
    // reset to initial value (very rarely, but could happen)
    var testTimerRefresh by remember {
        mutableStateOf(false)
    }

    val currentWordNumber = data.currentWordNumber + 1
    val totalWordsNumber = data.words.size

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "${currentWordNumber}/${totalWordsNumber}",
            modifier = Modifier.weight(0.5f),
            style = MaterialTheme.typography.titleMedium,
        )

        BasicTimer(
            totalTimeInMillis = testTimer,
            refresh = testTimerRefresh,
            modifier = Modifier.weight(0.5f)
        ) {
            if (!testTimerRefresh) {
                testTimer = it - 100L
            } else {
                testTimerRefresh = false
            }
        }
    }

    FlashcardPractice(
        data = data,
        allowFlashcardHint = false,
        textFieldEnabled = testTimer > 0,
        actions = actions
    ) {
        val timeTaken = initialTestTimer - testTimer

        testTimer = initialTestTimer
        testTimerRefresh = true

        actions.updateTestHistory(
            flashcardAnswer = FlashcardAnswer(
                answer = data.answer,
                word = data.words[data.currentWordNumber]
            ),
            timeTaken = timeTaken
        )
        actions.setNextWord()
    }
}

@Composable
fun FlashcardPracticeTraining(
    data: FlashcardPracticeScreenData,
    actions: FlashcardPracticeScreenActions
) {
    FlashcardPractice(
        data = data,
        textFieldSupportingText = stringResource(id = R.string.flashcard_hint),
        textFieldError = if (data.error != null)
            stringResource(id = data.error!!)
        else
            null,
        actions = actions
    ) {
        // User should answer correctly
        if (actions.isAnswerCorrect(data.answer)) {
            actions.setNextWord()
        }
    }
}

@Composable
fun FlashcardPractice(
    data: FlashcardPracticeScreenData,
    allowFlashcardHint: Boolean = true,
    textFieldSupportingText: String? = null,
    textFieldEnabled: Boolean = true,
    textFieldError: String? = null,
    actions: FlashcardPracticeScreenActions,
    onActionButtonClick: () -> Unit
) {
    Flashcard(text = data.flashcardText) {
        if (allowFlashcardHint) {
            actions.setFlashcardText()
        }
    }

    Spacer(modifier = Modifier.height(smallMargin()))

    BasicTextFieldElement(
        value = data.answer,
        onValueChange = { actions.setAnswer(it) },
        label = stringResource(id = R.string.answer_label),
        supportingText = textFieldSupportingText,
        enabled = textFieldEnabled,
        errorMessage = textFieldError
    )

    ElevatedButton(onClick = {
        onActionButtonClick()
    }) {
        Text(text = stringResource(id = R.string.next_label))
    }
}

@Composable
fun FlashcardPracticeResult(
    title: String,
    actions: FlashcardPracticeScreenActions,
    testHistory: TestHistory?
) {
    Text(
        color = MaterialTheme.colorScheme.primary,
        text = title.uppercase(),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(basicMargin()),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = mediumMargin())
    ) {
        Button(
            onClick = {
                actions.resetFlashcard()
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(id = R.string.repeat_label))
        }

        if (testHistory != null) {
            Button(
                onClick = {
                    actions.saveTestHistory()
                    actions.resetFlashcard()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.save_test_history_label))
            }
        }
    }

    if (testHistory != null) {
        TestSummary(testHistory = testHistory)
    }
}

@Composable
fun TestSummary(
    testHistory: TestHistory
) {
    val correctAnswers = testHistory.answers.count {
        it.answer == it.word.translation
    }
    val allAnswers = testHistory.answers.count()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "${stringResource(id = R.string.number_of_correct_answers)}: ${correctAnswers}/${allAnswers}")
        Text(text = "${stringResource(id = R.string.date_of_completion)}: ${DateUtils.getDateString(testHistory.dateOfCompletion)}")
        Text(
            text = "${stringResource(id = R.string.time_taken)}: " +
                    "${testHistory.timeTaken / 1000L} " +
                    stringResource(id = R.string.seconds)
        )

        Divider(modifier = Modifier.padding(vertical = basicMargin()))

        LazyColumn {
            testHistory.answers.forEach { answer ->
                item {
                    TestSummaryColumn(answer = answer)
                }
            }
        }
    }
}

@Composable
fun TestSummaryColumn(
    answer: FlashcardAnswer
) {
    val isAnswerCorrect = answer.answer == answer.word.translation
    val answerColor = if (isAnswerCorrect) Color.Green else Color.Red
    val answerIcon = if (isAnswerCorrect) Icons.Default.Done else Icons.Default.Close

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = basicMargin())
    ) {
        Text(text = "${stringResource(id = R.string.word)}: ${answer.word.name}")
        Text(text = "${stringResource(id = R.string.correct_answer)}: ${answer.word.translation}")

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${stringResource(id = R.string.your_answer)}: ${answer.answer}",
                color = answerColor
            )

            Spacer(modifier = Modifier.padding(horizontal = smallMargin()))

            Icon(
                imageVector = answerIcon,
                tint = answerColor,
                contentDescription = null,
                modifier = Modifier.size(basicMargin())
            )
        }
    }
}