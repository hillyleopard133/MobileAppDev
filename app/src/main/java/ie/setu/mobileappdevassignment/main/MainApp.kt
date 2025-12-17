package ie.setu.mobileappdevassignment.main

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ie.setu.mobileappdevassignment.models.RecipeModel
import ie.setu.mobileappdevassignment.models.RecipeMemStore
import ie.setu.mobileappdevassignment.models.RecipeStore
import timber.log.Timber
import timber.log.Timber.i
import java.io.File

class MainApp : Application() {
    lateinit var recipes: RecipeStore

    private val fileName = "recipes.json"

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        recipes = RecipeMemStore()
        i("Recipes started")

        //recipes.addAll(loadRecipes(this))
    }

    /*

    // Save recipes list to internal storage
    fun saveRecipes() {
        val file = File(filesDir, fileName)
        try {
            val gson = Gson()
            val jsonString = gson.toJson(recipes.recipes)
            file.writeText(jsonString)
            i("Recipes saved successfully")
        } catch (e: Exception) {
            i("Error saving recipes: ${e.message}")
        }
    }

    // Load recipes list from internal storage
    private fun loadRecipes(context: Context): List<RecipeModel> {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) return emptyList()

        return try {
            val jsonString = file.readText()
            val gson = Gson()
            val type = object : TypeToken<ArrayList<RecipeModel>>() {}.type
            gson.fromJson<ArrayList<RecipeModel>>(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            i("Error loading recipes: ${e.message}")
            emptyList()
        }
    }

     */
}