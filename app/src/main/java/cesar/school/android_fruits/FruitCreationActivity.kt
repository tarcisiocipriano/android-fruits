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
import java.io.IOException


class FruitCreationActivity : AppCompatActivity() {

    companion object {
        const val GALLERY_PICTURE = 1
    }

    private lateinit var binding: ActivityFruitCreationBinding
    private lateinit var newFruitPhoto: Bitmap

    private var listNewPhotos = MainActivity.listNewPhotos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toast.makeText(this, "works", Toast.LENGTH_SHORT)

        binding.buttonAddFruitConfirm.setOnClickListener {
            val name = binding.inputFruitName.text.toString()
            val benefits = binding.textareaFruitBenefits.text.toString()

            if (!name.isNullOrEmpty() && !benefits.isNullOrEmpty()) {
                listNewPhotos.add(newFruitPhoto)
                val newFruit = Fruit(name, benefits, null, null)
                val returnIntent = Intent()
                returnIntent.putExtra(MainActivity.MAIN_ACTIVITY_FRUIT_ADDED_ID, newFruit)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }

        // upload image
        binding.buttonUploadFruitPhoto.setOnClickListener {
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
            try {
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data?.data!!)
                val source = ImageDecoder.createSource(this.contentResolver, data?.data!!)
                val bitmap = ImageDecoder.decodeBitmap(source)

                newFruitPhoto = bitmap

                binding.imageFruitPreview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}