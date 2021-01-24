package cesar.school.android_fruits

import android.app.Activity
import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cesar.school.android_fruits.databinding.ActivityFruitDetailsBinding
import cesar.school.android_fruits.model.Fruit

class FruitDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFruitDetailsBinding

    private var index: Int? = null

    private val fruitPhotos: TypedArray by lazy {
        resources.obtainTypedArray(R.array.fruitPhotos)
    }

    private var listNewPhotos = MainActivity.listNewPhotos


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fruit = intent.getParcelableExtra<Fruit>(MainActivity.MAIN_ACTIVITY_FRUIT_ID)
        index = intent.getIntExtra(MainActivity.MAIN_ACTIVITY_FRUIT_INDEX, -1)

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
    }

    private fun removeFruit(index: Int) {
        val returnIntent = Intent()
        returnIntent.putExtra(MainActivity.MAIN_ACTIVITY_FRUIT_ID, index)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_remove -> {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.apply {
                setTitle("Wait!")
                setMessage("You're about to remove the fruit, are you sure?")
                setCancelable(false)
                setPositiveButton("Yes") { _, _ ->
                    index?.let { removeFruit(it) }
                    Toast.makeText(this@FruitDetailsActivity, "Fruit removed", Toast.LENGTH_SHORT).show()
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
            }
            val alertDialog = builder.create()
			alertDialog.show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}