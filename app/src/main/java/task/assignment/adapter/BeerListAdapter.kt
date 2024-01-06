package task.assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import task.assignment.R
import task.assignment.databinding.BeerListItemBinding
import task.assignment.interfaces.BeerItemClick
import task.assignment.model.BeerListResponseItem

class BeerListAdapter(val context: Context, var galleryItems: ArrayList<BeerListResponseItem>, val onItemCLick: BeerItemClick) :
    RecyclerView.Adapter<BeerListAdapter.GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.beer_list_item, parent, false
        ),context,onItemCLick)
    }

    override fun getItemCount(): Int {
        return galleryItems.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val model = galleryItems[position]
        holder.bindData(model)
    }


    class GalleryViewHolder(val binding: BeerListItemBinding, val context: Context, val onItemCLick: BeerItemClick) :
        RecyclerView.ViewHolder(binding.root) {

       fun bindData(model :BeerListResponseItem)
       {
           Glide.with(context).load(model.imageUrl).into(binding.beerImage)
           binding.beerNameTv.text = model.name
           binding.tagLineTv.text = model.tagline
           itemView.setOnClickListener {
               onItemCLick.onBeerClick(model)
           }
       }
    }
}