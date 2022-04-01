package com.haider.mvvmdemo.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.haider.mvvmdemo.QuoteApplication
import com.haider.mvvmdemo.repository.QuoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuoteWorker(private val context: Context, private val params: WorkerParameters) : Worker(context,params) {
    override fun doWork(): Result {
        val repository = (context as QuoteApplication).repository
        CoroutineScope(Dispatchers.IO).launch {
            repository.getQuotesFromBackground()
        }
        return Result.success()
    }

}