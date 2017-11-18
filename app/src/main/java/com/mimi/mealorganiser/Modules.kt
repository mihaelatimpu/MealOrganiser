package com.mimi.mealorganiser

import com.mimi.mealorganiser.Context.AddEditRecipe
import com.mimi.mealorganiser.recipeaddedit.AddEditRecipeContract
import com.mimi.mealorganiser.recipeaddedit.AddEditRecipeFragment
import com.mimi.mealorganiser.recipeaddedit.AddEditRecipePresenter
import org.koin.android.module.AndroidModule

/**
 * Created by Mimi on 06/11/2017.
 * All the modules used for Koin
 */
fun appModules() = listOf(RecipeModule(), RepositoryModule())
class RecipeModule:AndroidModule(){
    override fun context() = applicationContext {
        context(AddEditRecipe){
            provide { AddEditRecipeFragment() }
            provide { AddEditRecipePresenter(get()) } bind AddEditRecipeContract.Presenter::class
        }
    }
}
/**
 * Module constants
 */
object Context {
    val Recipes = "Recipes"
    val RecipeDetail = "RecipeDetail"
    val AddEditRecipe = "AddEditRecipe"
}