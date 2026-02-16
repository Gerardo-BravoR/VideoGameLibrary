package ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videogamelibrary.databinding.ItemGameBinding
import model.VideoGame

class GameAdapter : RecyclerView.Adapter<GameAdapter.VH>() {
    private val items = mutableListOf<VideoGame>()

    fun submitList(list: List<VideoGame>)
    {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class VH(val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val game = items[position]
        holder.binding.ivCover.setImageResource(game.imagenResId)
        holder.binding.tvTitle.text = game.titulo
        holder.binding.tvPlatform.text = game.plataforma
        holder.binding.tvYear.text = "Año: ${game.anioLanzamiento}"
    }

    override fun getItemCount(): Int = items.size
}