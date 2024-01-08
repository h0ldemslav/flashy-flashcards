package cz.mendelu.pef.flashyflashcards.mlkit

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import cz.mendelu.pef.flashyflashcards.extensions.getTitleCase
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

class LanguageCodesException(message: String): Exception(message)

class MLKitTranslateManager {

    private var codesToLanguages: Map<String, String> = mapOf()
    private var sourceLanguageCode: String? = null
    private var targetLanguageCode: String? = null
    private var translator: Translator? = null

    init {
        setCodesToLanguages()
    }

    fun setTranslator(
        onDownloadSuccess: (() -> Unit)?,
        onDownloadFailure: (() -> Unit)?
    ) {
        if (sourceLanguageCode == null || targetLanguageCode == null) {
            throw LanguageCodesException("Source and target language codes cannot be null")
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguageCode!!)
            .setTargetLanguage(targetLanguageCode!!)
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

    fun getDownloadedCodesToLanguages(
        onDownloadSuccess: (Map<String, String>) -> Unit,
        onDownloadFailure: (Exception) -> Unit
    ) {
        val modelManager = RemoteModelManager.getInstance()

        modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { models ->
                val downloadedCodesToLanguages = mutableMapOf<String, String>()

                models.forEach {
                    val languageCode = it.language
                    val languageFullName = codesToLanguages[languageCode]

                    // English is a built-in model and you cannot delete it
                    if (languageFullName != null && languageCode != TranslateLanguage.ENGLISH) {
                        downloadedCodesToLanguages[languageCode] = languageFullName
                    }
                }

                onDownloadSuccess(downloadedCodesToLanguages.toMap())
            }
            .addOnFailureListener { exception ->
                onDownloadFailure(exception)
            }
    }

    fun deleteTranslateModel(
        code: String,
        onSuccessDelete: () -> Unit,
        onFailureDelete: (Exception) -> Unit
    ) {
        val isLanguageCodeValid = codesToLanguages.keys.contains(code)

        if (isLanguageCodeValid) {
            val modelManager = RemoteModelManager.getInstance()
            val model = TranslateRemoteModel.Builder(code).build()

            modelManager.deleteDownloadedModel(model)
                .addOnSuccessListener {
                    onSuccessDelete()
                }
                .addOnFailureListener { exception ->
                    onFailureDelete(exception)
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

    fun setSourceAndTargetLanguageCodes(sourceLanguage: String?, targetLanguage: String?) {
        sourceLanguageCode = codesToLanguages.keys.find { codesToLanguages[it] == sourceLanguage }
        targetLanguageCode = codesToLanguages.keys.find { codesToLanguages[it] == targetLanguage }
    }

    fun getCurrentSourceAndTargetLanguageCodes(): Pair<String?, String?> {
        return Pair(sourceLanguageCode, targetLanguageCode)
    }

    fun getSourceAndTargetLanguageNames(
        sourceCode: String?,
        targetCode: String?
    ): Pair<String, String>? {
        if (sourceCode == null || targetCode == null) return null

        return Pair(codesToLanguages[sourceCode]!!, codesToLanguages[targetCode]!!)
    }

    fun getAllAvailableCodesToLanguages(): Map<String, String> {
        return codesToLanguages
    }

    private fun setCodesToLanguages() {
        val languageCodes = TranslateLanguage.getAllLanguages()
        val codesToFullNames = mutableMapOf<String, String>()
        val members = TranslateLanguage::class.java.fields

        members.forEach { field ->
            try {
                val fieldName = field.name.getTitleCase()
                val fieldValue = field.get(null)

                if (languageCodes.contains(fieldValue)) {
                    if (fieldValue != null) {
                        codesToFullNames[fieldValue.toString()] = fieldName
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

        codesToLanguages = codesToFullNames.toMap()
    }
}