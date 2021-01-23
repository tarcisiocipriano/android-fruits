package cesar.school.android_fruits

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cesar.school.android_fruits.adapter.FruitAdapter
import cesar.school.android_fruits.databinding.ActivityMainBinding
import cesar.school.android_fruits.model.Fruit
import cesar.school.android_fruits.MockData.initialFruits


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

        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.title = "Fruit list"

        listFruitsInit(savedInstanceState)
        recyclerviewSetup()

        binding.buttonAddFruit.setOnClickListener {
            val resultActivity = Intent(this, FruitCreationActivity::class.java)
            startActivityForResult(resultActivity, MAIN_ACTIVITY_ADD_REQUEST_CODE)
        }
    }

    private fun listFruitsInit(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            listFruits.addAll(initialFruits)
        } else {
            listFruits.addAll(savedInstanceState.getParcelableArrayList(SAVED_FRUIT_LIST) ?: ArrayList())
            fruitAdapter.notifyDataSetChanged()
        }
    }

    private fun recyclerviewSetup() {
        binding.fruitList.adapter = fruitAdapter
        binding.fruitList.layoutManager = LinearLayoutManager(this)
    }

    // fruitAdapter on click implementation
    private fun onFruitClickListener(fruit: Fruit, index: Int) {
        val resultActivity = Intent(this, FruitDetailsActivity::class.java)
        resultActivity.putExtra(MAIN_ACTIVITY_FRUIT_ID, fruit)
        resultActivity.putExtra(MAIN_ACTIVITY_FRUIT_INDEX, index)
        startActivityForResult(resultActivity, MAIN_ACTIVITY_REMOVE_REQUEST_CODE)
    }

    private fun addFruit(newFruit: Fruit) {
        listFruits.add(newFruit)
        fruitAdapter.notifyItemInserted(listFruits.lastIndex)
    }

    private fun removeFruit(fruitIndex: Int) {
        listFruits.removeAt(fruitIndex)
        fruitAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && MAIN_ACTIVITY_ADD_REQUEST_CODE == requestCode) {
            val newFruit = data?.getParcelableExtra<Fruit>(MAIN_ACTIVITY_FRUIT_ADDED_ID)
            newFruit?.let { addFruit(it) }
        }
        if (resultCode == Activity.RESULT_OK && MAIN_ACTIVITY_REMOVE_REQUEST_CODE == requestCode) {
            val fruitIndex = data?.getIntExtra(MAIN_ACTIVITY_FRUIT_ID, -1)
            fruitIndex?.let { removeFruit(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_FRUIT_LIST, listFruits)
    }
}