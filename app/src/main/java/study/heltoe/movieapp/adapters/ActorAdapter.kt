package study.heltoe.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import study.heltoe.movieapp.databinding.ActorCardBinding
import study.heltoe.movieapp.models.movieInfo.Person

class ActorAdapter: RecyclerView.Adapter<ActorAdapter.ActorView>() {
    inner class ActorView(val binding: ActorCardBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    var differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorView {
        return ActorView(
            ActorCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ActorView, position: Int) {
        val actor = differ.currentList[position]
        holder.itemView.apply {
            holder.binding.actorName.text = actor.name ?: actor.enName
            actor.photo?.let {
                Glide.with(this).load(it).into(holder.binding.actorImage)
            }
            setOnClickListener {
                onItemClickListener?.let { it(actor) }
            }
        }
    }

    private var onItemClickListener: ((Person) -> Unit)? = null

    fun setOnItemClickListener (listener: (Person) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}