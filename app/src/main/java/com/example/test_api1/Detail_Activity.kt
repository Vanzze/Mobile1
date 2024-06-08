package com.example.test_api1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.test_api1.databinding.ActivityDetailBinding
import com.example.test_api1.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Detail_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getIntExtra("PRODUCT_ID", 0)
        showProductById(productId)

    }

    private fun showProductById(productId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                var url = URL("http://10.0.2.2:8000/api/product/$productId")
                var connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if (responseCode == 200) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }

                    reader.close()

                    var jsonResponse = JSONObject(response.toString())
                    val name = jsonResponse.getString("name")
                    val image = jsonResponse.getString("image")

                    val bitmap = downloadbitmap("http://10.0.2.2:8000/images/${image}")

                    withContext(Dispatchers.Main) {
                        binding.texttes1.text = name
                        binding.imgtest.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                println(e)
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