package com.pasha.sufi.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.pasha.sufi.R
import com.pasha.sufi.models.VkPost
import com.pasha.sufi.theme.ThemeManager

class ArticleDetailFragment : Fragment() {
    
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnClose: ImageButton
    
    private var post: VkPost? = null
    
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
        btnClose = view.findViewById(R.id.btnClose)
        
        // Получаем пост из аргументов
        post = arguments?.getSerializable("post") as? VkPost
        
        setupWebView()
        applyTheme()
        
        btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        
        loadContent()
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
        post?.let {
            val htmlContent = generateHtml(it.text)
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        }
    }
    
    private fun generateHtml(content: String): String {
        // Базовая единица измерения в WebView - пиксели
        // Используем 16px как базовый размер, умножаем на масштаб
        val baseFontSize = 36 * ThemeManager.currentFontSize.scale
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
                    line-height: 1.6;
                }
                p {
                    margin-bottom: 16px;
                }
                a {
                    color: ${ThemeManager.currentTheme.colors.primary};
                    text-decoration: none;
                }
                h1, h2, h3, h4, h5, h6 {
                    color: ${ThemeManager.currentTheme.colors.primary};
                    margin-bottom: 16px;
                    margin-top: 24px;
                }
                h1 { font-size: ${baseFontSize * 1.5}px; }
                h2 { font-size: ${baseFontSize * 1.3}px; }
                h3 { font-size: ${baseFontSize * 1.2}px; }
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
        btnClose.setColorFilter(ThemeManager.getPrimaryColor())
    }
    
    override fun onResume() {
        super.onResume()
        applyTheme()
        // Обновляем WebView при смене темы
        loadContent()
    }
    
    companion object {
        fun newInstance(post: VkPost): ArticleDetailFragment {
            val fragment = ArticleDetailFragment()
            val args = Bundle()
            args.putSerializable("post", post)
            fragment.arguments = args
            return fragment
        }
    }
}
