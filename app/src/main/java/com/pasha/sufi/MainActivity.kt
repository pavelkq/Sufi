package com.pasha.sufi

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.pasha.sufi.ui.base.BaseActivity
import com.pasha.sufi.ui.articles.ArticlesContainerFragment
import com.pasha.sufi.ui.books.BooksFragment
import com.pasha.sufi.ui.chasha.ChashaSakiFragment
import com.pasha.sufi.ui.chat.ChatFragment
import com.pasha.sufi.ui.menu.MenuPopup
import com.pasha.sufi.R

class MainActivity : BaseActivity() {
    
    private lateinit var fragmentContainer: View
    private lateinit var navChasha: LinearLayout
    private lateinit var navBooks: LinearLayout
    private lateinit var navArticles: LinearLayout
    private lateinit var navChat: LinearLayout
    private lateinit var navMenu: LinearLayout
    
    private var currentFragmentTag = "chasha"
    private lateinit var menuPopup: MenuPopup
    
    companion object {
        const val FRAGMENT_CHASHA = "chasha"
        const val FRAGMENT_BOOKS = "books"
        const val FRAGMENT_ARTICLES = "articles"
        const val FRAGMENT_CHAT = "chat"
    }
    
    override fun getLayoutResId(): Int = R.layout.activity_main
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        fragmentContainer = findViewById(R.id.fragment_container)
        navChasha = findViewById(R.id.nav_chasha)
        navBooks = findViewById(R.id.nav_books)
        navArticles = findViewById(R.id.nav_articles)
        navChat = findViewById(R.id.nav_chat)
        navMenu = findViewById(R.id.nav_menu)
        
        menuPopup = MenuPopup(this)
        
        setupNavigation()
        
        // Всегда показываем Чашу Саки при запуске
        if (savedInstanceState == null) {
            showFragment(FRAGMENT_CHASHA)
        }
    }
    
    private fun setupNavigation() {
        navChasha.setOnClickListener { showFragment(FRAGMENT_CHASHA) }
        navBooks.setOnClickListener { showFragment(FRAGMENT_BOOKS) }
        navArticles.setOnClickListener { showFragment(FRAGMENT_ARTICLES) }
        navChat.setOnClickListener { showFragment(FRAGMENT_CHAT) }
        navMenu.setOnClickListener { 
            Toast.makeText(this, "Меню открывается", Toast.LENGTH_SHORT).show()
            showMenu() 
        }
    }
    
    private fun showFragment(tag: String) {
        currentFragmentTag = tag
        
        val fragment = when (tag) {
            FRAGMENT_CHASHA -> ChashaSakiFragment.newInstance()
            FRAGMENT_BOOKS -> BooksFragment.newInstance()
            FRAGMENT_ARTICLES -> ArticlesContainerFragment.newInstance()
            FRAGMENT_CHAT -> ChatFragment.newInstance()
            else -> return
        }
        
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
        
        updateNavSelection(tag)
    }
    
    private fun updateNavSelection(tag: String) {
        val alphaSelected = 1.0f
        val alphaNormal = 0.6f
        
        navChasha.alpha = if (tag == FRAGMENT_CHASHA) alphaSelected else alphaNormal
        navBooks.alpha = if (tag == FRAGMENT_BOOKS) alphaSelected else alphaNormal
        navArticles.alpha = if (tag == FRAGMENT_ARTICLES) alphaSelected else alphaNormal
        navChat.alpha = if (tag == FRAGMENT_CHAT) alphaSelected else alphaNormal
    }
    
    private fun showMenu() {
        menuPopup.show(navMenu)
    }
    
    fun openArticleDetail(articleId: Int, title: String) {
        // TODO: Открыть детальную статью
    }
}
