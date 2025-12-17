package ie.setu.mobileappdevassignment.views.recipe

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ie.setu.mobileappdevassignment.main.MainApp
import ie.setu.mobileappdevassignment.models.IngredientModel
import ie.setu.mobileappdevassignment.models.Location
import ie.setu.mobileappdevassignment.models.RecipeModel
import ie.setu.mobileappdevassignment.views.editlocation.EditLocationView
import timber.log.Timber

class RecipePresenter (private val view: RecipeView) {
    var recipe = RecipeModel()
    var app: MainApp = view.application as MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false

    init {
        if (view.intent.hasExtra("recipe_edit")) {
            edit = true
            recipe = view.intent.extras?.getParcelable("recipe_edit")!!
            view.showRecipe(recipe)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    fun doAddOrSaveRecipe(title: String, description: String, vegetarian: Boolean, vegan: Boolean, glutenFree: Boolean) {
        recipe.title = title
        recipe.description = description
        recipe.vegetarian = vegetarian
        recipe.vegan = vegan
        recipe.glutenFree = glutenFree

        if (edit) {
            app.recipes.update(recipe)
        } else {
            app.recipes.create(recipe)
        }
        view.setResult(Activity.RESULT_OK)
        view.finish()
    }

    fun doAddIngredient(name: String, amount: Int, unit: String) {
        if (name.isNotEmpty() && amount > 0) {
            recipe.ingredients.add(
                IngredientModel(name, amount, unit)
            )
            view.refreshIngredients()
        }
    }

    fun doCancel() {
        view.finish()
    }

    fun doDelete() {
        view.setResult(99)
        app.recipes.delete(recipe)
        view.finish()
    }

    fun doSelectImage() {
        //   showImagePicker(imageIntentLauncher,view)
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
            .build()
        imageIntentLauncher.launch(request)
    }

    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (recipe.zoom != 0f) {
            location.lat =  recipe.lat
            location.lng = recipe.lng
            location.zoom = recipe.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)

        mapIntentLauncher.launch(launcherIntent)
    }

    fun cachePlacemark (title: String, description: String) {
        recipe.title = title
        recipe.description = description
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher = view.registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {
            try{
                view.contentResolver
                    .takePersistableUriPermission(it!!,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION )
                recipe.image = it // The returned Uri
                Timber.i("IMG :: ${recipe.image}")
                view.updateImage(recipe.image)
            }
            catch(e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            //val location = result.data!!.extras?.getParcelable("location",Location::class.java)!!
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            recipe.lat = location.lat
                            recipe.lng = location.lng
                            recipe.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}