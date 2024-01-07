package task.assignment.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import task.assignment.R
import task.assignment.adapter.BeerListAdapter
import task.assignment.databinding.FragmentBeerListBinding
import task.assignment.interfaces.BeerActivityAction
import task.assignment.interfaces.BeerItemClick
import task.assignment.model.BeerListResponseItem
import task.assignment.network.BeerRepository
import task.assignment.viewmodel.BeerViewModel
import task.assignment.viewmodel.BeerViewModelFactory

class BeerListFragment : Fragment() {

    private lateinit var binding: FragmentBeerListBinding
    private lateinit var beerViewModel: BeerViewModel
    private var beerActivityAction: BeerActivityAction? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_beer_list, container, false)
        beerViewModel.beerListData.observe(viewLifecycleOwner) {
            setBeerList(it)
        }
        beerViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.srlBeerList.isRefreshing = it
        }

        binding.srlBeerList.setOnRefreshListener {
            beerViewModel.getBeerList()
        }

        beerViewModel.isError.observe(viewLifecycleOwner) {
            binding.tvError.visibility = if (it) View.VISIBLE else View.INVISIBLE
            if (it) {
                binding.tvError.text = beerViewModel.errorMessage
            }
        }

        return binding.root
    }

    private fun setBeerList(beerListResponseItems: ArrayList<BeerListResponseItem>) {
        val beerListAdapter =
            BeerListAdapter(beerListResponseItems, object : BeerItemClick {
                override fun onBeerClick(beerListResponseItem: BeerListResponseItem) {
                    beerViewModel.selectedBeerId.value = beerListResponseItem.id
                    beerActivityAction?.moveToDetailsPage()
                }
            })
        binding.rvBeerList.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        binding.rvBeerList.adapter = beerListAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BeerActivityAction) {
            beerActivityAction = context
        }
        beerViewModel =
            ViewModelProvider(requireActivity(), BeerViewModelFactory(BeerRepository())).get(
                BeerViewModel::class.java
            )
        beerViewModel.getBeerList()
    }
}