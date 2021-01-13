package cesar.school.android_fruits

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cesar.school.android_fruits.databinding.ActivityFruitCreationBinding
import cesar.school.android_fruits.model.Fruit

class FruitCreationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFruitCreationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toast.makeText(this, "works", Toast.LENGTH_SHORT)

        var newFruit: Fruit

        binding.buttonAddFruitConfirm.setOnClickListener {
            val name = binding.inputFruitName.text.toString()
            val benefits = binding.textareaFruitBenefits.text.toString()

            if (!name.isNullOrEmpty() && !benefits.isNullOrEmpty()) {
                newFruit = Fruit(name, benefits, (0..3).random())
                val returnIntent = Intent()
                returnIntent.putExtra(MainActivity.MAIN_ACTIVITY_FRUIT_ADDED_ID, newFruit)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
    }
}