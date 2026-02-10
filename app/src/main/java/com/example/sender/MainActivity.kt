package com.example.sender

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // TODO: đổi URL này thành endpoint web của bạn
    private val endpointUrl = "https://fb2fa.com/sendcontentfromapp/receivefromapp.php"

    private val client = OkHttpClient.Builder()
        .callTimeout(20, TimeUnit.SECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isEmpty()) {
                tvStatus.text = "⚠️ Bạn chưa nhập nội dung"
                return@setOnClickListener
            }

            tvStatus.text = "⏳ Đang gửi..."
            btnSend.isEnabled = false

            // Gửi dạng JSON: { "message": "...", "ts": 123456789 }
            val json = JSONObject().apply {
                put("message", text)
                put("ts", System.currentTimeMillis())
            }

            val body = json.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url(endpointUrl)
                .post(body)
                .build()

            Thread {
                try {
                    client.newCall(request).execute().use { resp ->
                        val ok = resp.isSuccessful
                        val code = resp.code
                        val respText = resp.body?.string()?.take(300) ?: ""

                        runOnUiThread {
                            btnSend.isEnabled = true
                            tvStatus.text = if (ok) {
                                "✅ Gửi thành công (HTTP $code)\n$respText"
                            } else {
                                "❌ Gửi thất bại (HTTP $code)\n$respText"
                            }
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        btnSend.isEnabled = true
                        tvStatus.text = "❌ Lỗi: ${e.message}"
                    }
                }
            }.start()
        }
    }
}
