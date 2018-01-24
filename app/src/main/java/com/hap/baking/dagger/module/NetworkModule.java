package com.hap.baking.dagger.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.hap.baking.dagger.scope.ApplicationScope;
import com.hap.baking.network.RecipeRestApi;
import com.hap.baking.network.RecipeRestService;
import com.hap.baking.util.RecipeSettings;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by luis on 12/4/17.
 */
@Module
public class NetworkModule {
    private <T> T getObservableAdapter(final OkHttpClient okHttpClient, final Class<T> clazz) {
        return new Retrofit.Builder()
                .baseUrl(RecipeSettings.getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(clazz);
    }

    @Provides
    @ApplicationScope
    protected OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addNetworkInterceptor(new StethoInterceptor());
        return builder.build();
    }

    @Provides
    @ApplicationScope
    protected RecipeRestService provideMovieRestService(final OkHttpClient okHttpClient) {
        final RecipeRestApi recipeRestApi = getObservableAdapter(okHttpClient, RecipeRestApi.class);
        return new RecipeRestService(recipeRestApi);
    }
}
