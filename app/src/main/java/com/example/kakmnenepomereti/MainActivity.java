package com.example.kakmnenepomereti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);

        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        // часть слова
        final Call<GitResult> call =
                gitHubService.getUsers("ZhacFresc");

        call.enqueue(new Callback<GitResult>() {
            @Override
            public void onResponse(Call<GitResult> call, Response<GitResult> response) {
                if (response.isSuccessful()) {
                    GitResult result = response.body();
                    String user = "Аккаунт Github: " + result.getItems().get(0).getLogin();
                    textView.setText(user);
                    Log.i("Git", String.valueOf(result.getItems().size()));

                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    int statusCode = response.code();

                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                    try {
                        textView.setText(errorBody.string());
                        progressBar.setVisibility(View.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GitResult> call, Throwable throwable) {
                textView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }
}