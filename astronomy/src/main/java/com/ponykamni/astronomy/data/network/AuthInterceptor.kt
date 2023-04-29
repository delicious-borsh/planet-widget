package com.ponykamni.astronomy.data.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

internal class AuthInterceptor : Interceptor {

    private val auth = Credentials.basic(API_APP_ID, API_SECRET)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val authenticatedRequest: Request = request.newBuilder()
            .header("Authorization", auth).build()
        return chain.proceed(authenticatedRequest)
    }
}


private const val API_APP_ID = "264ec829-5bcc-4b84-bed3-ba13eef431b0"
private const val API_SECRET =
    ""
