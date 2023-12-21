package cz.mendelu.pef.flashyflashcards.database.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.mendelu.pef.flashyflashcards.model.FlashcardAnswer

class FlashcardAnswersConverter {

    private val gson = Gson()

    @TypeConverter
    fun toString(answers: List<FlashcardAnswer>): String? {
        return gson.toJson(answers)
    }

    @TypeConverter
    fun fromString(json: String?): List<FlashcardAnswer>? {
        val type = object : TypeToken<List<FlashcardAnswer>>() {}.type
        return gson.fromJson(json, type)
    }
}