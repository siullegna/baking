package com.hap.baking;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.hap.baking.dagger.component.BakingAppComponent;
import com.hap.baking.dagger.component.DaggerBakingAppComponent;
import com.hap.baking.dagger.module.ContextModule;
import com.hap.baking.dagger.module.NetworkModule;
import com.hap.baking.util.SessionPreferences;

/**
 * Created by luis on 12/4/17.
 */

public class BakingApplication extends Application {
    private static BakingApplication INSTANCE;
    private BakingAppComponent bakingAppComponent;
    protected String userAgent;
    private static SessionPreferences sessionPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        userAgent = Util.getUserAgent(this, "ExoPlayerDemo");

        BakingApplication.INSTANCE = this;
        BakingApplication.sessionPreferences = new SessionPreferences(this);

        bakingAppComponent = DaggerBakingAppComponent.builder()
                .networkModule(new NetworkModule())
                .contextModule(new ContextModule(this))
                .build();

        Stetho.initializeWithDefaults(this);
    }

    public BakingAppComponent getBakingAppComponent() {
        return bakingAppComponent;
    }

    public static BakingApplication getInstance() {
        return (BakingApplication) BakingApplication.INSTANCE.getApplicationContext();
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public static SessionPreferences getSessionPreferences() {
        return sessionPreferences;
    }
}
