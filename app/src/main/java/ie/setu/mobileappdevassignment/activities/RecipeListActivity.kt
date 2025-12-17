package ie.setu.mobileappdevassignment.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ie.setu.mobileappdevassignment.R
import ie.setu.mobileappdevassignment.adapters.RecipeAdapter
import ie.setu.mobileappdevassignment.adapters.RecipeListener
import ie.setu.mobileappdevassignment.databinding.ActivityRecipeListBinding
import ie.setu.mobileappdevassignment.main.MainApp
import ie.setu.mobileappdevassignment.models.RecipeModel
import java.util.Locale
import java.util.Locale.getDefault

class RecipeListActivity : AppCompatActivity(), RecipeListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityRecipeListBinding
    private var position: Int = 0

    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRecipeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        val filterSpinner: Spinner = findViewById(R.id.filterType)

        ArrayAdapter.createFromResource(
            this,
            R.array.filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            filterSpinner.adapter = adapter
        }

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        //binding.recyclerView.adapter = RecipeAdapter(app.recipes)
        binding.recyclerView.adapter = RecipeAdapter(app.recipes.findAll(), this)

        binding.btnSearch.setOnClickListener() {
            val searchWord = binding.searchbar.text.toString()
            val searchRecipes = ArrayList<RecipeModel>()
            for(recipe in app.recipes.findAll()){
                if(recipe.title.lowercase(getDefault()).contains(searchWord.lowercase(getDefault())) ||
                    recipe.description.lowercase(getDefault()).contains(searchWord.lowercase(getDefault())) ){
                    val filterType = binding.filterType.selectedItem.toString()
                    if(filterType == "None"){
                        searchRecipes.add(recipe)
                    }else if(filterType == "Vegetarian"){
                        if(recipe.vegetarian){
                            searchRecipes.add(recipe)
                        }
                    }else if(filterType == "Vegan"){
                        if(recipe.vegan){
                            searchRecipes.add(recipe)
                        }
                    }else if(filterType == "Gluten Free"){
                        if(recipe.glutenFree){
                            searchRecipes.add(recipe)
                        }
                    }
                }
            }
            binding.recyclerView.adapter = RecipeAdapter(searchRecipes, this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, RecipeActivity::class.java)
                getResult.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, RecipeMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRecipeClick(recipe: RecipeModel, pos : Int) {
        val launcherIntent = Intent(this, RecipeActivity::class.java)
        launcherIntent.putExtra("recipe_edit", recipe)
        position = pos
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.recipes.findAll().size)
            }
            else{
                if (it.resultCode == 99)     (binding.recyclerView.adapter)?.notifyItemRemoved(position)
            }
        }


    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0,app.recipes.findAll().size)
            }
        }

}

