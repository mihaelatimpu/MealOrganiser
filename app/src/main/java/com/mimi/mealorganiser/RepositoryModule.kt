package com.mimi.mealorganiser

import com.mimi.mealorganiser.database.Database
import org.koin.android.module.AndroidModule
import android.arch.persistence.room.Room
import com.mimi.mealorganiser.dao.RecipeDao
import com.mimi.mealorganiser.repository.RecipeDataSource
import com.mimi.mealorganiser.repository.RecipeRepository


/**
 * Created by Mimi on 15/11/2017.
 *
 */
class RepositoryModule : AndroidModule() {
    override fun context() = applicationContext {
        provide(name = "localDataSource") {
            getRecipeDb(applicationContext)
        }

        provide {
            RecipeRepository(getRecipeDb(applicationContext))
        } bind (RecipeDataSource::class)
    }

    private fun getRecipeDb(context: android.content.Context): RecipeDao {

       /* val database = Room.databaseBuilder(context,
                Database::class.java, "recipe_db").build()
        return database.recipeDao()*/
        return ( context.applicationContext as MainApplication).recipeDb
    }
}