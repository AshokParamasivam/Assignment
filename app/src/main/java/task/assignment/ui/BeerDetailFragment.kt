package task.assignment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import task.assignment.R
import task.assignment.databinding.FragmentBeerDetailBinding
import task.assignment.model.BeerListResponseItem
import task.assignment.network.BeerRepository
import task.assignment.viewmodel.BeerViewModel
import task.assignment.viewmodel.BeerViewModelFactory

class BeerDetailFragment : Fragment() {

    private lateinit var binding: FragmentBeerDetailBinding
    private lateinit var beerViewModel: BeerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_beer_detail, container, false)
        beerViewModel =
            ViewModelProvider(requireActivity(), BeerViewModelFactory(BeerRepository())).get(
                BeerViewModel::class.java
            )
        beerViewModel.getBeerDetails()
        beerViewModel.beerDetails.observe(viewLifecycleOwner) {
            setBeerDetails(it)
        }

        beerViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressLayout.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        beerViewModel.isError.observe(viewLifecycleOwner) {
            binding.tvError.visibility = if (it) View.VISIBLE else View.GONE

            if (it) {
                binding.tvError.text = beerViewModel.errorMessage
            }
        }

        return binding.root
    }

    private fun setBeerDetails(beerDetail: BeerListResponseItem) {

        binding.beerDetailsModel = beerDetail

        val foodPairDisplay = StringBuilder()
        for (index in beerDetail.foodPairing.indices) {
            foodPairDisplay.append(beerDetail.foodPairing[index])
            if (index < beerDetail.foodPairing.size - 1)
                foodPairDisplay.append("\n")
        }
        binding.tvFoodPair.text = foodPairDisplay
    }

    override fun onStop() {
        super.onStop()
        beerViewModel.isError.value = false
    }

}