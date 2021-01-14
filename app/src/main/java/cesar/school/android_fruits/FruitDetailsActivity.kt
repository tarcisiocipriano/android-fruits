package cesar.school.android_fruits

import android.app.Activity
import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cesar.school.android_fruits.databinding.ActivityFruitDetailsBinding
import cesar.school.android_fruits.model.Fruit

class FruitDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFruitDetailsBinding

    private val fruitPhotos: TypedArray by lazy {
        resources.obtainTypedArray(R.array.fruitPhotos)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val (name, benefits, photo) = intent.getParcelableExtra<Fruit>(MainActivity.MAIN_ACTIVITY_FRUIT_ID) as Fruit
        val index = intent.getIntExtra(MainActivity.MAIN_ACTIVITY_FRUIT_INDEX, -1)

        binding.detailsFruitName.text = name
        binding.detailsFruitBenefits.text = benefits
        binding.detailsFruitPhoto.setImageDrawable(fruitPhotos.getDrawable(photo))

        // remove fruit
        binding.buttonRemoveFruit.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(MainActivity.MAIN_ACTIVITY_FRUIT_ID, index)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}