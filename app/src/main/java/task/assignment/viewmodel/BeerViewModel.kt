package task.assignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
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

    var selectedBeerId = MutableLiveData<Int>(1)

    var errorMessage: String = ""
        private set

    fun getBeerList() {

        _isLoading.value = true
        _isError.value = false

        viewModelScope.launch {
            val response = beerRepository.getBeerList()
            val responseBody = response.body()
            withContext(Dispatchers.Main) {
                _isLoading.value = false
                if (!response.isSuccessful || responseBody == null) {
                    onError("Error : ${response.message()} ")
                } else {
                    _beerListData.postValue(responseBody)
                }
            }
        }
    }


    fun getBeerDetails() {
        _isLoading.value = true
        _isError.value = false

        if (selectedBeerId.value!=null)
        {
            viewModelScope.launch {
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

        errorMessage = StringBuilder("ERROR: ")
            .append("$message some data may not displayed properly").toString()

        _isError.value = true
        _isLoading.value = false
    }

}