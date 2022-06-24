package study.heltoe.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import study.heltoe.movieapp.databinding.ActorCardBinding
import study.heltoe.movieapp.models.staffList.StaffResponse

class ActorAdapter: RecyclerView.Adapter<ActorAdapter.ActorView>() {
    inner class ActorView(val binding: ActorCardBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<StaffResponse>() {
        override fun areItemsTheSame(oldItem: StaffResponse, newItem: StaffResponse): Boolean {
            return oldItem.staffId == newItem.staffId
        }

        override fun areContentsTheSame(oldItem: StaffResponse, newItem: StaffResponse): Boolean {
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
        val staff = differ.currentList[position]
        holder.itemView.apply {
            val name = staff.nameRu ?: staff.nameEn
            name?.let { holder.binding.actorName.text = it }
            staff.posterUrl.let { Glide.with(this).load(it).into(holder.binding.actorImage) }
            setOnClickListener { onItemClickListener?.let { it(staff) } }
        }
    }

    private var onItemClickListener: ((StaffResponse) -> Unit)? = null

    fun setOnItemClickListener (listener: (StaffResponse) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}