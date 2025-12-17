package ie.setu.mobileappdevassignment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.mobileappdevassignment.databinding.CardIngredientBinding
import ie.setu.mobileappdevassignment.main.MainApp
import ie.setu.mobileappdevassignment.models.IngredientModel

class IngredientAdapter constructor(private var ingredients: ArrayList<IngredientModel>) :
    RecyclerView.Adapter<IngredientAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardIngredientBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding, this)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.bind(ingredient, ingredients)
    }

    override fun getItemCount(): Int = ingredients.size

    class MainHolder(private val binding : CardIngredientBinding, private val adapter: IngredientAdapter) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: IngredientModel, ingredients: ArrayList<IngredientModel>) {
            binding.ingredientName.text = ingredient.name
            binding.ingredientAmount.text = ingredient.amount.toString()
            if(ingredient.unit == "none"){
                binding.ingredientUnit.text = ""
            }else{
                binding.ingredientUnit.text = ingredient.unit
            }

            binding.btnDeleteIngredient.setOnClickListener {
                ingredients.removeAt(position)
                val app = binding.root.context.applicationContext as MainApp
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, ingredients.size)
            }
        }
    }
}