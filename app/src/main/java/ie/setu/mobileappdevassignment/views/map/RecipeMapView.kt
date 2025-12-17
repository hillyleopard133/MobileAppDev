package ie.setu.mobileappdevassignment.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import ie.setu.mobileappdevassignment.databinding.ActivityRecipeMapsBinding
import ie.setu.mobileappdevassignment.databinding.ContentRecipeMapsBinding
import ie.setu.mobileappdevassignment.main.MainApp
import ie.setu.mobileappdevassignment.models.RecipeModel

class RecipeMapView : AppCompatActivity() , GoogleMap.OnMarkerClickListener{

    private lateinit var binding: ActivityRecipeMapsBinding
    private lateinit var contentBinding: ContentRecipeMapsBinding
    lateinit var app: MainApp
    lateinit var presenter: RecipeMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityRecipeMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = RecipeMapPresenter(this)

        contentBinding = ContentRecipeMapsBinding.bind(binding.root)

        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync{
            presenter.doPopulateMap(it)
        }
    }
    fun showRecipe(recipe: RecipeModel) {
        contentBinding.currentTitle.text = recipe.title
        contentBinding.currentDescription.text = recipe.description
        Picasso.get()
            .load(recipe.image)
            .into(contentBinding.currentImage)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }

}