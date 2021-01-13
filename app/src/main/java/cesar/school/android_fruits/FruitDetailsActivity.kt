package cesar.school.android_fruits

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cesar.school.android_fruits.databinding.ActivityFruitDetailsBinding
import cesar.school.android_fruits.model.Fruit

class FruitDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFruitDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fruit = intent.getParcelableExtra<Fruit>(MainActivity.MAIN_ACTIVITY_FRUIT_ID)
        val index = intent.getIntExtra(MainActivity.MAIN_ACTIVITY_FRUIT_INDEX, -1)

        if (fruit != null) {
            binding.detailsFruitName.text = fruit.name
            binding.detailsFruitBenefits.text = fruit.benefits
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