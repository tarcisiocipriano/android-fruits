package cesar.school.android_fruits.adapter

import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cesar.school.android_fruits.R
import cesar.school.android_fruits.databinding.ItemFruitBinding
import cesar.school.android_fruits.model.Fruit

class FruitAdapter(private val context: Context,
                   private val fruits: List<Fruit>,
                   private val callback: (Fruit, Int) -> Unit): RecyclerView.Adapter<FruitAdapter.VH>() {

    private val fruitPhotos: TypedArray by lazy {
        context.resources.obtainTypedArray(R.array.fruitPhotos)
    }

    class VH(itemView: ItemFruitBinding): RecyclerView.ViewHolder(itemView.root) {
        val fruitName: TextView = itemView.itemFruitName
        val fruitBenefits: TextView = itemView.itemFruitDescription
        val fruitPhoto: ImageView = itemView.itemFruitPhoto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemFruitBinding.inflate(LayoutInflater.from(parent.context))
        val vh = VH(binding)
        vh.itemView.setOnClickListener {
            val fruit = fruits[vh.adapterPosition]
            callback(fruit, vh.adapterPosition)
        }
        return vh
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val (name, benefits, photo) = fruits[position]
        holder.fruitName.text = name
        holder.fruitBenefits.text = benefits
        holder.fruitPhoto.setImageDrawable(fruitPhotos.getDrawable(photo))
    }

    override fun getItemCount(): Int = fruits.size
}