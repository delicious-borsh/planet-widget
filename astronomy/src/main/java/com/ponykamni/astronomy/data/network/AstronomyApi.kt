package com.ponykamni.astronomy.data.network

import com.ponykamni.astronomy.data.dto.AutogeneratedDistanceDataDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface AstronomyApi {

    @GET("/api/v2/bodies/positions/{planet}")
    suspend fun getPlanetPosition(
        @Path("planet") planet: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("elevation") elevation: String,
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("time") time: String,
    ): AutogeneratedDistanceDataDto
}