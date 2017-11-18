package com.mimi.mealorganiser.recipeaddedit

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mimi.mealorganiser.R
import com.mimi.mealorganiser.utils.replaceFragmentInActivity
import org.koin.android.ext.android.inject


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class AddEditRecipeActivity : AppCompatActivity() {
   // private val presenter: AddEditRecipeContract.Presenter by inject()
    private val fragment: AddEditRecipeFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_recipe)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.findFragmentById(R.id.contentFrame) as AddEditRecipeFragment?
                ?: fragment.also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fragment.onActivityResult(requestCode, resultCode, data)
    }
}