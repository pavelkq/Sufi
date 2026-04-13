package com.pasha.sufi.ui.chat

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.pasha.sufi.R
import com.pasha.sufi.ui.base.BaseFragment

class ChatFragment : BaseFragment() {

    private lateinit var tvPlaceholder: TextView

    override fun getLayoutResId(): Int = R.layout.fragment_chat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvPlaceholder = view.findViewById(R.id.tvPlaceholder)
        tvPlaceholder.text = "💬 Чат\n\nБудет доступен в следующих версиях"
    }

    companion object {
        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }
}