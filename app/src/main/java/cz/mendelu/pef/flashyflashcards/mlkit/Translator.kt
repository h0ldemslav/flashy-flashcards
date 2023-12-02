package cz.mendelu.pef.flashyflashcards.mlkit

import android.util.Log
import com.google.mlkit.nl.translate.TranslateLanguage
import cz.mendelu.pef.flashyflashcards.extensions.getTitleCase
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

class Translator {

    fun getListOfFullLanguageNames(): Map<String, String> {
        val languages = TranslateLanguage.getAllLanguages()
        val fullNamesAndCodes = mutableMapOf<String, String>()
        val members = TranslateLanguage::class.java.fields

        members.forEach { field ->
            try {
                val fieldName = field.name.getTitleCase()
                val fieldValue = field.get(null)

                if (languages.contains(fieldValue)) {
                    if (fieldValue != null) {
                        fullNamesAndCodes[fieldName] = fieldValue.toString()
                    }
                }
            } catch (e: IllegalAccessException) {
                Log.e(null, e.message, e)
            } catch (e: IllegalArgumentException) {
                Log.e(null, e.message, e)
            } catch (e: NullPointerException) {
                Log.e(null, e.message, e)
            } catch (e: ExceptionInInitializerError) {
                Log.e(null, e.message, e)
            } catch (e: Exception) {
                Log.e(null, "MLKitUtils Exception: ${e.message ?: "unknown exception"}")
            }
        }

        return fullNamesAndCodes.toMap()
    }
}