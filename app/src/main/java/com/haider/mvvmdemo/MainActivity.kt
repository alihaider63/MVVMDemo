package com.haider.mvvmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.haider.mvvmdemo.databinding.ActivityMainBinding
import com.haider.mvvmdemo.models.Result
import com.haider.mvvmdemo.repository.Response
import com.haider.mvvmdemo.viewmodel.QuoteViewModel
import com.haider.mvvmdemo.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {


    lateinit var listOfQuote: List<Result>
    var index = 0
    lateinit var quoteViewModel: QuoteViewModel

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //val quoteService = RetrofitHelper.getInstance().create(QuoteService::class.java)

        //val repository = QuoteRepository(quoteService)
        val repository = (application as QuoteApplication).repository

        quoteViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(QuoteViewModel::class.java)

        quoteViewModel.quotes.observe(this, Observer {
            when(it) {
                is Response.Loading -> {
                    Log.d("AlihaiderLogCat","Loading")
                    Toast.makeText(this,"Loading", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    it.data?.let {
                        listOfQuote = it.results
                        setQuote(listOfQuote[index])                    }
                }
                is Response.Error -> {
                    Log.d("AlihaiderLogCat","Error")
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.next.setOnClickListener {
            onNext()
        }

        binding.previous.setOnClickListener {
            onPrevious()
        }

    }
    private fun onPrevious() {
        if (index != 0) {
            setQuote(listOfQuote[--index])
        } else {
            Toast.makeText(this,"Your are at First",Toast.LENGTH_LONG).show()
        }

    }

    private fun onNext() {
        if (index < listOfQuote.size - 1) {
            setQuote(listOfQuote[++index])
        }
    }

    private fun setQuote(result: Result) {
        binding.quote.text = result.content
        binding.quoteAuthor.text = result.author
    }
}