package com.vaibhavdhunde.app.elearning.ui.subtopic

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.FragmentSubtopicBinding
import com.vaibhavdhunde.app.elearning.util.EventObserver
import com.vaibhavdhunde.app.elearning.util.ViewModelFactory
import com.vaibhavdhunde.app.elearning.util.obtainViewModel
import com.vaibhavdhunde.app.elearning.util.snackbar
import kotlinx.android.synthetic.main.fragment_subtopic.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class SubtopicFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var subtopicViewModel: SubtopicViewModel

    private lateinit var binding: FragmentSubtopicBinding

    private var player: SimpleExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subtopic, container, false)

        subtopicViewModel = obtainViewModel(SubtopicViewModel::class.java, factory)

        binding = FragmentSubtopicBinding.bind(view).apply {
            viewmodel = subtopicViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRetry()
        setupEvents()
        loadSubtopic()
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            binding.viewmodel?.dataAvailable?.observe(viewLifecycleOwner, Observer { isAvailable ->
                if (isAvailable) {
                    initializePlayer()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || player == null) {
            binding.viewmodel?.dataAvailable?.observe(viewLifecycleOwner, Observer { isAvailable ->
                if (isAvailable) {
                    initializePlayer()
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun setupRetry() {
        btn_retry.setOnClickListener {
            loadSubtopic()
        }
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(viewLifecycleOwner, EventObserver { message ->
            fragment_subtopic.snackbar(message)
        })
    }

    private fun loadSubtopic() {
        binding.viewmodel?.loadSubtopic(getSubtopicId())
    }

    private fun getSubtopicId(): Long {
        return arguments!!.let {
            SubtopicFragmentArgs.fromBundle(it).subtopicId
        }
    }

    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(context)

        player_view.player = player

        binding.viewmodel?.let {
            val uri = Uri.parse(it.getSubtopic().url)
            val mediaSource = it.getMediaSource() ?: buildMediaSource(uri)
            player?.playWhenReady = it.getPlayWhenReady()
            player?.seekTo(it.getCurrentWindow(), it.getPlaybackPosition())
            player?.prepare(mediaSource, false, false)
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val userAgent = Util.getUserAgent(context, getString(R.string.app_name))

        val httpDataSourceFactory = DefaultHttpDataSourceFactory(
            userAgent,
            null,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true
        )

        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            null,
            httpDataSourceFactory
        )

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
        binding.viewmodel?.setMediaSource(mediaSource)
        return mediaSource
    }

    private fun releasePlayer() {
        player?.let { player ->
            binding.viewmodel?.let {
                it.setPlaybackPosition(player.currentPosition)
                it.setCurrentWindow(player.currentWindowIndex)
                it.setPlayWhenReady(player.playWhenReady)
            }
            player.release()
        }
        player = null
    }

}
