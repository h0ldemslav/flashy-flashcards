package cz.mendelu.pef.flashyflashcards.navigation.graphs

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class SettingsNavGraph(
    val start: Boolean = false
)
