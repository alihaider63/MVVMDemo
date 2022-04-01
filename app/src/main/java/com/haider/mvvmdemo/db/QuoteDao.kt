package com.haider.mvvmdemo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.haider.mvvmdemo.models.Result
import retrofit2.Response

@Dao
interface QuoteDao {

    @Insert
    suspend fun addQuotes(quotes: List<Result>)

    @Query("SELECT * FROM quote")
    suspend fun getQuotes() : List<Result>

}