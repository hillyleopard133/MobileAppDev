package ie.setu.mobileappdevassignment.models

data class RecipeModel(
    var title: String = "",
    var description: String = "",
    var ingredients: ArrayList<IngredientModel> = ArrayList<IngredientModel>(),
    var vegetarian: Boolean = false,
    var vegan: Boolean = false,
    var glutenFree: Boolean = false)