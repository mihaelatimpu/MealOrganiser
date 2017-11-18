package com.mimi.mealorganiser.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.mimi.mealorganiser.pojo.Recipe

/**
 * Created by Mimi on 01/11/2017.
 *
 */
@Dao
interface RecipeDao {
    @Insert
    fun insertAll(vararg recipes: Recipe)

    @Query("SELECT * from recipes")
    fun selectAll(): List<Recipe>
}