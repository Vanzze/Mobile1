package com.example.test_api1.product

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test_api1.Detail_Activity
import com.example.test_api1.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class ProductAdapter(private val product: Product): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imagepro1)
        val name: TextView = itemView.findViewById(R.id.textname)
        val price: TextView = itemView.findViewById(R.id.textprice)
        val stock: TextView = itemView.findViewById(R.id.textstock)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate((R.layout.product_item), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return product.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = product[position]

        holder.name.text = product.name
        holder.price.text = product.price.toString()
        holder.stock.text = product.stock.toString()

        holder.itemView.setOnClickListener(){
            val intent = Intent(holder.itemView.context, Detail_Activity::class.java);
            intent.putExtra("PRODUCT_ID", product.id)
            holder.itemView.context.startActivity(intent)
        }

        GlobalScope.launch(Dispatchers.IO) {
            val bitmap = downloadbitmap("http://10.0.2.2:8000/images/${product.image}")
            launch(Dispatchers.Main) {
                if(bitmap!= null) {
                    holder.imageView.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun downloadbitmap(imageURL: String): Bitmap? {
        var bitmap: Bitmap? = null
        var connection: HttpURLConnection? = null
        val url = URL(imageURL)
        connection = url.openConnection() as HttpURLConnection
        connection.connect()
        val inputStream = connection.inputStream
        bitmap = BitmapFactory.decodeStream(inputStream)
        return bitmap
    }
}