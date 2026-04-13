package com.pasha.sufi.ui.chasha

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.pasha.sufi.R
import com.pasha.sufi.ui.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class ChashaSakiFragment : BaseFragment() {
    
    private lateinit var tvTitle: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvContent: TextView
    
    override fun getLayoutResId(): Int = R.layout.fragment_chasha_saki
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        tvTitle = view.findViewById(R.id.tvTitle)
        tvDate = view.findViewById(R.id.tvDate)
        tvContent = view.findViewById(R.id.tvContent)
        
        loadDailyText()
    }
    
    private fun loadDailyText() {
        // TODO: Загрузить текст из DailyApi
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale("ru")).format(Date())
        tvDate.text = currentDate
        tvContent.text = "Мудрость Суфиев гласит:\n\n" +
                "Любовь — это мост между тобой и всем сущим.\n\n" +
                "Ищи свет в своем сердце, и ты найдешь путь.\n\n" +
                "Каждый день — это новая возможность стать лучше."
    }
    
    companion object {
        fun newInstance(): ChashaSakiFragment {
            return ChashaSakiFragment()
        }
    }
}
