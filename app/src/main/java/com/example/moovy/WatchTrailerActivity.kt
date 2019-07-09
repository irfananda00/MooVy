package com.example.moovy

import android.os.Bundle
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_watch_trailer.*

class WatchTrailerActivity : YouTubeBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_trailer)
        vidTrailer.initialize("AIzaSyDyUfUbu0C4XEptSFdbxq8Pb9dm5GfXtJA",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {

                    // do any work here to cue video, play video, etc.
                    youTubePlayer.cueVideo(intent.getStringExtra("trailer_id"))
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {

                }
            })
    }
}
