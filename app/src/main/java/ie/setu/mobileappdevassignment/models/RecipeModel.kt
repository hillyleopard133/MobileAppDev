package ie.setu.mobileappdevassignment.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeModel(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var image: Uri = Uri.EMPTY,
    var ingredients: ArrayList<IngredientModel> = ArrayList<IngredientModel>(),
    var vegetarian: Boolean = false,
    var vegan: Boolean = false,
    var glutenFree: Boolean = false): Parcelable