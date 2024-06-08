package com.example.test_api1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.test_api1.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnlogin.setOnClickListener{
            login()
        }
    }

    private fun login() {
        if(binding.etEmail.text.isEmpty() || binding.etPass.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch(Dispatchers.IO){
            try{
                var url = URL("http://10.0.2.2:8000/api/login")
                var connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.connectTimeout = 5000
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                var outputStream = DataOutputStream(connection.outputStream)
                var jsonObject = JSONObject()
                jsonObject.put("email", binding.etEmail.text)
                jsonObject.put("password", binding.etPass.text)
                var requestBody = jsonObject.toString()

                outputStream.writeBytes(requestBody)
                outputStream.flush()
                outputStream.close()

                var responseCode = connection.responseCode
                if(responseCode == 200) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(applicationContext, "Berhasil login", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext,HomeActivity::class.java)
                        startActivity(intent)
                    }
                }
                else {
                    withContext(Dispatchers.Main){
                        Toast.makeText(applicationContext, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception){
                println("Ini errornya : $e")
            }
        }

    }
}