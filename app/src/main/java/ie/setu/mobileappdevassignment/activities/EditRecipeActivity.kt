package ie.setu.mobileappdevassignment.activities

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ie.setu.mobileappdevassignment.R
import ie.setu.mobileappdevassignment.adapters.IngredientAdapter
import ie.setu.mobileappdevassignment.databinding.ActivityEditRecipeBinding
import ie.setu.mobileappdevassignment.main.MainApp
import ie.setu.mobileappdevassignment.models.IngredientModel
import ie.setu.mobileappdevassignment.models.RecipeModel
import timber.log.Timber.i

class EditRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditRecipeBinding
    var recipe = RecipeModel()
    var ingredient = IngredientModel()

    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        val unitSpinner: Spinner = findViewById(R.id.ingredientUnit)

        ArrayAdapter.createFromResource(
            this,
            R.array.units_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            unitSpinner.adapter = adapter
        }

        app = application as MainApp
        val position = intent.getIntExtra("position", -1)
        recipe = app.recipes[position]
        binding.recipeTitle.setText(recipe.title)
        binding.recipeDescription.setText(recipe.description)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = IngredientAdapter(recipe.ingredients)

        binding.ingredientAmount.minValue = 0
        binding.ingredientAmount.maxValue = 1000
        binding.ingredientAmount.value = 0

        //Make it editable (this was from chatGPT)
        val numberPickerEditText = binding.ingredientAmount.findViewById<EditText>(
            Resources.getSystem().getIdentifier("numberpicker_input", "id", "android")
        )
        numberPickerEditText.isFocusable = true
        numberPickerEditText.isFocusableInTouchMode = true
        numberPickerEditText.inputType = InputType.TYPE_CLASS_NUMBER

        binding.btnAddIngredient.setOnClickListener() {
            ingredient.name = binding.ingredientName.text.toString()
            ingredient.amount = binding.ingredientAmount.value
            ingredient.unit = binding.ingredientUnit.selectedItem.toString()
            if (ingredient.name.isNotEmpty() && ingredient.amount != 0) {
                recipe.ingredients.add(ingredient.copy())
                binding.ingredientName.setText("")
                binding.ingredientAmount.value = 0
                app.saveRecipes()
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0,recipe.ingredients.size)
            }
            else {
                Snackbar
                    .make(it,"Please enter a name and amount", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.btnDone.setOnClickListener() {
            recipe.title = binding.recipeTitle.text.toString()
            recipe.description = binding.recipeDescription.text.toString()
            if (recipe.title.isNotEmpty() && recipe.description.isNotEmpty()) {
                app.saveRecipes()
                val launcherIntent = Intent(this, RecipeListActivity::class.java)
                getResult.launch(launcherIntent)
            }
            else {
                Snackbar
                    .make(it,"Please enter a title and description", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_back, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_back -> {
                val launcherIntent = Intent(this, RecipeListActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
}