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

class MLKitTranslator {

    private var languages: Map<String, String> = mapOf()
    private var sourceLang: String? = null
    private var targetLang: String? = null
    private var translator: Translator? = null

    init {
        setMapOfLanguages()
    }

    fun setTranslator(
        onDownloadSuccess: (() -> Unit)?,
        onDownloadFailure: (() -> Unit)?
    ) {
        if (sourceLang == null || targetLang == null) {
            // TODO: handle sourceLang and targetLang nullability
            // TODO: When using this method in viewmodels, don't forget to try/catch
            throw Exception()
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
                    "MLKitTranslator",
                    "Failed to download model: ${it.message}"
                )
            }
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

    fun releaseTranslator() {
        // According to MLKit docs: Closes the translator and releases its resources.
        translator?.close()
    }

    fun setLanguages(source: String, target: String) {
        sourceLang = languages[source]
        targetLang = languages[target]
    }

    fun getMapOfLanguages(): Map<String, String> {
        return languages
    }

    private fun setMapOfLanguages() {
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
                Log.e(null, "MLKit Translator Exception: ${e.message ?: "unknown exception"}")
            }
        }

        languages = fullNamesAndCodes.toMap()
    }
}