package com.example.test3


data class DictionaryData(
    val words: List<Word>
)

data class Word(
    val id: Int,
    val kanji: List<Kanji>?,
    val kana: List<Kana>?,
    val sense: List<Sense>?
)

data class Kanji(
    val text: String,
    val common: Boolean
)

data class Kana(
    val text: String,
    val common: Boolean
)

data class Sense(
    val field: List<String>,
    val dialect: List<String>,
    val misc: List<String>,
    val info: List<String>,
    val languageSource: List<String>,
    val gloss: String,
    val related: List<String>,
    val antonym: List<String>,
    val tags: List<String>


)

data class SearchResult(
    val id: Int,
    val kanji: String?,
    val kana: String?,
    val gloss: String,
    val kanjiCommon: Boolean,
    val kanaCommon: Boolean,
    val misc: String?
)

