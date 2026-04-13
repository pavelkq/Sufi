package com.pasha.sufi

import android.app.Application
import android.content.Context
import com.pasha.sufi.theme.ThemeManager

class ArticleApplication : Application() {

    companion object {
        private lateinit var instance: ArticleApplication

        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Инициализируем тему
        ThemeManager.init(this)

        // TODO: Восстановление сессии если нужно
    }
}