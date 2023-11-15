package cz.mendelu.pef.flashyflashcards.extensions

import cz.mendelu.pef.flashyflashcards.R
import kotlin.math.floor

fun String.getImageStarsResFromFloat(): Int {
    val stars: Map<Float, Int> = mapOf(
        0f to R.drawable.stars_small_0,
        1f to R.drawable.stars_small_1,
        1.5f to R.drawable.stars_small_1_half,
        2f to R.drawable.stars_small_2,
        2.5f to R.drawable.stars_small_2_half,
        3f to R.drawable.stars_small_3,
        3.5f to R.drawable.stars_small_3_half,
        4f to R.drawable.stars_small_4,
        4.5f to R.drawable.stars_small_4_half,
        5f to R.drawable.stars_small_5
    )

    val rating = this.toFloatOrNull() ?: return stars[0f]!!
    val decimalPart = rating % 1
    val key = if (rating > 0 && decimalPart >= 0.5f) {
        // `rating` could be for instance 0.7, then key will be 0.5, where is no image for 0.5,
        // thus there is a check `rating > 0` for handling zeros with decimal part
        floor(rating) + 0.5f
    } else {
        floor(rating)
    }

    return stars[key] ?: stars[0f]!!
}