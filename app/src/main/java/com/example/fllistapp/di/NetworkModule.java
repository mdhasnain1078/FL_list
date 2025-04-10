package com.example.fllistapp.di;

import android.app.Application;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.fllistapp.repository.InternshipRepository;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public static RequestQueue provideRequestQueue(@ApplicationContext Context context) {
        return Volley.newRequestQueue(context);
    }

    @Provides
    @Singleton
    public static InternshipRepository provideInternshipRepository(RequestQueue requestQueue) {
        return new InternshipRepository(requestQueue);
    }

    @Provides
    @Singleton
    public static Context provideContext(Application application) {
        return application.getApplicationContext();
    }

}
