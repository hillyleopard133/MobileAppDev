package ie.setu.mobileappdevassignment.models

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class RecipeMemStore : RecipeStore{
    val recipes = ArrayList<RecipeModel>()

    override fun findAll(): ArrayList<RecipeModel> {
        return recipes
    }

    override fun create(recipe: RecipeModel) {
        recipe.id = getId()
        recipes.add(recipe)
    }

    override fun update(recipe: RecipeModel) {
        var foundRecipe: RecipeModel? = recipes.find { p -> p.id == recipe.id }
        if (foundRecipe != null) {
            foundRecipe.title = recipe.title
            foundRecipe.description = recipe.description
            foundRecipe.vegetarian = recipe.vegetarian
            foundRecipe.vegan = recipe.vegan
            foundRecipe.glutenFree = recipe.glutenFree
            foundRecipe.ingredients = recipe.ingredients
            foundRecipe.image = recipe.image
            foundRecipe.lat = recipe.lat
            foundRecipe.lng = recipe.lng
            foundRecipe.zoom = recipe.zoom
        }
    }
}