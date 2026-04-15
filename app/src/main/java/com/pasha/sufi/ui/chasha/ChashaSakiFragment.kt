package com.pasha.sufi.ui.chasha

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.pasha.sufi.R
import com.pasha.sufi.network.ApiClient
import com.pasha.sufi.network.DailyApi
import com.pasha.sufi.theme.ThemeManager
import com.pasha.sufi.ui.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ChashaSakiFragment : BaseFragment() {

    private var tvSectionTitle: TextView? = null
    private var tvDate: TextView? = null
    private var btnToday: Button? = null
    private var tvMainText: TextView? = null
    private var btnComment: Button? = null
    private var tvComment: TextView? = null

    private var selectedDate: LocalDate = LocalDate.now()
    
    override fun getLayoutResId(): Int = R.layout.fragment_chasha_saki

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvSectionTitle = view.findViewById(R.id.tvSectionTitle)
        tvDate = view.findViewById(R.id.tvDate)
        btnToday = view.findViewById(R.id.btnToday)
        tvMainText = view.findViewById(R.id.tvMainText)
        btnComment = view.findViewById(R.id.btnComment)
        tvComment = view.findViewById(R.id.tvComment)

        tvSectionTitle?.text = "Чаша Саки"

        btnComment?.setOnClickListener {
            tvComment?.visibility = if (tvComment?.isVisible == true) View.GONE else View.VISIBLE
        }

        updateDateDisplay(selectedDate)
        btnToday?.visibility = View.GONE

        tvDate?.setOnClickListener {
            openDatePicker()
        }

        btnToday?.setOnClickListener {
            selectedDate = LocalDate.now()
            updateDateDisplay(selectedDate)
            btnToday?.visibility = View.GONE
            // Важно: для API всегда используем 2024 год!
            val apiDate = LocalDate.of(2024, selectedDate.monthValue, selectedDate.dayOfMonth)
            loadDataForDate(apiDate)
        }

        // Загружаем данные для текущей даты (но для API используем 2024 год)
        val apiDate = LocalDate.of(2024, selectedDate.monthValue, selectedDate.dayOfMonth)
        loadDataForDate(apiDate)
    }

    private fun openDatePicker() {
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val displayDate = LocalDate.of(year, month + 1, dayOfMonth)
                updateDateDisplay(displayDate)

                // Для API всегда используем 2024 год, игнорируем выбранный год
                val apiDate = LocalDate.of(2024, month + 1, dayOfMonth)
                loadDataForDate(apiDate)

                val now = LocalDate.now()
                btnToday?.visibility = if (displayDate.year != now.year || 
                                         displayDate.monthValue != now.monthValue || 
                                         displayDate.dayOfMonth != now.dayOfMonth) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

                selectedDate = displayDate
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
        datePicker.show()
    }

    private fun updateDateDisplay(date: LocalDate) {
        // Показываем только день и месяц (без года, так как данные только за 2024)
        val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
        tvDate?.text = date.format(formatter)
    }

    private fun loadDataForDate(date: LocalDate) {
        val currentDateStr = date.format(DateTimeFormatter.ISO_DATE)
        val api = ApiClient.instance.create(DailyApi::class.java)

        tvMainText?.text = "Загрузка..."
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getDailyText(currentDateStr)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val dailyText = response.body()
                        tvMainText?.text = dailyText?.mainText ?: "Нет данных для этой даты"
                        tvComment?.text = dailyText?.comment ?: ""
                        tvComment?.visibility = View.GONE
                    } else {
                        tvMainText?.text = "Ошибка сервера: ${response.code()}"
                        Toast.makeText(requireContext(), "Ошибка сервера: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    tvMainText?.text = "Ошибка сети. Проверьте подключение."
                    Toast.makeText(requireContext(), "Ошибка сети: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    tvMainText?.text = "Ошибка HTTP: ${e.code()}"
                    Toast.makeText(requireContext(), "Ошибка HTTP: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvMainText?.text = "Неизвестная ошибка"
                    Toast.makeText(requireContext(), "Неизвестная ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onThemeApplied() {
        super.onThemeApplied()
        btnComment?.setBackgroundColor(ThemeManager.getButtonBackgroundColor())
        btnComment?.setTextColor(ThemeManager.getButtonTextColor())
        btnToday?.setBackgroundColor(ThemeManager.getButtonBackgroundColor())
        btnToday?.setTextColor(ThemeManager.getButtonTextColor())
        tvDate?.setTextColor(ThemeManager.getTextColor())
        tvMainText?.setTextColor(ThemeManager.getTextColor())
        tvComment?.setTextColor(ThemeManager.getTextSecondaryColor())
    }

    companion object {
        fun newInstance(): ChashaSakiFragment {
            return ChashaSakiFragment()
        }
    }
}
