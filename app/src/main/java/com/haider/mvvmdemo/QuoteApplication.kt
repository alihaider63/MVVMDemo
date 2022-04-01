package com.haider.mvvmdemo

import android.app.Application
import androidx.work.*
import com.haider.mvvmdemo.api.QuoteService
import com.haider.mvvmdemo.api.RetrofitHelper
import com.haider.mvvmdemo.db.QuoteDatabase
import com.haider.mvvmdemo.repository.QuoteRepository
import com.haider.mvvmdemo.worker.QuoteWorker
import java.util.concurrent.TimeUnit

class QuoteApplication : Application() {

    lateinit var repository: QuoteRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
        setupWorker()
    }

    private fun setupWorker() {
        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest = PeriodicWorkRequest.Builder(QuoteWorker::class.java,30, TimeUnit.MINUTES)
            .setConstraints(constraint)
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)

    }

    private fun initialize() {
        val quoteService = RetrofitHelper.getInstance().create(QuoteService::class.java)
        val database: QuoteDatabase = QuoteDatabase.getDatabase(applicationContext)
        repository = QuoteRepository(quoteService,database,applicationContext)
    }
}