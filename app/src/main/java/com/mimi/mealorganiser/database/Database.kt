package com.mimi.mealorganiser.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mimi.mealorganiser.dao.RecipeDao
import com.mimi.mealorganiser.dao.StepDao
import com.mimi.mealorganiser.pojo.Ingredient
import com.mimi.mealorganiser.pojo.Recipe
import com.mimi.mealorganiser.pojo.Step

/**
 * Created by Mimi on 01/11/2017.
 *
 */
@Database(entities = arrayOf(Recipe::class, Ingredient::class, Step::class), version = 1)
abstract class Database : RoomDatabase() {
    abstract fun stepDao(): StepDao
    abstract fun recipeDao():RecipeDao
}