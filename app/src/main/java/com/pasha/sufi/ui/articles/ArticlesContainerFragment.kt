package com.pasha.sufi.ui.articles

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.pasha.sufi.R
import com.pasha.sufi.models.VkPost
import com.pasha.sufi.network.VkApi
import com.pasha.sufi.network.VkApiClient
import com.pasha.sufi.ui.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticlesContainerFragment : BaseFragment() {
    
    private var tabLayout: TabLayout? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var tvError: TextView
    
    private var vkPostsAdapter: VkPostsSimpleAdapter? = null
    private var vkPosts: List<VkPost> = emptyList()
    
    override fun getLayoutResId(): Int = R.layout.fragment_articles_container
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        tabLayout = view.findViewById(R.id.tabLayout)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        tvError = view.findViewById(R.id.tvError)
        
        setupTabs()
        setupRecyclerView()
    }
    
    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        vkPostsAdapter = VkPostsSimpleAdapter(emptyList()) { post ->
            showFullPost(post)
        }
        recyclerView.adapter = vkPostsAdapter
    }
    
    private fun setupTabs() {
        val tabs = tabLayout ?: return
        
        tabs.setTabTextColors(
            android.graphics.Color.parseColor("#666666"),
            android.graphics.Color.parseColor("#000000")
        )
        
        tabs.addTab(tabs.newTab().setText("📰 Всё"))
        tabs.addTab(tabs.newTab().setText("📄 Статьи"))
        tabs.addTab(tabs.newTab().setText("🎵 Медиа"))
        tabs.addTab(tabs.newTab().setText("💬 Лента VK"))
        
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadAll()
                    1 -> loadArticles()
                    2 -> loadMedia()
                    3 -> loadVkFeed()
                }
                updateTabStyle(tab)
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.view?.let { tabView ->
                    val textView = getTabTextView(tabView)
                    textView?.setTextColor(android.graphics.Color.parseColor("#666666"))
                    textView?.setTypeface(null, android.graphics.Typeface.NORMAL)
                }
            }
            
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        
        // Загружаем VK ленту для "Всё" и "Лента VK"
        loadVkFeed()
        tabs.getTabAt(0)?.select()
    }
    
    private fun updateTabStyle(tab: TabLayout.Tab?) {
        tab?.view?.let { tabView ->
            val textView = getTabTextView(tabView)
            textView?.setTextColor(android.graphics.Color.parseColor("#000000"))
            textView?.setTypeface(null, android.graphics.Typeface.BOLD)
        }
    }
    
    private fun getTabTextView(tabView: View): TextView? {
        if (tabView is TextView) return tabView
        if (tabView is ViewGroup) {
            for (i in 0 until tabView.childCount) {
                val child = tabView.getChildAt(i)
                if (child is TextView) return child
                if (child is ViewGroup) {
                    val found = getTabTextView(child)
                    if (found != null) return found
                }
            }
        }
        return null
    }
    
    private fun loadAll() {
        // Показываем VK ленту (так как других статей пока нет)
        loadVkFeed()
    }
    
    private fun loadArticles() {
        showContent(false)
        tvEmpty.visibility = View.VISIBLE
        tvEmpty.text = "📄 Статьи\n\nСписок статей будет здесь после авторизации\n\nВ разработке"
    }
    
    private fun loadMedia() {
        showContent(false)
        tvEmpty.visibility = View.VISIBLE
        tvEmpty.text = "🎵 Медиа\n\nАудио и видео материалы\n\nВ разработке"
    }
    
    private fun loadVkFeed() {
        showContent(true)
        loadVkPosts()
    }
    
    private fun showContent(showRecycler: Boolean) {
        if (showRecycler) {
            recyclerView.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            tvError.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
        }
    }
    
    private fun loadVkPosts() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        tvEmpty.visibility = View.GONE
        tvError.visibility = View.GONE
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = VkApiClient.instance.create(VkApi::class.java)
                val response = api.getWallPosts(
                    ownerId = VkApiClient.COMMUNITY_ID,
                    count = 20,
                    offset = 0
                )
                
                val posts = response.response?.items ?: emptyList()
                
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (posts.isNotEmpty()) {
                        vkPosts = posts
                        vkPostsAdapter?.updatePosts(posts)
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        tvEmpty.visibility = View.VISIBLE
                        tvEmpty.text = "Нет постов в ленте"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    tvError.visibility = View.VISIBLE
                    tvError.text = "Ошибка загрузки: ${e.message}"
                }
                e.printStackTrace()
            }
        }
    }
    
    private fun showFullPost(post: VkPost) {
        // Открываем полноэкранный фрагмент
        val detailFragment = ArticleDetailFragment.newInstance(post)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }
    
    override fun onThemeApplied() {
        super.onThemeApplied()
        tabLayout?.setTabTextColors(
            android.graphics.Color.parseColor("#666666"),
            android.graphics.Color.parseColor("#000000")
        )
        tabLayout?.getTabAt(tabLayout?.selectedTabPosition ?: 0)?.let { tab ->
            updateTabStyle(tab)
        }
    }
    
    companion object {
        fun newInstance(): ArticlesContainerFragment {
            return ArticlesContainerFragment()
        }
    }
}
