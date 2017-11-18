package com.mimi.mealorganiser.pojo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Mimi on 01/11/2017.
 * The ingredients added inside a recipe
 */
@Entity(tableName = "Ingredient")
class Ingredient(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val name: String)
