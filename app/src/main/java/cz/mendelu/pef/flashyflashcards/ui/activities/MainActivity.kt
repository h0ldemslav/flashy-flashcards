package cz.mendelu.pef.flashyflashcards.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cz.mendelu.pef.flashyflashcards.navigation.DestinationsNavHostWrapper
import cz.mendelu.pef.flashyflashcards.ui.theme.FlashyFlashcardsTheme
import cz.mendelu.pef.flashyflashcards.utils.LocaleUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val mainActivityViewModel: MainActivityViewModel by viewModels()

        installSplashScreen().setKeepOnScreenCondition {
            mainActivityViewModel.isLoading.value
        }

        super.onCreate(savedInstanceState)

        setContent {
            FlashyFlashcardsTheme(mainActivityViewModel.isDarkTheme.value) {
                LocaleUtils.setLocale(LocalContext.current, mainActivityViewModel.language.value)

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!mainActivityViewModel.isLoading.value) {
                        DestinationsNavHostWrapper(
                            navGraph = mainActivityViewModel.startNavGraph.value,
                        )
                    }
                }
            }
        }
    }
}