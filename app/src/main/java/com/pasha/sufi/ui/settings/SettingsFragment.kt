package com.pasha.sufi.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pasha.sufi.R
import com.pasha.sufi.theme.ThemeManager

class SettingsFragment : Fragment() {
    
    private lateinit var themeSpinner: Spinner
    private lateinit var fontSizeSpinner: Spinner
    private lateinit var fontFamilySpinner: Spinner
    private lateinit var btnBack: Button
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        themeSpinner = view.findViewById(R.id.themeSpinner)
        fontSizeSpinner = view.findViewById(R.id.fontSizeSpinner)
        fontFamilySpinner = view.findViewById(R.id.fontFamilySpinner)
        btnBack = view.findViewById(R.id.btnBack)
        
        // Загружаем темы из ThemeManager
        val themes = ThemeManager.getAvailableThemes()
        val themeNames = if (themes.isNotEmpty()) {
            themes.map { it.name }
        } else {
            listOf("🌍 Земля", "💧 Вода", "🔥 Огонь", "💨 Воздух")
        }
        
        val fontSizes = listOf("Маленький", "Средний", "Большой", "Очень большой")
        val fontFamilies = listOf("Sans-serif", "Serif", "Monospace", "Системный")
        
        // Настраиваем спиннер темы
        val themeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, themeNames)
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        themeSpinner.adapter = themeAdapter
        
        // Настраиваем спиннер размера шрифта
        val fontSizeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fontSizes)
        fontSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fontSizeSpinner.adapter = fontSizeAdapter
        
        // Настраиваем спиннер шрифта
        val fontFamilyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fontFamilies)
        fontFamilyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fontFamilySpinner.adapter = fontFamilyAdapter
        
        // Устанавливаем текущие значения
        val currentThemeId = ThemeManager.currentTheme.id
        val themeIndex = themes.indexOfFirst { it.id == currentThemeId }
        if (themeIndex >= 0) {
            themeSpinner.setSelection(themeIndex)
        }
        
        val currentFontSizeId = ThemeManager.currentFontSize.id
        val fontSizeIndex = when(currentFontSizeId) {
            "small" -> 0
            "medium" -> 1
            "large" -> 2
            "xlarge" -> 3
            else -> 1
        }
        fontSizeSpinner.setSelection(fontSizeIndex)
        
        val currentFontFamilyId = ThemeManager.currentFontFamily.id
        val fontFamilyIndex = when(currentFontFamilyId) {
            "sans" -> 0
            "serif" -> 1
            "mono" -> 2
            "system" -> 3
            else -> 0
        }
        fontFamilySpinner.setSelection(fontFamilyIndex)
        
        // Обработчики выбора
        themeSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position < themes.size) {
                    val selectedTheme = themes[position]
                    ThemeManager.setTheme(selectedTheme.id)
                    // Применяем тему к активности
                    (activity as? AppCompatActivity)?.let {
                        ThemeManager.applyThemeToActivity(it)
                    }
                    Toast.makeText(requireContext(), "Тема: ${selectedTheme.name}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        
        fontSizeSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                val size = when(position) {
                    0 -> "small"
                    1 -> "medium"
                    2 -> "large"
                    3 -> "xlarge"
                    else -> "medium"
                }
                ThemeManager.setFontSize(size)
                // Применяем размер шрифта к активности
                (activity as? AppCompatActivity)?.let {
                    ThemeManager.applyThemeToActivity(it)
                }
                Toast.makeText(requireContext(), "Размер шрифта: ${fontSizes[position]}", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        
        fontFamilySpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                val family = when(position) {
                    0 -> "sans"
                    1 -> "serif"
                    2 -> "mono"
                    3 -> "system"
                    else -> "sans"
                }
                ThemeManager.setFontFamily(family)
                (activity as? AppCompatActivity)?.let {
                    ThemeManager.applyThemeToActivity(it)
                }
                Toast.makeText(requireContext(), "Шрифт: ${fontFamilies[position]}", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        
        // Кнопка назад
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        
        // Применяем тему к этому фрагменту
        view.setBackgroundColor(ThemeManager.getBackgroundColor())
    }
    
    override fun onResume() {
        super.onResume()
        view?.setBackgroundColor(ThemeManager.getBackgroundColor())
    }
    
    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
