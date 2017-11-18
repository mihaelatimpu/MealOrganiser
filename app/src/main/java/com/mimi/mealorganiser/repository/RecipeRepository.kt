package com.mimi.mealorganiser.repository

import com.mimi.mealorganiser.dao.RecipeDao
import com.mimi.mealorganiser.pojo.Recipe

/**
 * Created by Mimi on 15/11/2017.
 *
 */
class RecipeRepository(private val localDb: RecipeDao) : RecipeDataSource {
    override fun saveRecipe(recipe: Recipe) {
        localDb.insertAll(recipe)

    }
}