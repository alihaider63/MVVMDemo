package com.haider.mvvmdemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haider.mvvmdemo.models.QuoteList
import com.haider.mvvmdemo.repository.QuoteRepository
import com.haider.mvvmdemo.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuoteViewModel(private val repository: QuoteRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuotes(1)
        }
    }

    val quotes: LiveData<Response<QuoteList>>
    get() = repository.quotes

}