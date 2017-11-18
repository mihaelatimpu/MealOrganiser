package com.mimi.mealorganiser

import android.app.Application
import android.arch.persistence.room.Room
import com.mimi.mealorganiser.dao.RecipeDao
import com.mimi.mealorganiser.database.Database
import org.koin.android.ext.android.startAndroidContext

/**
 * Created by Mimi on 06/11/2017.
 *
 */
class MainApplication : Application(){
    lateinit var recipeDb:RecipeDao

    override fun onCreate() {
        super.onCreate()

        val database = Room.databaseBuilder(this,
                Database::class.java, "recipe_db").build()
        recipeDb = database.recipeDao()
        // Start Koin
        startAndroidContext(this, appModules())
    }
}