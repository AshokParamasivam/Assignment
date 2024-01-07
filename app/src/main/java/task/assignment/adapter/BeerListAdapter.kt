package task.assignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import task.assignment.R
import task.assignment.databinding.BeerListItemBinding
import task.assignment.interfaces.BeerItemClick
import task.assignment.model.BeerListResponseItem

class BeerListAdapter(
    var beerListItems: ArrayList<BeerListResponseItem>,
    val onItemCLick: BeerItemClick
) :
    RecyclerView.Adapter<BeerListAdapter.BeerListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerListViewHolder {
        return BeerListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.beer_list_item, parent, false
            ), onItemCLick
        )
    }

    override fun getItemCount(): Int {
        return beerListItems.size
    }

    override fun onBindViewHolder(holder: BeerListViewHolder, position: Int) {
        val model = beerListItems[position]
        holder.bindData(model)
    }

    class BeerListViewHolder(
        val binding: BeerListItemBinding,
        val onItemCLick: BeerItemClick
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: BeerListResponseItem) {
            binding.beerItemModel = model
            binding.itemClickListener = onItemCLick
        }
    }
}