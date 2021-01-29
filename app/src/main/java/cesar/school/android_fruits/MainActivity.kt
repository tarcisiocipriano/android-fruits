package cesar.school.android_fruits

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
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

        const val REMOVE_DUPLICATED = "REMOVE_DUPLICATED"
        const val REMOVE_DUPLICATED_ORDERED_ALPHABETICALLY = "REMOVE_DUPLICATED_ORDERED_ALPHABETICALLY"

        const val DUPLICATED_STATE = "DUPLICATED_STATE"
        const val ORDERED_ALPHABETICALLY_STATE = "ORDERED_ALPHABETICALLY_STATE"
    }

    private lateinit var binding : ActivityMainBinding

    private var listFruits = ArrayList<Fruit>()

    private var listNotDuplicated = false;
    private var listOrderedAlphabetically = false;

    init {
        listFruits = arrayListOf<Fruit>().apply { addAll(initialFruits) }
    }

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
        savedInstanceState?.getBoolean(DUPLICATED_STATE)?.let { listNotDuplicated = it }
        savedInstanceState?.getBoolean(ORDERED_ALPHABETICALLY_STATE)?.let { listOrderedAlphabetically = it }
        fruitListFilter(listNotDuplicated, listOrderedAlphabetically)
        fruitAdapter.notifyDataSetChanged()
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
        newFruit.let { initialFruits.add(it); listFruits.add(it); FruitAdapter.fruits.add(it) }
        fruitListFilter(listNotDuplicated, listOrderedAlphabetically)
        fruitAdapter.notifyItemInserted(listFruits.lastIndex)
    }

    private fun removeFruit(fruitIndex: Int) {
        fruitIndex.let { listFruits.removeAt(it); FruitAdapter.fruits.removeAt(it); initialFruits.removeAt(it) }
        fruitListFilter(listNotDuplicated, listOrderedAlphabetically)
        fruitAdapter.notifyDataSetChanged()
    }

    private fun fruitListFilter(duplicated: Boolean, alphabeticallyOrdered: Boolean) {
        when {
            duplicated && alphabeticallyOrdered -> {
                fruitAdapter.filter.filter(REMOVE_DUPLICATED_ORDERED_ALPHABETICALLY)
                listNotDuplicated = true
                listOrderedAlphabetically = true
            }
            duplicated -> {
                fruitAdapter.filter.filter(REMOVE_DUPLICATED)
                listNotDuplicated = true
                listOrderedAlphabetically = false
            }
            alphabeticallyOrdered -> {
                fruitAdapter.filter.filter(ORDERED_ALPHABETICALLY_STATE)
                listNotDuplicated = false
                listOrderedAlphabetically = true
            }
            else -> {
                fruitAdapter.filter.filter("")
                listNotDuplicated = false
                listOrderedAlphabetically = false
            }
        }
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

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_filter -> {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_filter, null)
            val switchDuplicated = view.findViewById<SwitchCompat>(R.id.switchDuplicated)
            val switchOrderAlphabetically = view.findViewById<SwitchCompat>(R.id.switchOrderAlphabetically)

            if (fruitAdapter.isFiltered()) switchDuplicated.isChecked = true
            if (listOrderedAlphabetically) switchOrderAlphabetically.isChecked = true

            builder.apply {
                setView(view)
                setPositiveButton("Filter") { dialog, _ ->
                    fruitListFilter(switchDuplicated.isChecked, switchOrderAlphabetically.isChecked)
                    dialog.dismiss()
                }
            }
            builder.create().show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(DUPLICATED_STATE, listNotDuplicated)
        outState.putBoolean(ORDERED_ALPHABETICALLY_STATE, listOrderedAlphabetically)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}