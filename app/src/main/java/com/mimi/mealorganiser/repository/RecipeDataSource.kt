package com.mimi.mealorganiser.repository

import com.mimi.mealorganiser.pojo.Recipe

/**
 * Created by Mimi on 15/11/2017.
 *
 */
interface RecipeDataSource {

    interface LoadRecipesCallback {

        fun onRecipesLoaded(recipes: List<Recipe>)

        fun onDataNotAvailable()
    }

    interface GetRecipeCallback {

        fun onRecipeLoaded(recipe: Recipe)

        fun onDataNotAvailable()
    }

    fun saveRecipe(recipe: Recipe)
}