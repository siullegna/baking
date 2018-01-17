package com.hap.baking.section.step;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;
import com.hap.baking.BakingApplication;
import com.hap.baking.R;
import com.hap.baking.db.room.entity.Step;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends BaseVideoFragment implements View.OnClickListener {
    private static final String TAG = VideoFragment.class.getName();

    private static final String EXTRA_STEP_KEY = "com.hap.baking.fragment.EXTRA_STEP_KEY";
    private static final String EXTRA_RESUME_WINDOW_KEY = "com.hap.baking.fragment.EXTRA_RESUME_WINDOW_KEY";
    private static final String EXTRA_RESUME_POSITION_KEY = "com.hap.baking.fragment.EXTRA_RESUME_POSITION_KEY";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    @BindView(R.id.video)
    SimpleExoPlayerView video;
    @BindView(R.id.retry_container)
    View retryContainer;
    @BindView(R.id.retry_icon)
    ImageView retryIcon;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.step_detail)
    TextView stepDetail;
    @BindView(R.id.video_not_available)
    View videoNotAvailable;

    private Step step;
    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private boolean inErrorState;
    private boolean shouldAutoPlay;
    private int resumeWindow;
    private long resumePosition;

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param step information about the step.
     * @return A new instance of fragment VideoFragment.
     */
    public static VideoFragment newInstance(final Step step) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STEP_KEY, step);
        fragment.setArguments(args);
        return fragment;
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private Uri getVideoUri() throws NoVideoAvailableException {
        if (TextUtils.isEmpty(step.getVideoURL())) {
            throw new NoVideoAvailableException("Video is not available");
        }
        return Uri.parse(step.getVideoURL());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shouldAutoPlay = true;
        clearResumePosition();
        mediaDataSourceFactory = (BakingApplication.getInstance()).buildDataSourceFactory(BANDWIDTH_METER);
        ;
        if (getArguments() != null) {
            step = getArguments().getParcelable(EXTRA_STEP_KEY);
        }
        if (savedInstanceState != null) {
            if (step == null) {
                step = savedInstanceState.getParcelable(EXTRA_STEP_KEY);
            }
            resumeWindow = savedInstanceState.getInt(EXTRA_RESUME_WINDOW_KEY);
            resumePosition = savedInstanceState.getLong(EXTRA_RESUME_POSITION_KEY);
        }
    }

    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        video.requestFocus();
        stepDetail.setText(step.getDescription());

        retryContainer.setOnClickListener(this);

        video.setVisibility(View.VISIBLE);
        retryContainer.setVisibility(View.VISIBLE);
        videoNotAvailable.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            initializePlayer();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && video != null && getContext() != null) {
            initializePlayer();
        } else if (video != null && getContext() != null) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(EXTRA_STEP_KEY, step);
        updateResumePosition();
        outState.putInt(EXTRA_RESUME_WINDOW_KEY, resumeWindow);
        outState.putLong(EXTRA_RESUME_POSITION_KEY, resumePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        if (retryIcon.getVisibility() == View.VISIBLE) {
            retryIcon.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
            initializePlayer();
        }
    }

    private void updateResumePosition() {
        if (player == null) {
            return;
        }
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = Math.max(0, player.getContentPosition());
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    private void initializePlayer() {
        if (!getUserVisibleHint()) {
            return;
        }

        try {
            final Uri videoUri = getVideoUri();
            boolean needNewPlayer = player == null;
            if (needNewPlayer) {
                TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
                trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);

                final DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());

                player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
                player.addListener(new PlayerEventListener());

                video.setPlayer(player);
                player.setPlayWhenReady(shouldAutoPlay);
            }

            final MediaSource mediaSource = new ExtractorMediaSource(videoUri, mediaDataSourceFactory, defaultExtractorsFactory, null, null);
            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                player.seekTo(resumeWindow, resumePosition);
            }
            player.prepare(mediaSource, !haveResumePosition, false);
            inErrorState = false;
            updateUI();
        } catch (NoVideoAvailableException e) {
            Log.e(TAG, e.getMessage());
            // show not available screen
            video.setVisibility(View.GONE);
            retryContainer.setVisibility(View.GONE);
            videoNotAvailable.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI() {
        retryContainer.setVisibility(inErrorState ? View.VISIBLE : View.GONE);
        retryIcon.setVisibility(inErrorState ? View.VISIBLE : View.GONE);
        loader.setVisibility(inErrorState ? View.GONE : View.VISIBLE);
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            updateResumePosition();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    private class PlayerEventListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            updateUI();
        }

        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
            if (inErrorState) {
                // This will only occur if the user has performed a seek whilst in the error state. Update
                // the resume position so that if the user then retries, playback will resume from the
                // position to which they seeked.
                updateResumePosition();
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            inErrorState = true;
            if (isBehindLiveWindow(e)) {
                clearResumePosition();
                initializePlayer();
            } else {
                updateResumePosition();
                updateUI();
            }
        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            updateUI();
        }
    }
}
