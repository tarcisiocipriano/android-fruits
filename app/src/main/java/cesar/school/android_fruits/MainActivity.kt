package cesar.school.android_fruits

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import cesar.school.android_fruits.adapter.FruitAdapter
import cesar.school.android_fruits.databinding.ActivityMainBinding
import cesar.school.android_fruits.model.Fruit


class MainActivity : AppCompatActivity() {

    companion object {
        const val MAIN_ACTIVITY_ADD_REQUEST_CODE = 1
        const val MAIN_ACTIVITY_FRUIT_ADDED_ID = "newFruit"

        const val MAIN_ACTIVITY_REMOVE_REQUEST_CODE = 2
        const val MAIN_ACTIVITY_FRUIT_ID = "fruit"
        const val MAIN_ACTIVITY_FRUIT_INDEX = "fruitIndex"
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
            startActivityForResult(resultActivity, MAIN_ACTIVITY_ADD_REQUEST_CODE)
        }
    }

    // open details activity
    private fun onFruitClickListener(fruit: Fruit, index: Int) {
        val resultActivity = Intent(this, FruitDetailsActivity::class.java)
        resultActivity.putExtra(MAIN_ACTIVITY_FRUIT_ID, fruit)
        resultActivity.putExtra(MAIN_ACTIVITY_FRUIT_INDEX, index)
        startActivityForResult(resultActivity, MAIN_ACTIVITY_REMOVE_REQUEST_CODE)
    }

    // activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (MAIN_ACTIVITY_ADD_REQUEST_CODE == requestCode) {
                val (name, benefits, photo) = data?.getParcelableExtra<Fruit>(MAIN_ACTIVITY_FRUIT_ADDED_ID) as Fruit
                listFruits.add(Fruit(name, benefits, photo))
                fruitAdapter.notifyItemInserted(listFruits.lastIndex)
            }
            if (MAIN_ACTIVITY_REMOVE_REQUEST_CODE == requestCode) {
                val fruitIndex = data?.getIntExtra(MAIN_ACTIVITY_FRUIT_ID, -1)
                if (fruitIndex != null) {
                    listFruits.removeAt(fruitIndex)
                    fruitAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}