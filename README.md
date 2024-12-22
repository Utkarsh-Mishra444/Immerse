# Immerse: Japanese Language Learning App

Immerse is an Android application that helps you learn Japanese by providing contextual word usage examples directly from YouTube videos. By combining video transcripts, dictionary lookups, and advanced language processing, this app offers a dynamic and practical approach to language learning.

## Features

*   **Contextual Learning:** See how Japanese words are used in real-world video content.
*   **YouTube Integration:** Easily import YouTube video links for study.
*   **Transcript Processing:** Uses the `youtube-transcript-api` Python library to extract video dialogue.
*   **Word Lookup:** Access comprehensive word definitions via the integrated JMDICT dictionary.
    *   Supports searching in both Romaji and Japanese characters.
*   **Natural Language Processing:** Employs Kuromoji for accurate word base form processing, facilitating precise searches and support for word conjugations.
*   **Persistent Storage:** Processed video data (transcripts, timestamps, word base forms) is stored in a database for offline access, re-usability and efficient searches.
*   **Timestamped Links:** When searching for a word, the app provides a list of timestamps for the word to be used in the context of the video.
*   **Add Multiple Videos:** You can add as many videos as you want and search words within the context of each video.
*   **Light and Dark Mode Support:** Built with Material Design 3, the app automatically supports both light and dark modes.

## How It Works

1.  **Add a Video:** Paste a YouTube video link into the app. The app will then:
    *   Retrieve the video transcript using the `youtube-transcript-api`.
    *   Process the text using Kuromoji for accurate base form identification, so the app can find words regardless of their tense.
    *   Store the words and timestamp mapping in an internal database.
       

2.  **Search for a Word:** Enter a Japanese word you want to learn. The app will:
    *   Query the database and find all instances of the word within previously processed videos.
    *   Display the timestamps for the word to be used in the context of the video.
   
 
3.  **Study with Context:** Click a timestamp to jump to the exact moment in the YouTube video where the word is used.
       


## Technologies Used

*   **Android Native Development:** Kotlin, Jetpack Compose
*   **YouTube Transcript API:** `youtube-transcript-api` (Python library)
*   **Japanese Dictionary:** JMDICT (open-source)
*   **Natural Language Processing:** Kuromoji
*   **Python Integration:** Chaquopy (for `youtube-transcript-api` access and data processing)
*   **Database:** SQLite

## Why This App?

Traditional language learning resources often lack contextual examples, making it challenging to understand how words are used in real-life situations. This application fills this gap by leveraging YouTube videos and advanced language processing to provide a more immersive and effective language learning experience.

## Important Setup Information

### JMdict Dictionary

This application uses the JMdict Japanese-English dictionary data. You **must** download the latest version of the JMdict dictionary directly from the JMdict website: [http://www.edrdg.org/jmdict/j_jmdict.html](http://www.edrdg.org/jmdict/j_jmdict.html). 

**License:** The JMdict data is licensed under a Creative Commons Attribution-ShareAlike Licence (V4.0). You must attribute the work in the manner specified by the author or licensor. See:
    [https://creativecommons.org/licenses/by-sa/4.0/](https://creativecommons.org/licenses/by-sa/4.0/)

For more details about JMdict license, please see:
    [https://www.edrdg.org/edrdg/licence.html](https://www.edrdg.org/edrdg/licence.html)

The links to the JMdict project and KANJIDIC project as required by the license are as follows:
[https://www.edrdg.org/wiki/index.php/JMdict-EDICT_Dictionary_Project](https://www.edrdg.org/wiki/index.php/JMdict-EDICT_Dictionary_Project)
[https://www.edrdg.org/wiki/index.php/KANJIDIC_Project](https://www.edrdg.org/wiki/index.php/KANJIDIC_Project)

### Building and Running the App

To build and run the Immerse app:

1.  Open the project in Android Studio.
2.  Build the application.
3.  Run the application on an Android device or emulator.

## Note for Android Studio Users

This project was initially created in Android Studio with the project name "Test3". If you encounter any issues when downloading and opening the project, try renaming the project folder to "Test3".

## `youtube-transcript-api` License

This project utilizes the `youtube-transcript-api` Python library, which is licensed under the MIT License. You can find the full license text here:

   [https://opensource.org/licenses/MIT](https://opensource.org/licenses/MIT)
