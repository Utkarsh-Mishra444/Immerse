package com.example.test3

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.atilika.kuromoji.ipadic.Tokenizer

class SubtitleData(context: Context?) {
    internal val dbHelper = SubtitleDatabaseHelper(context)

    // Method to process and store subtitles
    fun processAndStoreSubtitles(url: String?, videoName: String?, subtitles: String) {
        // Add the subtitle file to the database and retrieve the file_id
        val fileId = dbHelper.addSubtitleFile(url, videoName)

        // Split subtitles into separate entries based on blank lines
        val lines = subtitles.split("\n\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val tokenizer = Tokenizer()

        // 1. Tokenization and Data Processing
        val wordTimestamps: MutableList<WordTimestamp> = ArrayList() // Store tokenized data
        for (line in lines) {
            val parts = line.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (parts.size < 3) continue  // Skip malformed entries

            val start = convertTimestampToSeconds(
                parts[1].split(" --> ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0].replace(",", "."))
            val text = parts[2]
            for (token in tokenizer.tokenize(text)) {
                val baseForm = token.baseForm
                wordTimestamps.add(WordTimestamp(fileId, baseForm, start))
            }
        }

        // 2. Database Insertion (Use batching here)
        // Get writable database
        dbHelper.addWordTimestampsBatch(wordTimestamps) // Pass the list to the helper


    }

    // Helper class to store tokenized data
    // Make WordTimestamp public
    public class WordTimestamp(val fileId: Long, val word: String, val timestamp: Int)


    // Convert SRT timestamp to seconds
    private fun convertTimestampToSeconds(timestamp: String): Int {
        val parts = timestamp.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        val seconds = parts[2].toDouble().toInt()
        return hours * 3600 + minutes * 60 + seconds
    }

    // Method to retrieve all timestamps for a specific word
    fun getTimestampsForWord(word: String?): Cursor {
        return dbHelper.getWordTimestamps(word!!)
    }
}
