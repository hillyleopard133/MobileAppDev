package ie.setu.mobileappdevassignment.views.recipelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.mobileappdevassignment.R
import ie.setu.mobileappdevassignment.adapters.RecipeAdapter
import ie.setu.mobileappdevassignment.adapters.RecipeListener
import ie.setu.mobileappdevassignment.databinding.ActivityRecipeListBinding
import ie.setu.mobileappdevassignment.main.MainApp
import ie.setu.mobileappdevassignment.models.RecipeModel

class RecipeListView : AppCompatActivity(), RecipeListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityRecipeListBinding
    private var position: Int = 0
    lateinit var presenter: RecipeListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = RecipeListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadRecipes()

        // Spinner setup
        val filterAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.filter_array,
            android.R.layout.simple_spinner_item
        )
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filterType.adapter = filterAdapter

        // Search button
        binding.btnSearch.setOnClickListener {
            val searchTerm = binding.searchbar.text.toString()
            val filterType = binding.filterType.selectedItem.toString()
            val filteredRecipes = presenter.getFilteredRecipes(searchTerm, filterType)

            binding.recyclerView.adapter = RecipeAdapter(filteredRecipes, this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddRecipe() }
            R.id.item_map -> { presenter.doShowRecipesMap() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRecipeClick(recipe: RecipeModel, position: Int) {
        this.position = position
        presenter.doEditRecipe(recipe, this.position)
    }

    private fun loadRecipes() {
        binding.recyclerView.adapter = RecipeAdapter(presenter.getRecipes(), this)
        onRefresh()
    }

    fun onRefresh() {
        binding.recyclerView.adapter?.
        notifyItemRangeChanged(0,presenter.getRecipes().size)
    }

    fun onDelete(position : Int) {
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }

}

