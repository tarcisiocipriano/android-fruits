package cesar.school.android_fruits

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import cesar.school.android_fruits.adapter.FruitAdapter
import cesar.school.android_fruits.databinding.ActivityMainBinding
import cesar.school.android_fruits.model.Fruit


class MainActivity : AppCompatActivity() {

    companion object {
        const val MAIN_ACTIVITY_FRUIT_REQUEST_CODE = 1
        const val MAIN_ACTIVITY_RESULT_ID = "newFruit"
    }

    private lateinit var binding : ActivityMainBinding

    private val listFruits = mutableListOf(
        Fruit("Apple", "Lorem ipsum", 0),
        Fruit("Grape", "Lorem ipsum", 1),
        Fruit("Orange", "Lorem ipsum", 2),
        Fruit("Strawberry", "Lorem ipsum", 3)
    )

    private val fruitAdapter = FruitAdapter(this, listFruits, this::onFruitClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fruitList.adapter = fruitAdapter
        binding.fruitList.layoutManager = GridLayoutManager(this, 1)

        // setup insert button
        binding.buttonAddFruit.setOnClickListener {
            val resultActivity = Intent(this, FruitCreationActivity::class.java)
            startActivityForResult(resultActivity, MAIN_ACTIVITY_FRUIT_REQUEST_CODE)
        }
    }

    private fun onFruitClickListener(fruit: Fruit) {
        Toast.makeText(this, "Fruit: ${fruit.name}", Toast.LENGTH_SHORT).show()
    }

    // activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (MAIN_ACTIVITY_FRUIT_REQUEST_CODE == requestCode) {
                val (name, benefits, photo) = data?.getParcelableExtra<Fruit>(MAIN_ACTIVITY_RESULT_ID) as Fruit
                listFruits.add(Fruit(name, benefits, photo))
                fruitAdapter.notifyItemInserted(listFruits.lastIndex)
            }
        }
    }
}