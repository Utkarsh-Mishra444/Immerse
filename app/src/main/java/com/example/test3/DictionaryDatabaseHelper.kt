package com.example.test3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DictionaryDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "jmdict.db"
        private const val DATABASE_VERSION = 1
    }

    init {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        if (!dbFile.exists()) {
            dbFile.parentFile?.mkdirs()
            copyDatabaseFile(context, dbFile)
        }
    }

    private fun copyDatabaseFile(context: Context , dbFile: File) {
        try {
            context.resources.openRawResource(R.raw.jmdict).use { inputStream ->
                FileOutputStream(dbFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: IOException) {
            throw RuntimeException("Error copying resource database to local storage", e)
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Not needed as database is pre-populated
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Not needed as database is pre-populated
    }


    // Inside DictionaryDatabaseHelper.kt
    fun databaseExists(context: Context): Boolean {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        return dbFile.exists()
    }

    fun isDatabaseNotEmpty(): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT EXISTS(SELECT 1 FROM words LIMIT 1)", null)
        var isNotEmpty = false
        if (cursor.moveToFirst()) {
            isNotEmpty = cursor.getInt(0) == 1
        }
        cursor.close()
        //db.close()
        return isNotEmpty
    }
    fun searchJapaneseToEnglish(searchTerm: String): List<SearchResult> {
        val db = this.readableDatabase

        val cursor = db.rawQuery("""
            SELECT w.id, k.kanji, k2.kana, s.gloss, k.common AS k_common, k2.common AS k2_common, s.misc
            FROM words w
            LEFT JOIN kanji k ON w.id = k.word_id
            LEFT JOIN kana k2 ON w.id = k2.word_id
            JOIN senses s ON w.id = s.word_id
            WHERE k.kanji LIKE ? OR k2.kana LIKE ?
            ORDER BY
                CASE
                    WHEN k.kanji = ? OR k2.kana = ? THEN 1
                    WHEN k.kanji LIKE ? OR k2.kana LIKE ? THEN 2
                    ELSE 3
                END,
                CASE
                    WHEN k.common = 1 OR k2.common = 1 THEN 1
                    ELSE 2
                END,
                LENGTH(k.kanji), LENGTH(k2.kana), s.misc DESC
        """, arrayOf("%$searchTerm%", "%$searchTerm%", searchTerm, searchTerm, "%$searchTerm%", "%$searchTerm%"))

        val results = mutableListOf<SearchResult>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val kanji = cursor.getString(cursor.getColumnIndexOrThrow("kanji"))
            val kana = cursor.getString(cursor.getColumnIndexOrThrow("kana"))
            val gloss = cursor.getString(cursor.getColumnIndexOrThrow("gloss"))
            val kCommon = cursor.getInt(cursor.getColumnIndexOrThrow("k_common")) > 0
            val k2Common = cursor.getInt(cursor.getColumnIndexOrThrow("k2_common")) > 0
            val misc = cursor.getString(cursor.getColumnIndexOrThrow("misc"))

            results.add(SearchResult(id, kanji, kana, gloss, kCommon, k2Common, misc))
        }
        cursor.close()
        //db.close()

        return results
    }



    private fun Sense.toContentValues(): ContentValues = ContentValues().apply {
        put("field", field.joinToString(","))
        put("dialect", dialect.joinToString(","))
        put("misc", misc.joinToString(","))
        put("info", info.joinToString(","))
        put("language_source", languageSource.joinToString(","))
        put("gloss", gloss)
        put("related", related.joinToString(","))
        put("antonym", antonym.joinToString(","))
        put("tags", tags.joinToString(","))
    }
}


class DictionarySearchViewModel(private val context: Context) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults

    private val dbHelper = DictionaryDatabaseHelper(context)

    fun performSearch(query: String) {
        viewModelScope.launch {
            _searchResults.value = emptyList()
            val results = withContext(Dispatchers.IO) {
                dbHelper.searchJapaneseToEnglish(query)
            }
            _searchResults.value = results
        }
    }

    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }
}

class DictionarySearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionarySearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DictionarySearchViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}