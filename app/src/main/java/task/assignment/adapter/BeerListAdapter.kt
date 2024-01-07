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
    var galleryItems: ArrayList<BeerListResponseItem>,
    val onItemCLick: BeerItemClick
) :
    RecyclerView.Adapter<BeerListAdapter.GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.beer_list_item, parent, false
            ), onItemCLick
        )
    }

    override fun getItemCount(): Int {
        return galleryItems.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val model = galleryItems[position]
        holder.bindData(model)
    }

    class GalleryViewHolder(
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