package task.assignment.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import task.assignment.model.BeerListResponse

interface ApiEndPoint {
    @GET("beers")
    suspend fun getBeerList(): Response<BeerListResponse>

    @GET("beers/{id}")
    suspend fun getBeerDetails(@Path("id") id: Int?): Response<BeerListResponse>

}