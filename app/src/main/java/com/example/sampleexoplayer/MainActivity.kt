package com.example.sampleexoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util

class MainActivity : AppCompatActivity(), Player.EventListener {

    private lateinit var player : SimpleExoPlayer
    private lateinit var playerView : PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        player = SimpleExoPlayer.Builder(this).build()
        playerView = findViewById(R.id.videoView)

        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "PPE"))
        // This is the MediaSource representing the media to be played.
        val uri = RawResourceDataSource.buildRawResourceUri(R.raw.tnf_gcv_u01ls02_01)
        val extractorsFactory = DefaultExtractorsFactory()
            .setMp3ExtractorFlags(Mp3Extractor.FLAG_ENABLE_CONSTANT_BITRATE_SEEKING)
            .setConstantBitrateSeekingEnabled(true)

        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory).createMediaSource(uri)

        playerView.player = player
        player.prepare(videoSource)
        player.playWhenReady = true

        player.addListener(object : Player.EventListener {

            override fun onPlayerError(error: ExoPlaybackException) {
                error.printStackTrace()
                when (error.type) {
                    ExoPlaybackException.TYPE_SOURCE -> Toast.makeText(this@MainActivity, error.sourceException.message, Toast.LENGTH_LONG).show()
                    ExoPlaybackException.TYPE_RENDERER -> Toast.makeText(this@MainActivity, error.rendererException.message, Toast.LENGTH_LONG).show()
                    ExoPlaybackException.TYPE_UNEXPECTED -> Toast.makeText(this@MainActivity, error.unexpectedException.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
