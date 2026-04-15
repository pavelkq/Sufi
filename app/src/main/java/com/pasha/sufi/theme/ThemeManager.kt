package com.pasha.sufi.theme

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pasha.sufi.R
import java.io.InputStreamReader

data class ThemeColors(
    val primary: String,
    val primaryDark: String,
    val primaryLight: String,
    val text: String,
    val textSecondary: String,
    val background: String,
    val cardBackground: String,
    val accent: String,
    val buttonBackground: String,
    val buttonText: String,
    val menuBackground: String,
    val menuText: String
)

data class FontSize(
    val id: String,
    val name: String,
    val scale: Float
)

data class FontFamily(
    val id: String,
    val name: String,
    val css: String
)

data class Theme(
    val id: String,
    val name: String,
    val colors: ThemeColors
)

data class ThemesConfig(
    val themes: List<Theme>,
    val fontSizes: List<FontSize>,
    val fontFamilies: List<FontFamily>
)

object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_CURRENT_THEME = "current_theme"
    private const val KEY_FONT_SIZE = "font_size"
    private const val KEY_FONT_FAMILY = "font_family"
    
    private lateinit var config: ThemesConfig
    private lateinit var prefs: SharedPreferences
    private lateinit var appContext: Context
    
    var currentTheme: Theme = Theme(
        id = "earth",
        name = "Земля",
        colors = ThemeColors(
            primary = "#E67E22",
            primaryDark = "#D35400",
            primaryLight = "#F39C12",
            text = "#2C3E50",
            textSecondary = "#7F8C8D",
            background = "#FDF2E9",
            cardBackground = "#FFFFFF",
            accent = "#E67E22",
            buttonBackground = "#E67E22",
            buttonText = "#FFFFFF",
            menuBackground = "#E67E22",
            menuText = "#FFFFFF"
        )
    )
    
    var currentFontSize: FontSize = FontSize("medium", "Средний", 1.0f)
    var currentFontFamily: FontFamily = FontFamily("sans", "Sans-serif", "sans-serif")
    
    fun init(context: Context) {
        appContext = context.applicationContext
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadThemesConfig()
        loadSavedTheme()
        loadSavedFontSize()
        loadSavedFontFamily()
    }
    
    private fun loadThemesConfig() {
        try {
            val inputStream = appContext.assets.open("themes.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<ThemesConfig>() {}.type
            config = Gson().fromJson(reader, type)
            
            val savedThemeId = prefs.getString(KEY_CURRENT_THEME, "earth")
            currentTheme = config.themes.find { it.id == savedThemeId } ?: config.themes.firstOrNull() ?: currentTheme
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun loadSavedTheme() {
        val themeId = prefs.getString(KEY_CURRENT_THEME, "earth")
        if (::config.isInitialized) {
            config.themes.find { it.id == themeId }?.let {
                currentTheme = it
            }
        }
    }
    
    private fun loadSavedFontSize() {
        val fontSizeId = prefs.getString(KEY_FONT_SIZE, "medium")
        if (::config.isInitialized) {
            config.fontSizes.find { it.id == fontSizeId }?.let {
                currentFontSize = it
            }
        }
    }
    
    private fun loadSavedFontFamily() {
        val fontFamilyId = prefs.getString(KEY_FONT_FAMILY, "sans")
        if (::config.isInitialized) {
            config.fontFamilies.find { it.id == fontFamilyId }?.let {
                currentFontFamily = it
            }
        }
    }
    
    fun setTheme(themeId: String) {
        if (::config.isInitialized) {
            config.themes.find { it.id == themeId }?.let {
                currentTheme = it
                prefs.edit().putString(KEY_CURRENT_THEME, themeId).apply()
            }
        }
    }
    
    fun setFontSize(fontSizeId: String) {
        if (::config.isInitialized) {
            config.fontSizes.find { it.id == fontSizeId }?.let {
                currentFontSize = it
                prefs.edit().putString(KEY_FONT_SIZE, fontSizeId).apply()
            }
        }
    }
    
    fun setFontFamily(fontFamilyId: String) {
        if (::config.isInitialized) {
            config.fontFamilies.find { it.id == fontFamilyId }?.let {
                currentFontFamily = it
                prefs.edit().putString(KEY_FONT_FAMILY, fontFamilyId).apply()
            }
        }
    }
    
    fun getAvailableThemes(): List<Theme> = if (::config.isInitialized) config.themes else emptyList()
    fun getAvailableFontSizes(): List<FontSize> = if (::config.isInitialized) config.fontSizes else emptyList()
    fun getAvailableFontFamilies(): List<FontFamily> = if (::config.isInitialized) config.fontFamilies else emptyList()
    
    private fun getColor(colorHex: String): Int {
        return try {
            Color.parseColor(colorHex)
        } catch (e: Exception) {
            Color.BLACK
        }
    }
    
    fun getPrimaryColor(): Int = getColor(currentTheme.colors.primary)
    fun getPrimaryDarkColor(): Int = getColor(currentTheme.colors.primaryDark)
    fun getPrimaryLightColor(): Int = getColor(currentTheme.colors.primaryLight)
    fun getTextColor(): Int = getColor(currentTheme.colors.text)
    fun getTextSecondaryColor(): Int = getColor(currentTheme.colors.textSecondary)
    fun getBackgroundColor(): Int = getColor(currentTheme.colors.background)
    fun getCardBackgroundColor(): Int = getColor(currentTheme.colors.cardBackground)
    fun getAccentColor(): Int = getColor(currentTheme.colors.accent)
    fun getButtonBackgroundColor(): Int = getColor(currentTheme.colors.buttonBackground)
    fun getButtonTextColor(): Int = getColor(currentTheme.colors.buttonText)
    fun getMenuBackgroundColor(): Int = getColor(currentTheme.colors.menuBackground)
    fun getMenuTextColor(): Int = getColor(currentTheme.colors.menuText)
    
    fun applyThemeToActivity(activity: AppCompatActivity) {
        activity.window.setStatusBarColor(getPrimaryDarkColor())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.window.setNavigationBarColor(getPrimaryDarkColor())
        }
        
        activity.window.decorView.rootView?.let { rootView ->
            applyThemeToView(rootView)
        }
        
        val bottomNav = activity.window.decorView.rootView.findViewById<ViewGroup>(R.id.bottom_navigation)
        bottomNav?.setBackgroundColor(getPrimaryColor())
        
        // Обновляем фон баннера
        val banner = activity.window.decorView.rootView.findViewById<ImageView>(R.id.bannerImageView)
        banner?.setBackgroundColor(getBackgroundColor())
    }
    
    fun applyThemeToView(view: View) {
        // Пропускаем баннер - он должен оставаться прозрачным
        if (view.id == R.id.bannerImageView) {
            return
        }
        
        when (view) {
            is ViewGroup -> {
                view.children.forEach { child -> applyThemeToView(child) }
                if (view.tag != "skip_theme") {
                    if (view.id != R.id.bottom_navigation) {
                        view.setBackgroundColor(getBackgroundColor())
                    }
                }
            }
            is TextView -> {
                view.setTextColor(getTextColor())
                val originalSize = view.tag as? Float ?: view.textSize
                if (view.tag == null) {
                    view.tag = view.textSize
                }
                view.textSize = (view.tag as Float) * currentFontSize.scale
            }
            is Button -> {
                view.setBackgroundColor(getButtonBackgroundColor())
                view.setTextColor(getButtonTextColor())
                val originalSize = view.tag as? Float ?: view.textSize
                if (view.tag == null) {
                    view.tag = view.textSize
                }
                view.textSize = (view.tag as Float) * currentFontSize.scale
            }
            is EditText -> {
                view.setTextColor(getTextColor())
                view.setHintTextColor(getTextSecondaryColor())
                val originalSize = view.tag as? Float ?: view.textSize
                if (view.tag == null) {
                    view.tag = view.textSize
                }
                view.textSize = (view.tag as Float) * currentFontSize.scale
            }
            else -> {
                if (view.tag != "skip_theme" && view.id != R.id.bottom_navigation) {
                    view.setBackgroundColor(getBackgroundColor())
                }
            }
        }
    }
    
    fun getWebViewCSS(): String {
        val baseFontSize = 16 * currentFontSize.scale
        return """
            <style>
                body {
                    background-color: ${currentTheme.colors.background};
                    color: ${currentTheme.colors.text};
                    font-family: ${currentFontFamily.css};
                    font-size: ${baseFontSize}px;
                    padding: 20px 16px;
                    line-height: 1.6;
                }
                a {
                    color: ${currentTheme.colors.primary};
                }
                h1, h2, h3, h4, h5, h6 {
                    color: ${currentTheme.colors.primary};
                }
                img {
                    max-width: 100%;
                    height: auto;
                }
                blockquote {
                    border-left: 4px solid ${currentTheme.colors.primary};
                    padding-left: 16px;
                    margin-left: 0;
                    color: ${currentTheme.colors.textSecondary};
                }
            </style>
        """.trimIndent()
    }
}
