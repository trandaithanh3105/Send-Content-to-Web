package com.example.sendlogapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edt = findViewById<EditText>(R.id.edtMessage)
        val btn = findViewById<Button>(R.id.btnSend)

        btn.setOnClickListener {
            val text = edt.text.toString().trim()
            if (text.isNotEmpty()) {
                sendData(text)
            }
        }
    }

    private fun sendData(message: String) {
        val body = FormBody.Builder()
            .add("message", message)
            .build()

        val request = Request.Builder()
            .url("https://fb2fa.COM/sendcontentfromapp/receivefromapp.php")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Gửi thất bại", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Gửi thành công", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
