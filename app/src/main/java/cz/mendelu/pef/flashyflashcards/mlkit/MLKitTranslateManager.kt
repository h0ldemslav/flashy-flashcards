package cz.mendelu.pef.flashyflashcards.mlkit

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import cz.mendelu.pef.flashyflashcards.extensions.getTitleCase
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

class TranslateSourceTargetLanguagesException(message: String): Exception(message)

class MLKitTranslateManager {

    private var languages: Map<String, String> = mapOf()
    private var sourceLang: String? = null
    private var targetLang: String? = null
    private var translator: Translator? = null

    init {
        createMapOfAvailableLanguages()
    }

    fun setTranslator(
        onDownloadSuccess: (() -> Unit)?,
        onDownloadFailure: (() -> Unit)?
    ) {
        if (sourceLang == null || targetLang == null) {
            throw TranslateSourceTargetLanguagesException("Source and target languages cannot be null.")
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang!!)
            .setTargetLanguage(targetLang!!)
            .build()

        translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator!!.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                if (onDownloadSuccess != null) {
                    onDownloadSuccess()
                }
            }
            .addOnFailureListener {
                if (onDownloadFailure != null) {
                    onDownloadFailure()
                }

                Log.e(
                    "MLKitTranslateManager",
                    "Failed to download model: ${it.message}"
                )
            }
    }

    /**
     * Resets [Translator] instance to null.
     */
    fun resetTranslator() {
        translator = null
    }

    fun translate(
        text: String,
        onSuccessTranslate: (String) -> Unit,
        onFailureTranslate: (Exception) -> Unit
    ) {
        if (translator != null) {
            translator!!.translate(text)
                .addOnSuccessListener { translatedText ->
                    onSuccessTranslate(translatedText)
                }
                .addOnFailureListener { exception ->
                    onFailureTranslate(exception)
                }
        }
    }

    /**
    * Closes the translator and releases its resources.
    */
    fun closeTranslator() {
        translator?.close()
    }

    fun isTranslatorNull(): Boolean {
        return translator == null
    }

    fun setSourceAndTargetLanguages(source: String, target: String) {
        sourceLang = languages[source]
        targetLang = languages[target]
    }

    fun getSourceAndTargetLanguages(): Pair<String?, String?> {
        return Pair(sourceLang, targetLang)
    }

    fun getMapOfAvailableLanguages(): Map<String, String> {
        return languages
    }

    private fun createMapOfAvailableLanguages() {
        val languageCodes = TranslateLanguage.getAllLanguages()
        val fullNamesAndCodes = mutableMapOf<String, String>()
        val members = TranslateLanguage::class.java.fields

        members.forEach { field ->
            try {
                val fieldName = field.name.getTitleCase()
                val fieldValue = field.get(null)

                if (languageCodes.contains(fieldValue)) {
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
                Log.e(null, "MLKitTranslateManager Exception: ${e.message ?: "unknown exception"}")
            }
        }

        languages = fullNamesAndCodes.toMap()
    }
}