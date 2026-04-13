package com.pasha.sufi.ui.menu

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pasha.sufi.R
import com.pasha.sufi.ui.settings.SettingsFragment

class MenuPopup(private val context: Context) {
    
    fun show(anchorView: View) {
        val popupMenu = PopupMenu(context, anchorView)
        
        popupMenu.menu.add(0, 1, 0, "👤 Профиль")
        popupMenu.menu.add(0, 2, 1, "⚙️ Настройки")
        popupMenu.menu.add(0, 3, 2, "ℹ️ О программе")
        
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                1 -> {
                    Toast.makeText(context, "👤 Профиль (в разработке)", Toast.LENGTH_SHORT).show()
                    true
                }
                2 -> {
                    openSettings()
                    true
                }
                3 -> {
                    Toast.makeText(context, "ℹ️ О программе (в разработке)", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        
        popupMenu.show()
    }
    
    private fun openSettings() {
        try {
            val activity = context as? AppCompatActivity
            if (activity == null) {
                Toast.makeText(context, "Ошибка: activity не найдена", Toast.LENGTH_SHORT).show()
                return
            }
            
            Toast.makeText(context, "Открываем настройки...", Toast.LENGTH_SHORT).show()
            
            val settingsFragment = SettingsFragment.newInstance()
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, settingsFragment)
                .addToBackStack("settings")
                .commit()
                
            Toast.makeText(context, "Фрагмент настроек должен открыться", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
