package ie.setu.mobileappdevassignment.views.recipelist

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.mobileappdevassignment.main.MainApp
import ie.setu.mobileappdevassignment.models.RecipeModel
import ie.setu.mobileappdevassignment.views.map.RecipeMapView
import ie.setu.mobileappdevassignment.views.recipe.RecipeView

class RecipeListPresenter (val view: RecipeListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var position: Int = 0

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    fun getFilteredRecipes(searchTerm: String = "", filterType: String = "None"): ArrayList<RecipeModel> {
        val recipes = app.recipes.findAll()
        return recipes.filter { recipe ->
            val matchesSearch = recipe.title.contains(searchTerm, ignoreCase = true) ||
                    recipe.description.contains(searchTerm, ignoreCase = true)

            val matchesFilter = when (filterType) {
                "Vegetarian" -> recipe.vegetarian
                "Vegan" -> recipe.vegan
                "Gluten Free" -> recipe.glutenFree
                else -> true
            }

            matchesSearch && matchesFilter
        }.toCollection(ArrayList())
    }


    fun getRecipes() = app.recipes.findAll()

    fun doAddRecipe() {
        val launcherIntent = Intent(view, RecipeView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditRecipe(recipe: RecipeModel, pos: Int) {
        val launcherIntent = Intent(view, RecipeView::class.java)
        launcherIntent.putExtra("recipe_edit", recipe)
        position = pos
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doShowRecipesMap() {
        val launcherIntent = Intent(view, RecipeMapView::class.java)
        mapIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == RESULT_OK) view.onRefresh()
                else // Deleting
                    if (it.resultCode == 99) view.onDelete(position)
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }


}