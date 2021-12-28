package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.RepresentativeDetailBinding
import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.representative.model.Representative


class RepresentativeListAdapter : ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder.from(parent)        //we create the viewholder as below
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

//the binding item is the single detailed screen to be recycled.
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

//we create the repVH class with dataBinding as a parameter
    class RepresentativeViewHolder(/*private val*/ var binding: RepresentativeDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        //this binding links to the xml representative view
        //click listener added
        fun bind(/*clickListener: RepresentativeClickListener,*/ item: Representative) {
            binding.representative = item       //this binds to the detailed layout for the representative and sets the default

           // binding.clickListener = clickListener
            binding.representativePhoto.setImageResource(R.drawable.ic_profile)

            //additional data for the representative detail
            binding.representativePosition.text = item.official.name
            binding.electName.text = item.office.name
            binding.partyName.text = item.official.party

            //TO DO: Show social links ** Hint: Use provided helper methods
            item.official.urls?.let { showWWWLinks(it) }

            //TO DO: Show www link ** Hint: Use provided helper methods
            item.official.channels?.let { showSocialLinks(it) }

            binding.executePendingBindings()
        }

        //T ODO: Add companion object to inflate ViewHolder (from)

        companion object {
            fun from(parent: ViewGroup): RepresentativeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RepresentativeDetailBinding.inflate(layoutInflater, parent, false)
                return RepresentativeViewHolder(binding)
            }
        }

        //helper methods
        private fun showSocialLinks(channels: List<Channel>) {
            val facebookUrl = getFacebookUrl(channels)
            if (!facebookUrl.isNullOrBlank()) {
                enableLink(binding.facebookIcon, facebookUrl)
            }

            val twitterUrl = getTwitterUrl(channels)
            if (!twitterUrl.isNullOrBlank()) {
                enableLink(binding.twitterIcon, twitterUrl)
            }
        }

        private fun showWWWLinks(urls: List<String>) {
            enableLink(binding.wwwIcon, urls.first())
        }

        private fun getFacebookUrl(channels: List<Channel>): String? {
            return channels.filter { channel -> channel.type == "Facebook" }
                    .map { channel -> "https://www.facebook.com/${channel.id}" }
                    .firstOrNull()
        }

        private fun getTwitterUrl(channels: List<Channel>): String? {
            return channels.filter { channel -> channel.type == "Twitter" }
                    .map { channel -> "https://www.twitter.com/${channel.id}" }
                    .firstOrNull()
        }

        private fun enableLink(view: ImageView, url: String) {
            view.visibility = View.VISIBLE
            view.setOnClickListener { setIntent(url) }
        }

        private fun setIntent(url: String) {
            val uri = Uri.parse(url)
            val intent = Intent(ACTION_VIEW, uri)
            itemView.context.startActivity(intent)
        }
    }

    //TO DO: Create RepresentativeDiffCallback
    class RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>() {
        override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
            //  return oldItem == newItem
            return (oldItem.official == newItem.official)// && oldItem.office == newItem.office)
        }

        override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
            return oldItem.office == newItem.office
            //try  return oldItem == newItem
        }
    }

    //TO DO: Create RepresentativeListener
    class RepresentativeClickListener(val clickListener: (representative: Representative) -> Unit) {
        fun onClick(representative: Representative) = clickListener(representative)
    }
//}

