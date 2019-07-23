package pl.starter.android.di

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import pl.starter.android.BuildConfig
import pl.starter.android.base.BaseApp
import pl.starter.android.utils.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ProtocolException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class AppModule(private val app: BaseApp) {

    @Singleton
    @Provides
    fun provideBaseSchedulers(): BaseSchedulers = BaseSchedulersImpl()

    @Provides
    fun provideApp() = app

    @Provides
    fun provideContext(): Context = app

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()!!

//    @Provides
//    @Singleton
//    fun provideTokenInterceptor(sessionsRepository: SessionRepository) = TokenInterceptor(sessionsRepository)

    @Singleton
    @Provides
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }


    @Singleton
    @Provides
    fun provideOkHttp(
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpBuilder = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)


        okHttpBuilder.addNetworkInterceptor { chain ->
            try {
                chain.proceed(chain.request())
            } catch (e: ProtocolException) {
                Response.Builder()
                    .request(chain.request())
                    .code(204)
                    .protocol(Protocol.HTTP_1_1)
                    .message("")
                    .body(ResponseBody.create(null, ""))
                    .build()
            }
        }
        okHttpBuilder.addNetworkInterceptor(tokenInterceptor)
        if (BuildConfig.DEBUG) {
            okHttpBuilder.addNetworkInterceptor(httpLoggingInterceptor)
        }


        return okHttpBuilder.build()
    }


    @Singleton
    fun provideSharedPreferences(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)!!


    @Provides
    fun provideStringProvider(context: Context): StringProvider = ContextStringProvider(context)

    @Provides
    fun provideErrorHandler(stringProvider: StringProvider): ErrorHandler = ErrorHandlerImpl(
        stringProvider
    )


}
