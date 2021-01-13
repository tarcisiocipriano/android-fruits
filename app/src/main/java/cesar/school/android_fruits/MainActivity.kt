package cesar.school.android_fruits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import cesar.school.android_fruits.adapter.FruitAdapter
import cesar.school.android_fruits.databinding.ActivityMainBinding
import cesar.school.android_fruits.model.Fruit

class MainActivity : AppCompatActivity() {

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
    }

    private fun onFruitClickListener(fruit: Fruit) {
        Toast.makeText(this, "Fruit: ${fruit.name}", Toast.LENGTH_SHORT).show()
    }
}