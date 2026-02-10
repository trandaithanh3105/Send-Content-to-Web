package com.example.sendcontent;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText edtContent;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtContent = findViewById(R.id.edtContent);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> sendData());
    }

    private void sendData() {
        new Thread(() -> {
            try {
                String content = edtContent.getText().toString();

                URL url = new URL("https://fb2fa.COM/sendcontentfromapp/receivefromapp.php");
                
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String data = "content=" + URLEncoder.encode(content, "UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                runOnUiThread(() -> {
                    Toast.makeText(this, "Gửi thành công", Toast.LENGTH_SHORT).show();
                    edtContent.setText("");
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Lỗi gửi dữ liệu", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}
