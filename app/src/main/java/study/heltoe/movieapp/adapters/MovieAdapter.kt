package study.heltoe.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import study.heltoe.movieapp.databinding.MovieItemBinding
import study.heltoe.movieapp.models.movieList.Movie

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.MovieView>() {
    inner class MovieView(val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    var differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieView {

        return MovieView(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieView, position: Int) {
        val movie = differ.currentList[position]
        holder.itemView.apply {
            movie.poster.previewUrl?.let {
                Glide.with(this).load(movie.poster.url).into(holder.binding.cardImage)
            }
            val name = movie.name ?: movie.alternativeName
            name?.let {
                holder.binding.cardText.text = name
            }
            setOnClickListener {
                onItemClickListener?.let { it(movie) }
            }
        }
    }

    private var onItemClickListener: ((Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}