package com.example.test_api1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_api1.databinding.ActivityHomeBinding
import com.example.test_api1.databinding.ActivityMainBinding
import com.example.test_api1.product.Product
import com.example.test_api1.product.ProductAdapter
import com.example.test_api1.product.ProductItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showAllData()
    }

    private fun showAllData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                var url = URL("http://10.0.2.2:8000/api/products")
                var connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if(responseCode == 200){
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null){
                        response.append(line)
                    }

                    reader.close()
                    val jsonresponse = JSONArray(response.toString())

                    val productList = mutableListOf<ProductItem>()
                    for (i in 0 until jsonresponse.length()){
                        val productObject = jsonresponse.getJSONObject(i)
                        val productItem =ProductItem(
                            id = productObject.getInt("id"),
                            name = productObject.getString("name"),
                            image = productObject.getString("image"),
                            price = productObject.getInt("price"),
                            stock = productObject.getInt("stock"),
                            category_id = productObject.getInt("category_id"),
                            created_at = productObject.getString("created_at"),
                            updated_at = productObject.getString("updated_at"),
                        )
                        productList.add(productItem)
                    }
                    val product = Product(productList)
                    withContext(Dispatchers.Main){
                        setUpRecyclerView(product)
                    }
                }
            } catch (e: Exception) {
                println("error = $e")
            }
        }
    }

    private fun setUpRecyclerView(product: Product) {
        if(product.isNotEmpty()){
            val recyclerView = binding.rvhome
            val layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
            recyclerView.layoutManager = layoutManager
            val adapter = ProductAdapter(product)
            recyclerView.adapter = adapter
        }
    }
}