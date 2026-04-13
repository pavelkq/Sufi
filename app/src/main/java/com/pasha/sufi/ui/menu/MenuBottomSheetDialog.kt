package com.pasha.sufi.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pasha.sufi.R
import com.pasha.sufi.ui.settings.SettingsFragment

class MenuBottomSheetDialog : BottomSheetDialogFragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_menu_bottom_sheet, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val menuProfile = view.findViewById<TextView>(R.id.menuProfile)
        val menuSettings = view.findViewById<TextView>(R.id.menuSettings)
        val menuAbout = view.findViewById<TextView>(R.id.menuAbout)
        val btnClose = view.findViewById<TextView>(R.id.btnClose)
        
        menuProfile.setOnClickListener {
            dismiss()
            Toast.makeText(requireContext(), "Профиль (в разработке)", Toast.LENGTH_SHORT).show()
        }
        
        menuSettings.setOnClickListener {
            dismiss()
            Toast.makeText(requireContext(), "Открываем настройки...", Toast.LENGTH_SHORT).show()
            openSettings()
        }
        
        menuAbout.setOnClickListener {
            dismiss()
            Toast.makeText(requireContext(), "О программе (в разработке)", Toast.LENGTH_SHORT).show()
        }
        
        btnClose.setOnClickListener {
            dismiss()
        }
    }
    
    private fun openSettings() {
        val settingsFragment = SettingsFragment.newInstance()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, settingsFragment)
            .addToBackStack(null)
            .commit()
    }
}
