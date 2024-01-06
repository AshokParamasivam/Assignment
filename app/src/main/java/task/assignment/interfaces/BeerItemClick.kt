package task.assignment.interfaces

import task.assignment.model.BeerListResponseItem

interface BeerItemClick {
    fun onBeerClick(beerListResponseItem: BeerListResponseItem)
}