package cz.mendelu.pef.flashyflashcards.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

// Thanks to Gabriele Mariotti
// https://stackoverflow.com/questions/68924018/jetpack-compose-scroll-to-bottom-listener-end-of-list/74677843#74677843

@Composable
fun LazyListState.isAtBottom(): Boolean {
    return remember(this) {
        derivedStateOf {
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount &&
                        lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
            }
        }
    }.value
}
