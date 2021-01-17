package cesar.school.android_fruits

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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

        val listNewPhotos = mutableListOf<Bitmap>()

        const val SAVED_FRUIT_LIST = "save_fruit_list"
    }

    private lateinit var binding : ActivityMainBinding

    private var listFruits = ArrayList<Fruit>()

    private val fruitAdapter = FruitAdapter(this, listFruits, this::onFruitClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set the state restored in the view, or just initiate with 4 fruit samples
        if (savedInstanceState != null) {
            listFruits.addAll(savedInstanceState.getParcelableArrayList(SAVED_FRUIT_LIST) ?: ArrayList())
            fruitAdapter.notifyDataSetChanged()
        } else {
            listFruits.addAll(arrayListOf(
                Fruit("Apple", "Apples are among the most popular fruits, and also happen to be incredibly nutritious.\n" +
                        "They contain a high amount of fiber, vitamin C, potassium and vitamin K. They also provide some B vitamins (19).", 0, null),
                Fruit("Grape", "Grapes are very healthy. Their high antioxidant content is what makes them stand out.\n" +
                        "The anthocyanins and resveratrol in grapes have both been shown to reduce inflammation (73Trusted Source, 74Trusted Source).", 1, null),
                Fruit("Orange", "Oranges are one of the most popular and nutritious fruits in the world.\n" +
                        "Eating one medium orange will provide a significant amount of vitamin C and potassium. They’re also a good source of B vitamins, such as thiamine and folate (62).", 2, null),
                Fruit("Strawberry", "Strawberries are highly nutritious.\n" +
                        "Their vitamin C, manganese, folate and potassium contents are where they really shine (34).", 3, null),
            ))
        }

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
                val newFruit = data?.getParcelableExtra<Fruit>(MAIN_ACTIVITY_FRUIT_ADDED_ID)
                if (newFruit != null) {
                    listFruits.add(Fruit(newFruit.name, newFruit.benefits, null, (listNewPhotos.size - 1)))
                    fruitAdapter.notifyItemInserted(listFruits.lastIndex)
                }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_FRUIT_LIST, listFruits)
    }
}