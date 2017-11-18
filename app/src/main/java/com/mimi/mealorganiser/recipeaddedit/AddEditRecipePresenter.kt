package com.mimi.mealorganiser.recipeaddedit

import android.graphics.Bitmap
import com.mimi.mealorganiser.R
import com.mimi.mealorganiser.pojo.Recipe
import com.mimi.mealorganiser.pojo.Step
import com.mimi.mealorganiser.repository.RecipeDataSource
import com.mimi.mealorganiser.utils.TimeUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 02/11/2017.
 * This presenter is attached to the Recipe activity.
 */
class AddEditRecipePresenter(private val dataSource: RecipeDataSource)
    : AddEditRecipeContract.Presenter {

    override lateinit var view: AddEditRecipeContract.View
    private val ingredients = arrayListOf<String>()
    private val steps = arrayListOf<Step>()
    private var bitmap: Bitmap? = null
    private val timeUtil = TimeUtils()

    override fun addImage() {
        if (view.isActive)
            view.startImagePickerIntent()
    }

    override fun onImagePicked(bitmap: Bitmap) {
        this.bitmap = bitmap
        if (view.isActive)
            view.refreshImage(bitmap)
    }

    override fun start() {
        if (view.isActive)
            view.init()
    }

    override fun editIngredient(ingredient: String) {
        if (!view.isActive)
            return
        view.showAddDialog(title = view.getText(R.string.edit, R.string.ingredient),
                inputText = ingredient,
                isOk = {
                    doesNotContainValue(item = it, oldItem = ingredient,
                            list = ingredients)
                }, onConfirm = {
            ingredients.remove(ingredient)
            ingredients.add(it)
            refreshIngredients()
        })
    }

    override fun addIngredient() {
        if (!view.isActive)
            return
        view.showAddDialog(
                title = view.getText(R.string.add_new, R.string.ingredient),
                isOk = {
                    doesNotContainValue(item = it, list = ingredients)
                },
                onConfirm = {
                    ingredients.add(it)
                    refreshIngredients()
                })
    }

    override fun deleteIngredient(ingredient: String) {
        if (!view.isActive)
            return
        view.showConfirmDialog(
                title = view.getText(R.string.are_you_sure, R.string.ingredient),
                message = view.getText(R.string.you_can_add_it_back_later)) {
            ingredients.remove(ingredient)
            refreshIngredients()
        }
    }

    private fun refreshIngredients() {
        if (!view.isActive)
            return
        view.refreshIngredients(ingredients)
    }

    override fun addStep() {
        if (!view.isActive)
            return
        view.showAddDialog(
                title = view.getText(R.string.add_new, R.string.step),
                isOk = {
                    doesNotContainValue(item = it,
                            list = steps.map { it.text })
                },
                onConfirm = {
                    steps.add(Step(order = steps.size, text = it))
                    refreshSteps()
                })
    }

    override fun editStep(step: Step) {
        if (!view.isActive)
            return
        view.showAddDialog(
                title = view.getText(R.string.edit_item, R.string.step),
                inputText = step.text,
                isOk = {
                    doesNotContainValue(item = it, oldItem = step.text,
                            list = steps.map { it.text })
                },
                onConfirm = {
                    step.text = it
                    steps.remove(step)
                    steps.add(step)
                    refreshSteps()
                })
    }

    private fun doesNotContainValue(item: String, oldItem: String? = null,
                                    list: List<String>): Boolean {
        return (oldItem != null && item == oldItem) || (list.none { it == item })
    }

    override fun deleteStep(step: Step) {
        if (!view.isActive)
            return
        view.showConfirmDialog(
                title = view.getText(R.string.are_you_sure, R.string.step),
                message = view.getText(R.string.you_can_add_it_back_later)) {
            steps.remove(step)
            refreshSteps()
        }

    }

    override fun onStepOrderChanged(newSteps: ArrayList<Step>) {
        var position = 0
        newSteps.forEach {
            it.order = position++
        }
        steps.clear()
        steps.addAll(newSteps)
        refreshSteps()
    }

    private fun refreshSteps() {
        if (!view.isActive)
            return
        val sortedList = steps.sortedBy { it.order }
        view.refreshSteps(sortedList)

    }

    override fun saveRecipe() {
        if (!view.isActive)
            return
        val title = view.getTitle()
        if (title.isEmpty()) {
            view.showTitleError(R.string.please_enter_text)
            return
        }
        if (view.getTime().isEmpty()) {
            view.showTimeError(R.string.please_enter_text)
            return
        }
        val timeinMillis =
                try {
                    timeUtil.convertToMillis(view.getTime(), view.getTimeType())
                } catch (e: Exception) {
                    view.showTimeError(R.string.invalid_time_error)
                    return
                }
        view.showLoadingDialog()
        doAsync {
            val recipe = Recipe.createRecipe(mTitle = title,
                    mDescription = view.getDescription(),
                    mDuration = timeinMillis,
                    mIngredients = ingredients,
                    mSteps = steps)
            dataSource.saveRecipe(recipe)
            onComplete {
                view.hideLoadingDialog()
                view.toast(R.string.new_recipe_added)
                clearAll()
            }
        }
    }
    private fun clearAll(){
        steps.clear()
        refreshSteps()
        ingredients.clear()
        refreshIngredients()
        view.clearAll()
    }

    override fun onInputTextChanged() {
        val isAllTextCompleted = view.isActive && !view.getTitle().isEmpty()
                && !view.getTime().isEmpty()
        val color = if (isAllTextCompleted) R.color.colorAccent
        else android.R.color.darker_gray
        view.changeSaveFabColor(color)
    }


}
