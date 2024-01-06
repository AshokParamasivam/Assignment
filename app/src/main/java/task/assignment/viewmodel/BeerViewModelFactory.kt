package task.assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import task.assignment.network.BeerRepository

class BeerViewModelFactory(private val beerRepository: BeerRepository) :ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BeerViewModel::class.java)) {
            BeerViewModel(this.beerRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}