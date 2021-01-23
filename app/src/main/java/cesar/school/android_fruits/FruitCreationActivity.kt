package cesar.school.android_fruits

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cesar.school.android_fruits.databinding.ActivityFruitCreationBinding
import cesar.school.android_fruits.model.Fruit


class FruitCreationActivity : AppCompatActivity() {

    companion object {
        const val GALLERY_PICTURE = 1
        const val SAVED_FRUIT_PHOTO = "save_fruit_photo"
        var newFruitPhoto: Bitmap? = null
    }

    private lateinit var binding: ActivityFruitCreationBinding

    private var listNewPhotos = MainActivity.listNewPhotos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add fruit"

        if (savedInstanceState != null) {
            val bitmap: Bitmap? = savedInstanceState.getParcelable<Bitmap>(SAVED_FRUIT_PHOTO)
            bitmap?.let {
                newFruitPhoto = it;
                binding.imageFruitPreview.setImageBitmap(bitmap)
            }
        }

        binding.buttonAddFruitConfirm.setOnClickListener {
            val name = binding.inputFruitName.text.toString()
            val benefits = binding.textareaFruitBenefits.text.toString()

            if (name.isNotEmpty() && benefits.isNotEmpty() && newFruitPhoto != null) {
                newFruitPhoto?.let { listNewPhotos.add(it) }
                newFruitPhoto = null
                val newFruit = Fruit(name, benefits, photoAdded = listNewPhotos.lastIndex)
                val returnIntent = Intent()
                returnIntent.putExtra(MainActivity.MAIN_ACTIVITY_FRUIT_ADDED_ID, newFruit)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            } else if (newFruitPhoto == null) {
                Toast.makeText(this, "Select an image", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Fill in the fields", Toast.LENGTH_SHORT).show()
            }
        }

        // upload image
        binding.imageFruitPreview.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, GALLERY_PICTURE)
            } catch (e: ActivityNotFoundException) {
                Log.e("TAG", "No gallery: $e")
            }
        }
    }

    // activity result
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_PICTURE) {
            val source = data?.data?.let { ImageDecoder.createSource(this.contentResolver, it) }
            val bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
            bitmap?.let { newFruitPhoto = it }
            binding.imageFruitPreview.setImageBitmap(bitmap)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_FRUIT_PHOTO, newFruitPhoto)
    }
}