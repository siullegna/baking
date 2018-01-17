package com.hap.baking.dagger.module;

import android.content.Context;

import com.hap.baking.dagger.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by luis on 12/18/17.
 */
@Module
public class ContextModule {
    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationScope
    protected Context provideContext() {
        return context;
    }
}
