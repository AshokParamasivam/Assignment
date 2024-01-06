package task.assignment.network

class BeerRepository {

    suspend fun getBeerList() =
        RetrofitClient.getRetrofitInstance().create(ApiEndPoint::class.java).getBeerList()

    suspend fun getBeerDetails(id: Int) =
        RetrofitClient.getRetrofitInstance().create(ApiEndPoint::class.java).getBeerDetails(id)

}