package study.heltoe.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import study.heltoe.movieapp.R
import study.heltoe.movieapp.databinding.StaffProfuctBinding
import study.heltoe.movieapp.models.staffInfo.PersonResponseFilms

class StaffMovieAdapter: RecyclerView.Adapter<StaffMovieAdapter.MovieView>() {
    inner class MovieView(val binding: StaffProfuctBinding): RecyclerView.ViewHolder(binding.root)

    var diffCallBack = object : DiffUtil.ItemCallback<PersonResponseFilms>() {
        override fun areItemsTheSame(
            oldItem: PersonResponseFilms,
            newItem: PersonResponseFilms
        ): Boolean {
            return oldItem.filmId == newItem.filmId
        }

        override fun areContentsTheSame(
            oldItem: PersonResponseFilms,
            newItem: PersonResponseFilms
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    var differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieView {
        return MovieView(
            StaffProfuctBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieView, position: Int) {
        val movie = differ.currentList[position]
        holder.itemView.apply {
            val name = movie.nameRu ?: movie.nameEn ?: ""
            val description = movie.description ?:""
            val rating = movie.rating ?: ""
            //
            holder.binding.name.text = name
            holder.binding.description.text = description
            //
            holder.binding.rating.text = rating
            if (rating.isNotEmpty() && rating.toDouble() > 7) {
                holder.binding.rating.setTextColor(resources.getColor(R.color.green))
            }
            if (rating.isNotEmpty() &&  rating.toDouble() < 5) {
                holder.binding.rating.setTextColor(resources.getColor(R.color.red))
            }
            setOnClickListener { onItemClickListener?.let { it(movie) } }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((PersonResponseFilms) -> Unit)? = null

    fun setOnItemClickListener (listener: (PersonResponseFilms) -> Unit) {
        onItemClickListener = listener
    }
}