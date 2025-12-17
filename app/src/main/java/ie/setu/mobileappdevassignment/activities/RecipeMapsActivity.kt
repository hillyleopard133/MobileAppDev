package ie.setu.mobileappdevassignment.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import ie.setu.mobileappdevassignment.databinding.ActivityRecipeMapsBinding
import ie.setu.mobileappdevassignment.databinding.ContentRecipeMapsBinding

class RecipeMapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeMapsBinding
    private lateinit var contentBinding: ContentRecipeMapsBinding
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecipeMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        contentBinding = ContentRecipeMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
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
