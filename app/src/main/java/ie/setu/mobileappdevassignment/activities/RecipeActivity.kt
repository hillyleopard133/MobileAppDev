package ie.setu.mobileappdevassignment.activities

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import ie.setu.mobileappdevassignment.R
import ie.setu.mobileappdevassignment.adapters.IngredientAdapter
import ie.setu.mobileappdevassignment.databinding.ActivityRecipeBinding
import ie.setu.mobileappdevassignment.helpers.showImagePicker
import ie.setu.mobileappdevassignment.models.IngredientModel
import ie.setu.mobileappdevassignment.models.Location

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    var recipe = RecipeModel()
    var ingredient = IngredientModel()
    var edit = false

    lateinit var app : MainApp

    private lateinit var imageIntentLauncher : ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            recipe.lat = location.lat
                            recipe.lng = location.lng
                            recipe.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }



    private fun registerImagePickerCallback() {
        imageIntentLauncher = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {
            try{
                contentResolver
                    .takePersistableUriPermission(it!!,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION )
                recipe.image = it // The returned Uri
                i("IMG :: ${recipe.image}")
                Picasso.get()
                    .load(recipe.image)
                    .into(binding.recipeImage)
            }
            catch(e:Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerImagePickerCallback()
        registerMapCallback()

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

        if (intent.hasExtra("recipe_edit")) {
            edit = true
            recipe = intent.extras?.getParcelable("recipe_edit")!!

            binding.recipeTitle.setText(recipe.title)
            binding.recipeDescription.setText(recipe.description)
            binding.checkboxVegetarian.isChecked = recipe.vegetarian
            binding.checkboxVegan.isChecked = recipe.vegan
            binding.checkboxGlutenFree.isChecked = recipe.glutenFree

            Picasso.get()
                .load(recipe.image)
                .into(binding.recipeImage)

            if (recipe.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_recipe_image)
            }

            binding.recyclerView.adapter = IngredientAdapter(recipe.ingredients)

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
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0,recipe.ingredients.size)
            }
            else {
                Snackbar
                    .make(it,R.string.enter_ingredient_name_and_amount, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.chooseImage.setOnClickListener {
            val request = PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                .build()
            imageIntentLauncher.launch(request)
        }


        binding.recipeLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (recipe.zoom != 0f) {
                location.lat =  recipe.lat
                location.lng = recipe.lng
                location.zoom = recipe.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_back, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99)
                app.recipes.delete(recipe)
                finish()
            }        R.id.item_back -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }



    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
}