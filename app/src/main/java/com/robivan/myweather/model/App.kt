package com.robivan.myweather.model

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.robivan.myweather.room.HistoryDao
import com.robivan.myweather.room.HistoryDataBase
import java.lang.IllegalStateException

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
        appContext = applicationContext
    }

    companion object {
        var appContext: Context? = null
            private set
        private var appInstance: App? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        fun getHistoryDao() : HistoryDao {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(appInstance!!.applicationContext, HistoryDataBase::class.java, DB_NAME).allowMainThreadQueries().build()
                    }
                }
            }
            return db!!.historyDao()
        }
    }
}