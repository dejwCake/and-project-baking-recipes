package sk.dejw.android.bakingrecipes.ui;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.bakingrecipes.R;
import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.models.RecipeStep;

public class RecipeStepFragment extends Fragment implements ExoPlayer.EventListener {

    public static final String RECIPE = "recipe";
    public static final String RECIPE_STEP_POSITION = "recipe_step_position";

    private static final String PLAYER_AUTO_PLAY = "auto_play";
    private static final String PLAYER_WINDOW = "window";
    private static final String PLAYER_POSITION = "position";

    private static final String TAG = RecipeStepFragment.class.getSimpleName();

    private Recipe mRecipe;
    private int mRecipeStepPosition;
    private RecipeStep mRecipeStep;
    private ArrayList<RecipeStep> mRecipeSteps;

    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.iv_thumbnail)
    ImageView mThumbnail;
    @BindView(R.id.tv_step_description)
    TextView mDescription;

    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private boolean mHasPlayer = false;

    private boolean mStartAutoPlay;
    private long mStartPosition;

    public RecipeStepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE);
            mRecipeStepPosition = savedInstanceState.getInt(RECIPE_STEP_POSITION);

            mStartAutoPlay = savedInstanceState.getBoolean(PLAYER_AUTO_PLAY);
            mStartPosition = savedInstanceState.getLong(PLAYER_POSITION);
        } else {
            clearStartPosition();
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);

        mRecipeSteps = new ArrayList<RecipeStep>(Arrays.asList(mRecipe.getSteps()));
        mRecipeStep = mRecipeSteps.get(mRecipeStepPosition);
        mDescription.setText(mRecipeStep.getDescription());
        if(!TextUtils.isEmpty(mRecipeStep.getThumbnailURL())) {
            Picasso.with(getContext())
                    .load(mRecipeStep.getThumbnailURL())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(mThumbnail);
        } else {
            mThumbnail.setVisibility(View.GONE);
        }

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.ic_launcher_foreground));

        if (!TextUtils.isEmpty(mRecipeStep.getVideoURL())) {
            initializeMediaSession();

            initializePlayer(Uri.parse(mRecipeStep.getVideoURL()));
            mHasPlayer = true;
            mPlayerView.setVisibility(View.VISIBLE);
        } else {
            mPlayerView.setVisibility(View.GONE);
        }

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    public void setRecipeStepPosition(int recipeStepPosition) {
        mRecipeStepPosition = recipeStepPosition;
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "BakingRecipes");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.seekTo(mStartPosition);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(mStartAutoPlay);
        }
    }

    private void releasePlayer() {
        if(mExoPlayer != null) {
            updateStartPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void updateStartPosition() {
        if (mExoPlayer != null) {
            mStartAutoPlay = mExoPlayer.getPlayWhenReady();
            mStartPosition = Math.max(0, mExoPlayer.getCurrentPosition());
        }
    }

    private void clearStartPosition() {
        mStartAutoPlay = true;
        mStartPosition = C.TIME_UNSET;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        updateStartPosition();
        currentState.putParcelable(RECIPE, mRecipe);
        currentState.putInt(RECIPE_STEP_POSITION, mRecipeStepPosition);

        currentState.putBoolean(PLAYER_AUTO_PLAY, mStartAutoPlay);
        currentState.putLong(PLAYER_POSITION, mStartPosition);

        super.onSaveInstanceState(currentState);
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
    public void onDestroy() {
        super.onDestroy();
        if(mHasPlayer) {
            releasePlayer();
        }
        if(mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
