package com.pasha.sufi.ui.base

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.pasha.sufi.R
import com.pasha.sufi.theme.ThemeManager

abstract class BaseActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.init(applicationContext)
        setContentView(getLayoutResId())
        applyTheme()
    }
    
    override fun onResume() {
        super.onResume()
        applyTheme()
    }
    
    protected fun applyTheme() {
        ThemeManager.applyThemeToActivity(this)
        
        // Обновляем фон баннера
        val banner = findViewById<ImageView>(R.id.bannerImageView)
        banner?.setBackgroundColor(ThemeManager.getBackgroundColor())
        
        onThemeApplied()
    }
    
    protected open fun onThemeApplied() {}
    
    protected abstract fun getLayoutResId(): Int
}
