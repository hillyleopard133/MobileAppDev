package ie.setu.mobileappdevassignment.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.mobileappdevassignment.databinding.CardRecipeBinding
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import ie.setu.mobileappdevassignment.main.MainApp
import ie.setu.mobileappdevassignment.models.RecipeModel

interface RecipeListener {
    fun onRecipeClick(recipe: RecipeModel)
}

class RecipeAdapter constructor(private var recipes: ArrayList<RecipeModel>, private val listener: RecipeListener) :
    RecyclerView.Adapter<RecipeAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardRecipeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding, this)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val recipe = recipes[holder.adapterPosition]
        holder.bind(recipe, recipes, listener)
    }

    override fun getItemCount(): Int = recipes.size

    class MainHolder(private val binding : CardRecipeBinding, private val adapter: RecipeAdapter) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeModel, recipes: ArrayList<RecipeModel>, listener: RecipeListener) {
            binding.recipeTitle.text = recipe.title
            binding.description.text = recipe.description
            Picasso.get().load(recipe.image).fit().centerCrop().into(binding.imageIcon)

            binding.root.setOnClickListener { listener.onRecipeClick(recipe) }

            binding.btnDeleteRecipe.setOnClickListener {
                recipes.removeAt(position)
                val app = binding.root.context.applicationContext as MainApp
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, recipes.size)
            }
        }
    }
}