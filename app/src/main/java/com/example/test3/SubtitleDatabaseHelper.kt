package com.example.test3

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.test3.SubtitleData.WordTimestamp
import com.moji4j.MojiConverter


class SubtitleDatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_SUBTITLE_FILES_TABLE = ("CREATE TABLE " + TABLE_SUBTITLE_FILES + "("
                + COLUMN_FILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_URL + " TEXT," // **Add a comma here**
                + COLUMN_VIDEO_NAME + " TEXT)")
        db.execSQL(CREATE_SUBTITLE_FILES_TABLE)

        val CREATE_WORD_TIMESTAMPS_TABLE = ("CREATE TABLE " + TABLE_WORD_TIMESTAMPS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FILE_ID + " INTEGER,"
                + COLUMN_WORD + " TEXT,"
                + COLUMN_TIMESTAMP + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_FILE_ID + ") REFERENCES " + TABLE_SUBTITLE_FILES + "(" + COLUMN_FILE_ID + "))")
        db.execSQL(CREATE_WORD_TIMESTAMPS_TABLE)

        // Optimization Code
        db.execSQL("CREATE INDEX word_timestamps_file_id ON word_timestamps (file_id)")
        db.execSQL("CREATE INDEX subtitle_files_file_id ON subtitle_files (file_id)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD_TIMESTAMPS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBTITLE_FILES)
        onCreate(db)
    }

    fun addSubtitleFile(url: String?, videoName: String?): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_URL, url)
        values.put(COLUMN_VIDEO_NAME, videoName) // Store video name
        val fileId = db.insert(TABLE_SUBTITLE_FILES, null, values)
        db.close()
        return fileId
    }


    fun addWordTimestamp(fileId: Int, word: String?, timestamp: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_FILE_ID, fileId)
        values.put(COLUMN_WORD, word)
        values.put(COLUMN_TIMESTAMP, timestamp)
        db.insert(TABLE_WORD_TIMESTAMPS, null, values)
        db.close()
    }

    fun addWordTimestampsBatch(wordTimestamps: List<WordTimestamp>) {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            for (wordTimestamp in wordTimestamps) {
                val values = ContentValues()
                values.put(COLUMN_FILE_ID, wordTimestamp.fileId)
                values.put(COLUMN_WORD, wordTimestamp.word)
                values.put(COLUMN_TIMESTAMP, wordTimestamp.timestamp)
                db.insert(TABLE_WORD_TIMESTAMPS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun getWordTimestamps(word: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT " + TABLE_SUBTITLE_FILES + "." + COLUMN_URL + ", " +
                    TABLE_WORD_TIMESTAMPS + "." + COLUMN_TIMESTAMP +
                    " FROM " + TABLE_WORD_TIMESTAMPS +
                    " JOIN " + TABLE_SUBTITLE_FILES +
                    " ON " + TABLE_WORD_TIMESTAMPS + "." + COLUMN_FILE_ID + " = " +
                    TABLE_SUBTITLE_FILES + "." + COLUMN_FILE_ID +
                    " WHERE " + TABLE_WORD_TIMESTAMPS + "." + COLUMN_WORD + " = ?",
            arrayOf(word)
        )
    }
    // Method to get timestamps for a word across all subtitle files
    fun getWordTimestampsForAllFiles(word: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT " + TABLE_SUBTITLE_FILES + "." + COLUMN_URL + ", " +
                    TABLE_WORD_TIMESTAMPS + "." + COLUMN_TIMESTAMP +
                    " FROM " + TABLE_WORD_TIMESTAMPS +
                    " JOIN " + TABLE_SUBTITLE_FILES +
                    " ON " + TABLE_WORD_TIMESTAMPS + "." + COLUMN_FILE_ID + " = " +
                    TABLE_SUBTITLE_FILES + "." + COLUMN_FILE_ID +
                    " WHERE " + TABLE_WORD_TIMESTAMPS + "." + COLUMN_WORD + " = ?",
            arrayOf(word)
        )
    }


    //Method to do search for both original and base form of the word
    fun getWordTimestampsForAllFilesMulti(word: String): Cursor {
        Log.d("DatabaseDebug", "About to query combined_results")
        val converter = MojiConverter()
        val db = this.readableDatabase
        val processedWord = converter.convertRomajiToHiragana(word)
        db.execSQL("DROP TABLE IF EXISTS combined_results")
        // Step 1: Create a temporary table
            db.execSQL("CREATE TEMP TABLE IF NOT EXISTS combined_results (url TEXT, timestamp INTEGER)")

            // Step 2: Insert results from the first query
            db.execSQL(
                "INSERT INTO combined_results (url, timestamp) " +
                        "SELECT " + TABLE_SUBTITLE_FILES + "." + COLUMN_URL + ", " +
                        TABLE_WORD_TIMESTAMPS + "." + COLUMN_TIMESTAMP +
                        " FROM " + TABLE_WORD_TIMESTAMPS +
                        " JOIN " + TABLE_SUBTITLE_FILES +
                        " ON " + TABLE_WORD_TIMESTAMPS + "." + COLUMN_FILE_ID + " = " +
                        TABLE_SUBTITLE_FILES + "." + COLUMN_FILE_ID +
                        " WHERE " + TABLE_WORD_TIMESTAMPS + "." + COLUMN_WORD + " = ?",
                arrayOf(word)
            )

            // Step 3: Insert results from the second query
            db.execSQL(
                "INSERT INTO combined_results (url, timestamp) " +
                        "SELECT " + TABLE_SUBTITLE_FILES + "." + COLUMN_URL + ", " +
                        TABLE_WORD_TIMESTAMPS + "." + COLUMN_TIMESTAMP +
                        " FROM " + TABLE_WORD_TIMESTAMPS +
                        " JOIN " + TABLE_SUBTITLE_FILES +
                        " ON " + TABLE_WORD_TIMESTAMPS + "." + COLUMN_FILE_ID + " = " +
                        TABLE_SUBTITLE_FILES + "." + COLUMN_FILE_ID +
                        " WHERE " + TABLE_WORD_TIMESTAMPS + "." + COLUMN_WORD + " = ?",
                arrayOf(processedWord)
            )

            // Step 4 & 5: Query the temporary table and return the combined Cursor
            val combinedCursor = db.rawQuery("SELECT * FROM combined_results", null)

            // Step 6: Optionally, drop the temporary table if no longer needed
            // db.execSQL("DROP TABLE IF EXISTS combined_results")
        Log.d("DatabaseDebug", "About to query combined_results")
        if (combinedCursor != null && combinedCursor.moveToFirst()) {
            do {
                val url = combinedCursor.getString(combinedCursor.getColumnIndexOrThrow("url"))
                val timestamp = combinedCursor.getInt(combinedCursor.getColumnIndexOrThrow("timestamp"))
                Log.d("CombinedCursorLog", "URL: $url, Timestamp: $timestamp")
            } while (combinedCursor.moveToNext())

            // Move the cursor back to the first position after logging its contents
            combinedCursor.moveToFirst()
        }
        Log.d("DatabaseDebug", "Finished querying combined_results")
            return combinedCursor
        }




    @SuppressLint("Range")
    fun getVideoNameByUrl(url: String?): String? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_SUBTITLE_FILES, // Corrected table name
            arrayOf(COLUMN_VIDEO_NAME), // columns to return
            "$COLUMN_URL = ?", // columns for the WHERE clause
            arrayOf(url), // values for the WHERE clause
            null, // don't group the rows
            null, // don't filter by row groups
            null // don't order the rows
        )

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_NAME))
        } else {
            return null
        }
    }
    companion object {
        private val DATABASE_NAME = "Subtitles.db"
        private val DATABASE_VERSION = 3

        val TABLE_SUBTITLE_FILES = "subtitle_files"
        val COLUMN_FILE_ID = "file_id"
        val COLUMN_URL = "url"
        val COLUMN_VIDEO_NAME = "video_name" // New column for video name

        val TABLE_WORD_TIMESTAMPS = "word_timestamps"
        val COLUMN_ID = "id"
        val COLUMN_WORD = "word"
        val COLUMN_TIMESTAMP = "timestamp"
    }
}