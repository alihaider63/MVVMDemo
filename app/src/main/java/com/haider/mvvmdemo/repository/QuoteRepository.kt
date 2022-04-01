package com.haider.mvvmdemo.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.haider.mvvmdemo.api.QuoteService
import com.haider.mvvmdemo.db.QuoteDatabase
import com.haider.mvvmdemo.models.QuoteList
import com.haider.mvvmdemo.utils.NetworkUtils.Companion.isInternetAvailable
import java.lang.Exception

class QuoteRepository(private val quoteService: QuoteService,
                      private val quoteDatabase: QuoteDatabase,
                      private val applicationContext: Context
) {

    private var quotesLiveData = MutableLiveData<Response<QuoteList>>()

    val quotes: LiveData<Response<QuoteList>>
        get() = quotesLiveData

    suspend fun getQuotes(page: Int) {
        if (isInternetAvailable(applicationContext)) {
            try {
                val result = quoteService.getQuotes(page)
                if (result?.body() != null) {
                    quotesLiveData.postValue(Response.Success(result.body()))
                }
            } catch (e: Exception) {
                quotesLiveData.postValue(Response.Error(e.message.toString()))
            }
        } else {
            try {
                val result = quoteDatabase.quoteDao().getQuotes()
                val quoteList = QuoteList(1,1,1,result,1,1)
                quotesLiveData.postValue(Response.Success(quoteList))

            } catch (e: Exception) {
                quotesLiveData.postValue(Response.Error(e.message.toString()))
            }
        }

    }


    suspend fun getQuotesFromBackground() {
        val randomNumber = (Math.random() * 10).toInt()
        if (isInternetAvailable(applicationContext)) {
            try {
                val result = quoteService.getQuotes(randomNumber)
                if (result?.body() != null) {
                    quotesLiveData.postValue(Response.Success(result.body()))
                    val quoteList = result.body()
                    val quoteResult = quoteList?.results
                    if (quoteResult != null) {
                        quoteDatabase.quoteDao().addQuotes(quoteResult)
                    }
                } else {
                    quotesLiveData.postValue(Response.Error("null"))
                }
            } catch (e: Exception) {
                quotesLiveData.postValue(Response.Error(e.message.toString()))
            }
        }
    }
}