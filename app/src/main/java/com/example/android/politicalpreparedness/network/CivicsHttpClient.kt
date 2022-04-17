package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient

class CivicsHttpClient : OkHttpClient() {

    companion object {

//<<<<<<< Updated upstream
        const val API_KEY = "//Add API key here" //TO DO: Place your API Key Here"
//=======
     //   const val API_KEY = "Add you API KEY" //TO DO: Place your API Key Here
//>>>>>>> Stashed changes

        fun getClient(): OkHttpClient {
            return Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original
                                .url()
                                .newBuilder()
                                .addQueryParameter("key", API_KEY)
                                .build()
                        val request = original
                                .newBuilder()
                                .url(url)
                                .build()
                        chain.proceed(request)
                    }
                    .build()
        }
    }
}