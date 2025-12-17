package ie.setu.mobileappdevassignment.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngredientModel(var name: String = "",
                           var amount: Int = 0,
                           var unit: String = "") : Parcelable