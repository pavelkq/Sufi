package com.pasha.sufi.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pasha.sufi.R
import com.pasha.sufi.models.VkPost
import com.pasha.sufi.theme.ThemeManager

class ArticleDetailFragment : Fragment() {
    
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnPrev: ImageButton
    private lateinit var btnList: TextView
    private lateinit var btnNext: ImageButton
    private lateinit var tvTitle: TextView
    
    private var currentPost: VkPost? = null
    private var allPosts: List<VkPost> = emptyList()
    private var currentIndex: Int = 0
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article_detail, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        webView = view.findViewById(R.id.webView)
        progressBar = view.findViewById(R.id.progressBar)
        btnPrev = view.findViewById(R.id.btnPrev)
        btnList = view.findViewById(R.id.btnList)
        btnNext = view.findViewById(R.id.btnNext)
        tvTitle = view.findViewById(R.id.tvTitle)
        
        // Получаем данные из аргументов
        currentPost = arguments?.getSerializable("currentPost") as? VkPost
        allPosts = (arguments?.getSerializable("allPosts") as? ArrayList<VkPost>) ?: emptyList()
        currentIndex = arguments?.getInt("currentIndex") ?: 0
        
        setupWebView()
        applyTheme()
        updateNavigationButtons()
        setTitle()
        
        btnPrev.setOnClickListener { navigateToPrev() }
        btnList.setOnClickListener { goBackToList() }
        btnNext.setOnClickListener { navigateToNext() }
        
        loadContent()
    }
    
    private fun updateNavigationButtons() {
        btnPrev.visibility = if (currentIndex > 0) View.VISIBLE else View.INVISIBLE
        btnNext.visibility = if (currentIndex < allPosts.size - 1) View.VISIBLE else View.INVISIBLE
    }
    
    private fun navigateToPrev() {
        if (currentIndex > 0) {
            currentIndex--
            currentPost = allPosts[currentIndex]
            setTitle()
            loadContent()
            updateNavigationButtons()
        }
    }
    
    private fun navigateToNext() {
        if (currentIndex < allPosts.size - 1) {
            currentIndex++
            currentPost = allPosts[currentIndex]
            setTitle()
            loadContent()
            updateNavigationButtons()
        }
    }
    
    private fun goBackToList() {
        parentFragmentManager.popBackStack()
    }
    
    private fun getFirstSentence(text: String): String {
        val trimmedText = text.trim()
        if (trimmedText.isEmpty()) return "Пост ВКонтакте"
        
        // Ищем знаки препинания конца предложения
        val sentenceEnders = listOf(".", "!", "?", "...", "!..", "?..", ".\"", "!\"", "?\"")
        
        // Ищем позицию первого знака конца предложения
        var endIndex = -1
        for (ender in sentenceEnders) {
            val index = trimmedText.indexOf(ender)
            if (index != -1 && (endIndex == -1 || index < endIndex)) {
                endIndex = index + ender.length
            }
        }
        
        // Если нашли конец предложения
        if (endIndex != -1 && endIndex <= 500) {
            return trimmedText.substring(0, endIndex)
        }
        
        // Если не нашли или предложение слишком длинное, берём до 500 символов
        return if (trimmedText.length > 500) {
            trimmedText.substring(0, 500) + "..."
        } else {
            trimmedText
        }
    }
    
    private fun setTitle() {
        currentPost?.let {
            val fullText = it.text.trim()
            val firstSentence = getFirstSentence(fullText)
            tvTitle.text = firstSentence
        }
    }
    
    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }
        
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }
        }
    }
    
    private fun loadContent() {
        progressBar.visibility = View.VISIBLE
        currentPost?.let {
            val htmlContent = generateHtml(it.text)
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        }
    }
    
    private fun generateHtml(content: String): String {
        val baseFontSize = 44 * ThemeManager.currentFontSize.scale
        val css = """
            <style>
                * {
                    margin: 0;
                    padding: 0;
                    box-sizing: border-box;
                }
                body {
                    background-color: ${ThemeManager.currentTheme.colors.background};
                    color: ${ThemeManager.currentTheme.colors.text};
                    font-family: ${ThemeManager.currentFontFamily.css};
                    font-size: ${baseFontSize}px;
                    padding: 20px 16px;
                    line-height: 1.4;
                }
                p {
                    margin-bottom: 16px;
                }
                a {
                    color: ${ThemeManager.currentTheme.colors.primary};
                    text-decoration: none;
                }
                img {
                    max-width: 100%;
                    height: auto;
                    margin: 16px 0;
                    border-radius: 8px;
                }
                blockquote {
                    border-left: 4px solid ${ThemeManager.currentTheme.colors.primary};
                    padding-left: 16px;
                    margin: 16px 0;
                    color: ${ThemeManager.currentTheme.colors.textSecondary};
                    font-style: italic;
                }
                pre, code {
                    background-color: ${ThemeManager.currentTheme.colors.cardBackground};
                    padding: 8px;
                    border-radius: 4px;
                    overflow-x: auto;
                }
            </style>
        """.trimIndent()
        
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes">
                $css
            </head>
            <body>
                ${content.replace("\n", "<br>")}
            </body>
            </html>
        """.trimIndent()
    }
    
    private fun applyTheme() {
        view?.setBackgroundColor(ThemeManager.getBackgroundColor())
        btnPrev.setColorFilter(ThemeManager.getPrimaryColor())
        btnList.setTextColor(ThemeManager.getPrimaryColor())
        btnNext.setColorFilter(ThemeManager.getPrimaryColor())
        tvTitle.setTextColor(ThemeManager.getTextColor())
        
        val scaledTitleSize = 44f * ThemeManager.currentFontSize.scale
        tvTitle.textSize = scaledTitleSize
        
        val scaledListSize = 32f * ThemeManager.currentFontSize.scale
        btnList.textSize = scaledListSize
    }
    
    override fun onResume() {
        super.onResume()
        applyTheme()
        loadContent()
    }
    
    companion object {
        fun newInstance(currentPost: VkPost, allPosts: List<VkPost>, currentIndex: Int): ArticleDetailFragment {
            val fragment = ArticleDetailFragment()
            val args = Bundle()
            args.putSerializable("currentPost", currentPost)
            args.putSerializable("allPosts", ArrayList(allPosts))
            args.putInt("currentIndex", currentIndex)
            fragment.arguments = args
            return fragment
        }
    }
}
