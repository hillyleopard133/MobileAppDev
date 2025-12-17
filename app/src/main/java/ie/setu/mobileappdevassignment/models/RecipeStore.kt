package ie.setu.mobileappdevassignment.models

interface RecipeStore {
    fun findAll(): ArrayList<RecipeModel>
    fun create(recipe: RecipeModel)

    fun update(recipe: RecipeModel)
}