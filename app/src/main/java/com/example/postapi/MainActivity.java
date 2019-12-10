package com.example.postapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etEmail, etPassword, etRole;
    Button btnPost;
    TextView tvEmail, tvPassword, tvRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String role = etRole.getText().toString().trim();

                JSONObject jInfo = new JSONObject();

                try {
                    jInfo.put("email", email);
                    jInfo.put("password", password);
                    jInfo.put("role", role);
                    Log.d("TAG", "convert to json: " + jInfo.toString());
                    new PostData(new PostDataListener() {

                        @Override
                        public void onFinish(UserResponse<String> result) {
                            if (result.getE() == null) {
                                Toast.makeText(MainActivity.this, result.getData(), Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(MainActivity.this, result.getE().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).execute("http://loto.vlythaytien.com/api/v1/provider/login", jInfo.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void init() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etRole = findViewById(R.id.etRole);
        btnPost = findViewById(R.id.btnPost);
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);
        tvRole = findViewById(R.id.tvRole);
    }
}
