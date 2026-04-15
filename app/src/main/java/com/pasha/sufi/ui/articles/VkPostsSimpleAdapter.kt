package com.pasha.sufi.ui.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pasha.sufi.R
import com.pasha.sufi.models.VkPost
import com.pasha.sufi.theme.ThemeManager
import java.text.SimpleDateFormat
import java.util.*

class VkPostsSimpleAdapter(
    private var posts: List<VkPost>,
    private val onItemClick: (VkPost, Int) -> Unit
) : RecyclerView.Adapter<VkPostsSimpleAdapter.PostViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vk_post_simple, parent, false)
        return PostViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], position)
    }
    
    override fun getItemCount(): Int = posts.size
    
    fun updatePosts(newPosts: List<VkPost>) {
        posts = newPosts
        notifyDataSetChanged()
    }
    
    private fun getFirstSentence(text: String): String {
        val trimmedText = text.trim()
        if (trimmedText.isEmpty()) return "Пост без текста"
        
        val sentenceEnders = listOf(".", "!", "?", "...", "!..", "?..", ".\"", "!\"", "?\"")
        
        var endIndex = -1
        for (ender in sentenceEnders) {
            val index = trimmedText.indexOf(ender)
            if (index != -1 && (endIndex == -1 || index < endIndex)) {
                endIndex = index + ender.length
            }
        }
        
        if (endIndex != -1 && endIndex <= 500) {
            return trimmedText.substring(0, endIndex)
        }
        
        return if (trimmedText.length > 500) {
            trimmedText.substring(0, 500) + "..."
        } else {
            trimmedText
        }
    }
    
    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        
        fun bind(post: VkPost, position: Int) {
            val fullText = post.text.trim()
            val title = getFirstSentence(fullText)
            tvTitle.text = title
            
            val date = Date(post.date * 1000)
            val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("ru"))
            tvDate.text = dateFormat.format(date)
            
            applyTheme()
            
            itemView.setOnClickListener {
                onItemClick(post, position)
            }
        }
        
        private fun applyTheme() {
            tvTitle.setTextColor(ThemeManager.getTextColor())
            tvTitle.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 46f)
            tvDate.setTextColor(ThemeManager.getTextSecondaryColor())
            tvDate.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 42f)
            
            val scaledSize = 46f * ThemeManager.currentFontSize.scale
            tvTitle.textSize = scaledSize
            tvDate.textSize = 42f * ThemeManager.currentFontSize.scale
            
            itemView.setBackgroundColor(ThemeManager.getCardBackgroundColor())
        }
    }
}
