package task.assignment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import task.assignment.model.BeerListResponseItem
import task.assignment.network.BeerRepository

class BeerViewModel(private val beerRepository: BeerRepository) : ViewModel() {

    private val _beerListData = MutableLiveData<ArrayList<BeerListResponseItem>>()
    val beerListData: LiveData<ArrayList<BeerListResponseItem>> get() = _beerListData

    private val _beerDetails = MutableLiveData<BeerListResponseItem>()
    val beerDetails: LiveData<BeerListResponseItem> get() = _beerDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var selectedBeerId = MutableLiveData(1)

    var errorMessage: String = ""

    var getBeerListJob: Job? = null
    var getBeerDetailsJob: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("Exception", "--- " + exception.message)
        onError("Error : ${exception.message} ")
    }

    fun getBeerList() {

        _isLoading.value = true
        _isError.value = false

        getBeerListJob = viewModelScope.launch(exceptionHandler) {
            val response = beerRepository.getBeerList()
            withContext(Dispatchers.Main) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    onError("Error : ${response.message()} ")
                } else {
                    val responseBody = response.body()
                    _beerListData.postValue(responseBody)
                }
            }
        }
    }


    fun getBeerDetails() {
        _isLoading.value = true
        _isError.value = false

        if (selectedBeerId.value != null) {
            getBeerDetailsJob = viewModelScope.launch (exceptionHandler){
                val response = beerRepository.getBeerDetails(selectedBeerId.value!!)
                val responseBody = response.body()
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    if (!response.isSuccessful || responseBody == null) {
                        onError("Error : ${response.message()} ")
                    } else {
                        _beerDetails.postValue(responseBody.first())
                    }
                }
            }
        }
    }


    private fun onError(inputMessage: String?) {

        val message =
            if (inputMessage.isNullOrBlank() or inputMessage.isNullOrEmpty()) "Unknown Error"
            else inputMessage

        errorMessage = StringBuilder("ERROR: ").append("$message").toString()

        _isError.value = true
        _isLoading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        getBeerDetailsJob?.cancel()
        getBeerListJob?.cancel()
    }

}