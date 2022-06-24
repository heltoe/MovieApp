package study.heltoe.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import study.heltoe.movieapp.databinding.MovieItemBinding
import study.heltoe.movieapp.models.movieTop.FilmTopResponseFilms

class TopMoviesAdapter : RecyclerView.Adapter<TopMoviesAdapter.MovieView>() {
    inner class MovieView(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<FilmTopResponseFilms>() {
        override fun areItemsTheSame(
            oldItem: FilmTopResponseFilms,
            newItem: FilmTopResponseFilms
        ): Boolean {
            return oldItem.filmId == newItem.filmId
        }

        override fun areContentsTheSame(
            oldItem: FilmTopResponseFilms,
            newItem: FilmTopResponseFilms
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)

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
            movie.posterUrlPreview.let {
                Glide.with(this).load(movie.posterUrlPreview).into(holder.binding.cardImage)
            }
            val name = movie.nameRu ?: movie.nameEn
            name?.let { holder.binding.cardText.text = name }
            setOnClickListener {
                onItemClickListener?.let { it(movie) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((FilmTopResponseFilms) -> Unit)? = null

    fun setOnItemClickListener(listener: (FilmTopResponseFilms) -> Unit) {
        onItemClickListener = listener
    }
}