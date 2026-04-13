package com.pasha.sufi.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        onThemeApplied()
    }
    
    protected open fun onThemeApplied() {}
    
    protected abstract fun getLayoutResId(): Int
}
