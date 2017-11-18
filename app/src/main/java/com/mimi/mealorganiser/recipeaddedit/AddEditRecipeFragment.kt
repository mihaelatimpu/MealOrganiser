package com.mimi.mealorganiser.recipeaddedit

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.mimi.mealorganiser.Context
import com.mimi.mealorganiser.R
import com.mimi.mealorganiser._base.BaseFragment
import com.mimi.mealorganiser.pojo.Step
import com.mimi.mealorganiser.recipeaddedit.adapter.IngredientsAdapter
import com.mimi.mealorganiser.recipeaddedit.adapter.StepsAdapter
import com.mimi.mealorganiser.utils.SimpleItemTouchHelperCallback
import com.mimi.mealorganiser.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_add_edit.*
import kotlinx.android.synthetic.main.item_expand_parent.view.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import org.koin.android.ext.android.inject

/**
 * Created by Mimi on 08/11/2017.
 * Main UI for adding a recipe or editing an existing one
 */
class AddEditRecipeFragment : BaseFragment(), AddEditRecipeContract.View {
    companion object {
        private val CHOOSE_IMAGE_CODE = 43432
    }

    override val contextName = Context.AddEditRecipe
    override val presenter: AddEditRecipeContract.Presenter by inject()
    override var isActive = false
        get() = isAdded

    private val ingredientsAdapter by lazy {
        IngredientsAdapter(
                onAdd = { presenter.addIngredient() },
                editItem = { presenter.editIngredient(it) },
                deleteItem = { presenter.deleteIngredient(it) }
        )
    }
    private val stepsAdapter by lazy {
        StepsAdapter(
                onAdd = { presenter.addStep() },
                editItem = { presenter.editStep(it) },
                deleteItem = { presenter.deleteStep(it) },
                onOrderChanged = { presenter.onStepOrderChanged(it) }
        )
    }

    override fun onResume() {
        super.onResume()
        presenter.view = this
        presenter.start()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(activity.findViewById<FloatingActionButton>(R.id.fab_edit_task_done)) {
            setImageResource(R.mipmap.ic_done_white_24dp)
            setOnClickListener {
                presenter.saveRecipe()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit, container, false)
    }

    override fun init() {
        initSteps()
        initIngredients()
        setOnTextChangedListeners()
        addImageButton.setOnClickListener { presenter.addImage() }
    }
    private fun setOnTextChangedListeners(){
        val textChangedListener = object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                presenter.onInputTextChanged()
            }

            override fun beforeTextChanged(s: CharSequence?,
                                           start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?,
                                       start: Int, before: Int, count: Int) {}

        }
        titleInput.editText?.addTextChangedListener(textChangedListener)
        timeInput.editText?.addTextChangedListener(textChangedListener)
    }

    private fun initSteps() {
        stepsList.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        stepsList.adapter = stepsAdapter
        val callback = SimpleItemTouchHelperCallback(stepsAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(stepsList)
        initStepsToggle()
    }

    private fun initIngredients() {
        ingredientsList.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        ingredientsList.adapter = ingredientsAdapter
        initIngredientsToggle()

    }


    override fun getText(res: Int, param: Int?): String {
        return if (param == null) getString(res)
        else getString(res, getString(param))

    }

    override fun refreshImage(bitmap: Bitmap) {
        image.setImageBitmap(bitmap)
    }

    override fun getTimeType() = spinner.selectedItemPosition

    override fun showAddDialog(title: String, inputText: String?,
                               isOk: (String) -> Boolean, onConfirm: (String) -> Unit) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        val input = EditText(activity)
        if (inputText != null) {
            input.append(inputText)
        }
        input.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        input.inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
        input.layoutParams = lp
        builder.setView(input)
        builder.setPositiveButton(R.string.confirm, { _, _ ->
            val text = input.text.toString()
            when {
                text.isEmpty() or text.isBlank() ->
                    toast(getString(R.string.please_enter_text))
                !isOk(text) ->
                    toast(getString(R.string.this_item_is_already_there))
                else -> {
                    input.hideKeyboard()
                    onConfirm(text)
                }
            }
        })
        builder.setNegativeButton(R.string.cancel, null)
        builder.create().show()
        input.setSelection(input.text.length)
    }

    override fun showConfirmDialog(title: String, message: String, onConfirm: () -> Unit) {
        alert(title = title, message = message) {
            yesButton { onConfirm() }
            noButton { }
        }.show()
    }

    override fun toast(text: Int) {
        toast(getText(text))
    }

    override fun clearAll() {
        showError(null,titleInput)
        title.text.clear()
        showError(null,descriptionInput)
        description.text.clear()
        showError(null,timeInput)
        time.text.clear()
        spinner.setSelection(0)

    }

    override fun startImagePickerIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true) //added snippet
        activity.startActivityForResult(Intent.createChooser(intent,
                getString(R.string.select_picture)), CHOOSE_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("BitmapTrack", "result: $resultCode, bitmap null: ${data == null}")
        if (requestCode == CHOOSE_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            extractBitmapFromIntent(data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun extractBitmapFromIntent(data: Intent) {
        try {
            val imageUri = data.data
            val imageStream = activity.contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            imageStream.close()
            presenter.onImagePicked(selectedImage)
        } catch (e: Exception) {
            toast("error: " + e.message)
            e.printStackTrace()
        }
    }

    private fun initStepsToggle() {
        stepsTitle.label.text = getString(R.string.steps)
        stepsTitle.setOnClickListener {
            if (stepsList.visibility == View.VISIBLE) {
                collapseView(stepsTitle.arrow, stepsList)
            } else {
                expandView(stepsTitle.arrow, stepsList)
            }
        }
    }

    private fun initIngredientsToggle() {
        ingredientsTitle.label.text = getString(R.string.ingredients)
        ingredientsTitle.setOnClickListener {
            if (ingredientsList.visibility == View.VISIBLE) {
                collapseView(ingredientsTitle.arrow, ingredientsList)
            } else {
                expandView(ingredientsTitle.arrow, ingredientsList)
            }
        }
    }

    private fun expandView(arrow: ImageView, list: RecyclerView) {
        arrow.setImageResource(R.mipmap.ic_keyboard_arrow_up_white_24dp)
        list.visibility = View.VISIBLE

    }

    private fun collapseView(arrow: ImageView, list: RecyclerView) {
        arrow.setImageResource(R.mipmap.ic_keyboard_arrow_down_white_24dp)
        list.visibility = View.GONE

    }

    override fun refreshIngredients(item: List<String>) {
        ingredientsAdapter.refreshItems(item)
    }

    override fun refreshSteps(item: List<Step>) {
        stepsAdapter.refreshItems(item)
    }

    override fun changeSaveFabColor(res: Int) {

        with(activity.findViewById<FloatingActionButton>(R.id.fab_edit_task_done)) {
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity,res))
        }
    }
    override fun getTitle() = titleInput.editText!!.text.toString()
    override fun getDescription() = descriptionInput.editText!!.text.toString()
    override fun getTime() = timeInput.editText!!.text.toString()
    override fun showTimeError(error: Int?) {
        showError(error, timeInput)
    }

    override fun showTitleError(error: Int?) {
        showError(error, titleInput)
    }

    private fun showError(@StringRes error: Int?, layout: TextInputLayout) {
        if (error == null) {
            layout.isErrorEnabled = false
        } else {
            layout.isErrorEnabled = true
            layout.error = getString(error)
        }
    }

}
