package com.mimi.mealorganiser.pojo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Mimi on 01/11/2017.
 * The step required to prepare a dish from a recipe.
 * One recipe has one or more steps, one step belongs to only one recipe
 */
@Entity(tableName = "Step",
        foreignKeys = arrayOf(
                ForeignKey(entity = Recipe::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("recipeId"))
        ))
class Step(
        @PrimaryKey(autoGenerate = true)
        val id: Long = -1,
        val recipeId: Long = -1,
        var order: Int,
        var text: String)