package cesar.school.android_fruits.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fruit (val name: String, val benefits: String, val photo: Int?, val photoAdded: Int?): Parcelable
