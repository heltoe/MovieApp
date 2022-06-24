package study.heltoe.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import study.heltoe.movieapp.databinding.MovieItemBinding
import study.heltoe.movieapp.models.movieList.FilmSearchByFiltersResponseItems

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.MovieView>() {
    inner class MovieView(val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<FilmSearchByFiltersResponseItems>() {
        override fun areItemsTheSame(oldItem: FilmSearchByFiltersResponseItems, newItem: FilmSearchByFiltersResponseItems): Boolean {
            return oldItem.kinopoiskId == newItem.kinopoiskId
        }

        override fun areContentsTheSame(oldItem: FilmSearchByFiltersResponseItems, newItem: FilmSearchByFiltersResponseItems): Boolean {
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
            movie.posterUrlPreview.let { Glide.with(this).load(it).into(holder.binding.cardImage)}
            val name = movie.nameRu ?: movie.nameOriginal ?: movie.nameEn
            name?.let { holder.binding.cardText.text = name }
            setOnClickListener { onItemClickListener?.let { it(movie) } }
        }
    }

    private var onItemClickListener: ((FilmSearchByFiltersResponseItems) -> Unit)? = null

    fun setOnItemClickListener(listener: (FilmSearchByFiltersResponseItems) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}