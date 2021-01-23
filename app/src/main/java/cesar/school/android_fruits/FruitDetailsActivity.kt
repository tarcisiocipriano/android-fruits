package cesar.school.android_fruits

import android.app.Activity
import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cesar.school.android_fruits.databinding.ActivityFruitDetailsBinding
import cesar.school.android_fruits.model.Fruit

class FruitDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFruitDetailsBinding

    private val fruitPhotos: TypedArray by lazy {
        resources.obtainTypedArray(R.array.fruitPhotos)
    }

    private var listNewPhotos = MainActivity.listNewPhotos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fruit = intent.getParcelableExtra<Fruit>(MainActivity.MAIN_ACTIVITY_FRUIT_ID)
        val index = intent.getIntExtra(MainActivity.MAIN_ACTIVITY_FRUIT_INDEX, -1)

        setSupportActionBar(binding.toolbarDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = fruit?.name

        if (fruit != null) {
            binding.detailsFruitBenefits.text = fruit.benefits
            if (fruit.photo != null) {
                binding.detailsFruitPhoto.setImageDrawable(fruitPhotos.getDrawable(fruit.photo))
            } else {
                binding.detailsFruitPhoto.setImageBitmap(listNewPhotos[fruit.photoAdded!!])
            }
        }

        // remove fruit
        binding.buttonRemoveFruit.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(MainActivity.MAIN_ACTIVITY_FRUIT_ID, index)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}