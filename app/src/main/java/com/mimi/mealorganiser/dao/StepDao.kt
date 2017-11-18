package com.mimi.mealorganiser.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.mimi.mealorganiser.pojo.Step

/**
 * Created by Mimi on 01/11/2017.
 * The class responsible for retrieving, adding, editing steps
 */
@Dao
interface StepDao {
    @Query("SELECT * from steps where recipeId = :recipeId")
    fun getAllStepsForRecipe(recipeId: Long):List<Step>

    @Insert
    fun insertAll(vararg steps: Step)

    @Delete
    fun delete(step: Step)

    @Update(onConflict = REPLACE)
    fun updateTask(step: Step)
}
