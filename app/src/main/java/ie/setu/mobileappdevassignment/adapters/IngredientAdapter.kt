package ie.setu.mobileappdevassignment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.mobileappdevassignment.databinding.CardIngredientBinding
import ie.setu.mobileappdevassignment.models.IngredientModel

class IngredientAdapter constructor(private var ingredients: List<IngredientModel>) :
    RecyclerView.Adapter<IngredientAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardIngredientBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val ingredient = ingredients[holder.adapterPosition]
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int = ingredients.size

    class MainHolder(private val binding : CardIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: IngredientModel) {
            binding.ingredientName.text = ingredient.name
            binding.ingredientAmount.text = ingredient.amount.toString()
            if(ingredient.unit == "none"){
                binding.ingredientUnit.text = ""
            }else{
                binding.ingredientUnit.text = ingredient.unit
            }
        }
    }
}