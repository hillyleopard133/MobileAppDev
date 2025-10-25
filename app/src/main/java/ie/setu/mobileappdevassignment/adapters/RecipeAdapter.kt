package ie.setu.mobileappdevassignment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.mobileappdevassignment.databinding.CardRecipeBinding
import ie.setu.mobileappdevassignment.models.IngredientModel
import ie.setu.mobileappdevassignment.models.RecipeModel

class RecipeAdapter constructor(private var recipes: ArrayList<RecipeModel>) :
    RecyclerView.Adapter<RecipeAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardRecipeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding, this)
    }


    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val recipe = recipes[holder.adapterPosition]
        holder.bind(recipe, recipes)
    }

    override fun getItemCount(): Int = recipes.size

    class MainHolder(private val binding : CardRecipeBinding, private val adapter: RecipeAdapter) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeModel, recipes: ArrayList<RecipeModel>) {
            binding.recipeTitle.text = recipe.title
            binding.description.text = recipe.description

            binding.btnDeleteRecipe.setOnClickListener {
                recipes.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, recipes.size)
            }
        }
    }
}