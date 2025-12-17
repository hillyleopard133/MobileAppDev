package ie.setu.mobileappdevassignment.activities

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.setu.mobileappdevassignment.main.MainApp
import ie.setu.mobileappdevassignment.models.RecipeModel
import timber.log.Timber.i
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.mobileappdevassignment.R
import ie.setu.mobileappdevassignment.adapters.IngredientAdapter
import ie.setu.mobileappdevassignment.databinding.ActivityRecipeBinding
import ie.setu.mobileappdevassignment.models.IngredientModel

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    var recipe = RecipeModel()
    var ingredient = IngredientModel()

    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
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

        var edit = false

        if (intent.hasExtra("recipe_edit")) {
            edit = true
            recipe = intent.extras?.getParcelable("recipe_edit")!!

            binding.recipeTitle.setText(recipe.title)
            binding.recipeDescription.setText(recipe.description)
            binding.checkboxVegetarian.isChecked = recipe.vegetarian
            binding.checkboxVegan.isChecked = recipe.vegan
            binding.checkboxGlutenFree.isChecked = recipe.glutenFree

            //TODO load ingredients

            binding.btnAddRecipe.setText(R.string.save_recipe)
        }

        binding.btnAddRecipe.setOnClickListener() {
            recipe.title = binding.recipeTitle.text.toString()
            recipe.description = binding.recipeDescription.text.toString()
            recipe.vegetarian = binding.checkboxVegetarian.isChecked
            recipe.vegan = binding.checkboxVegan.isChecked
            recipe.glutenFree= binding.checkboxGlutenFree.isChecked
            if (recipe.title.isNotEmpty() && recipe.description.isNotEmpty()) {
                if(edit){
                    app.recipes.update(recipe.copy())
                }else{
                    app.recipes.create(recipe.copy())
                }
                app.saveRecipes()
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,R.string.enter_recipe_title, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

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
                    .make(it,R.string.enter_ingredient_name_and_amount, Snackbar.LENGTH_LONG)
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