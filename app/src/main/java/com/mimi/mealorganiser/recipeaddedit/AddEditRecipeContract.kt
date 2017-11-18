package com.mimi.mealorganiser.recipeaddedit

import android.content.Context
import android.graphics.Bitmap
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import com.mimi.mealorganiser._base.BasePresenter
import com.mimi.mealorganiser._base.BaseView
import com.mimi.mealorganiser.pojo.Step

/**
 * Created by Mimi on 02/11/2017.
 *
 */
interface AddEditRecipeContract {
    interface View : BaseView<Presenter> {
        var isActive: Boolean
        fun refreshImage(bitmap: Bitmap)
        fun refreshIngredients(item: List<String>)
        fun refreshSteps(item: List<Step>)
        fun startImagePickerIntent()
        fun showAddDialog(title: String, inputText: String? = null, isOk: (String) -> Boolean,
                          onConfirm: (String) -> Unit)

        fun showConfirmDialog(title: String, message: String, onConfirm: () -> Unit)
        fun getText(@StringRes res: Int, @StringRes param: Int? = null): String
        fun getTitle(): String
        fun getTime(): String
        fun getDescription():String
        fun showTitleError(@StringRes error:Int?)
        fun showTimeError(@StringRes error:Int?)
        fun changeSaveFabColor(@ColorRes res:Int)
        fun getTimeType():Int
        fun clearAll()
        fun toast(@StringRes text:Int)
    }

    interface Presenter : BasePresenter<View> {
        fun addImage()
        fun onImagePicked(bitmap: Bitmap)
        fun addIngredient()
        fun addStep()
        fun editStep(step: Step)
        fun editIngredient(ingredient: String)
        fun deleteStep(step: Step)
        fun deleteIngredient(ingredient: String)
        fun onStepOrderChanged(newSteps: ArrayList<Step>)
        fun saveRecipe()
        fun onInputTextChanged()
    }
}