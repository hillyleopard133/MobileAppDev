package ie.setu.mobileappdevassignment.main

import android.app.Application
import ie.setu.mobileappdevassignment.models.RecipeJSONStore
import ie.setu.mobileappdevassignment.models.RecipeMemStore
import ie.setu.mobileappdevassignment.models.RecipeStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    lateinit var recipes: RecipeStore

    private val fileName = "recipes.json"

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        recipes = RecipeJSONStore(applicationContext)
        //recipes = RecipeMemStore()
        i("Recipes started")

    }
}