package ie.setu.mobileappdevassignment.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.setu.mobileappdevassignment.databinding.ActivityAssignmentBinding
import ie.setu.mobileappdevassignment.models.PlacemarkModel
import timber.log.Timber
import timber.log.Timber.i

class AssignmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssignmentBinding

    val placemarks = ArrayList<PlacemarkModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())

        i("Placemark Activity started...")

        binding.btnAdd.setOnClickListener() {
            var placemark = PlacemarkModel()
            placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.placemarkDescription.text.toString()
            if (placemark.title.isNotEmpty() && placemark.description.isNotEmpty()) {
                placemarks.add(placemark)
                i("add Button Pressed: " + placemark.title + ", " + placemark.description)
                i("Existing list")
                for (placemark in placemarks){
                    i("Entry: " + placemark.title + ", " + placemark.description)
                }
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title and description", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}