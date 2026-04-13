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
    private val onItemClick: (VkPost) -> Unit
) : RecyclerView.Adapter<VkPostsSimpleAdapter.PostViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vk_post_simple, parent, false)
        return PostViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }
    
    override fun getItemCount(): Int = posts.size
    
    fun updatePosts(newPosts: List<VkPost>) {
        posts = newPosts
        notifyDataSetChanged()
    }
    
    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        
        fun bind(post: VkPost) {
            // Берём первые 100 символов как заголовок
            val title = if (post.text.length > 100) {
                post.text.substring(0, 100) + "..."
            } else {
                post.text
            }.trim().ifEmpty { "Пост без текста" }
            
            tvTitle.text = title
            
            // Форматируем дату
            val date = Date(post.date * 1000)
            val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("ru"))
            tvDate.text = dateFormat.format(date)
            
            // Применяем тему и размер шрифта - используем sp для автоматического масштабирования
            tvTitle.setTextColor(ThemeManager.getTextColor())
            tvTitle.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 36f)
            tvDate.setTextColor(ThemeManager.getTextSecondaryColor())
            tvDate.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 32f)
            
            // Применяем масштаб из настроек
            val scaledSize = 36f * ThemeManager.currentFontSize.scale
            tvTitle.textSize = scaledSize
            tvDate.textSize = 32f * ThemeManager.currentFontSize.scale
            
            itemView.setBackgroundColor(ThemeManager.getCardBackgroundColor())
            
            itemView.setOnClickListener {
                onItemClick(post)
            }
        }
    }
}
