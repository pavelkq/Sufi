package com.pasha.sufi.ui.books

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.pasha.sufi.R
import com.pasha.sufi.ui.base.BaseFragment

class BooksFragment : BaseFragment() {

    private lateinit var tvPlaceholder: TextView

    override fun getLayoutResId(): Int = R.layout.fragment_books

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvPlaceholder = view.findViewById(R.id.tvPlaceholder)
        tvPlaceholder.text = "📚 Книги\n\nРаздел в разработке"
    }

    companion object {
        fun newInstance(): BooksFragment {
            return BooksFragment()
        }
    }
}