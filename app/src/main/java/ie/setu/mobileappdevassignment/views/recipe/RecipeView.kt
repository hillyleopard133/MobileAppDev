package ie.setu.mobileappdevassignment.views.recipe

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.mobileappdevassignment.R
import ie.setu.mobileappdevassignment.adapters.IngredientAdapter
import ie.setu.mobileappdevassignment.databinding.ActivityRecipeBinding
import ie.setu.mobileappdevassignment.models.IngredientModel
import ie.setu.mobileappdevassignment.models.RecipeModel
import timber.log.Timber.i

class RecipeView : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    private lateinit var presenter: RecipePresenter
    var recipe = RecipeModel()
    var ingredient = IngredientModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = RecipePresenter(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = IngredientAdapter(presenter.recipe.ingredients)

        binding.ingredientAmount.minValue = 0
        binding.ingredientAmount.maxValue = 1000
        binding.ingredientAmount.value = 0

        // Spinner setup
        val unitAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.units_array,
            android.R.layout.simple_spinner_item
        )

        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.ingredientUnit.adapter = unitAdapter

        binding.chooseImage.setOnClickListener {
            presenter.cachePlacemark(binding.recipeTitle.text.toString(), binding.recipeDescription.text.toString())
            presenter.doSelectImage()
        }

        binding.recipeLocation.setOnClickListener {
            presenter.cachePlacemark(binding.recipeTitle.text.toString(), binding.recipeDescription.text.toString())
            presenter.doSetLocation()
        }

        binding.btnAddRecipe.setOnClickListener {
            if (binding.recipeTitle.text.toString().isEmpty()) {
                Snackbar.make(binding.root, R.string.enter_recipe_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                presenter.doAddOrSaveRecipe(
                    title = binding.recipeTitle.text.toString(),
                    description = binding.recipeDescription.text.toString(),
                    vegetarian = binding.checkboxVegetarian.isChecked,
                    vegan = binding.checkboxVegan.isChecked,
                    glutenFree = binding.checkboxGlutenFree.isChecked
                )
            }
        }

        binding.btnAddIngredient.setOnClickListener {
            presenter.doAddIngredient(
                binding.ingredientName.text.toString(),
                binding.ingredientAmount.value,
                binding.ingredientUnit.selectedItem.toString()
            )

            binding.ingredientName.setText("")
            binding.ingredientAmount.value = 0
        }

    }

    fun refreshIngredients() {
        binding.recyclerView.adapter?.notifyItemInserted(
            presenter.recipe.ingredients.size - 1
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_back, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_back -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showRecipe(recipe: RecipeModel) {
        binding.recipeTitle.setText(recipe.title)
        binding.recipeDescription.setText(recipe.description)
        binding.checkboxVegetarian.isChecked = recipe.vegetarian
        binding.checkboxVegan.isChecked = recipe.vegan
        binding.checkboxGlutenFree.isChecked = recipe.glutenFree

        binding.btnAddRecipe.setText(R.string.save_recipe)
        Picasso.get()
            .load(recipe.image)
            .into(binding.recipeImage)
        if (recipe.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_recipe_image)
        }

        binding.btnAddIngredient.setOnClickListener {
            presenter.doAddIngredient(
                binding.ingredientName.text.toString(),
                binding.ingredientAmount.value,
                binding.ingredientUnit.selectedItem.toString()
            )

            binding.ingredientName.setText("")
            binding.ingredientAmount.value = 0
        }

    }

    fun updateImage(image: Uri){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.recipeImage)
        binding.chooseImage.setText(R.string.change_recipe_image)
    }
}