package com.pasha.sufi.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pasha.sufi.theme.ThemeManager

abstract class BaseFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyTheme()
    }
    
    override fun onResume() {
        super.onResume()
        applyTheme()
    }
    
    protected fun applyTheme() {
        view?.let {
            ThemeManager.applyThemeToView(it)
        }
        onThemeApplied()
    }
    
    protected open fun onThemeApplied() {}
    
    protected abstract fun getLayoutResId(): Int
}
