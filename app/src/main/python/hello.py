import youtube_transcript_api
from youtube_transcript_api import YouTubeTranscriptApi

def hello_world():
    return "Hello, World!"

def download_auto_generated_subtitles(youtube_url):
    language_code='ja'
    video_id = youtube_url.split('v=')[-1]  # Extract video ID from URL
    transcript = YouTubeTranscriptApi.get_transcript(video_id, languages=[language_code])

    # Convert transcript to SRT format
    subtitle_text = ""
    for i, entry in enumerate(transcript):
        start = entry['start']
        duration = entry['duration']
        end = start + duration
        text = entry['text']

        # Convert seconds to hh:mm:ss,ms format
        start_time = f"{int(start // 3600):02}:{int((start % 3600) // 60):02}:{int(start % 60):02},{int((start % 1) * 1000):03}"
        end_time = f"{int(end // 3600):02}:{int((end % 3600) // 60):02}:{int(end % 60):02},{int((end % 1) * 1000):03}"

        subtitle_text += f"{i+1}\n{start_time} --> {end_time}\n{text}\n\n"
    return subtitle_text

def get_video_title_from_url(youtube_url):
    # Extract video ID from URL
    video_id = youtube_url.split('v=')[-1]
    # Fetch video title using video ID
    try:
        video_title = YouTubeTranscriptApi.get_transcript(video_id)[0]['title']
        return video_title
    except Exception as e:
        # Handle error
        return None

