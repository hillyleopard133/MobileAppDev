package ie.setu.mobileappdevassignment.models

data class RecipeModel(var title: String = "", var description: String = "", var ingredients: ArrayList<IngredientModel> = ArrayList<IngredientModel>())