package com.mimi.mealorganiser.pojo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Relation

/**
 * Created by Mimi on 01/11/2017.
 * This class will contain the main Recipe object
 */
@Entity(tableName = "recipe")
class Recipe {
    companion object {
        fun createRecipe(mTitle: String, mDuration: Long,
                         mDescription: String, mIngredients: ArrayList<String>,
                         mSteps: ArrayList<Step>): Recipe {
            val recipe = Recipe()
            with(recipe) {
                name = mTitle
                description = mDescription
                timeInMillis = mDuration
                steps = mSteps
                ingredients = mIngredients.map { Ingredient(id = -1, name = it) }
            }
            return recipe
        }
    }

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var name: String = ""

    var description: String = ""

    var timeInMillis: Long = 0

    var ingredients: List<Ingredient> = listOf()

    @Relation(entity = Step::class, parentColumn = "id", entityColumn = "recipeId")
    var steps: List<Step> = listOf()

}