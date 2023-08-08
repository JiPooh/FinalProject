package algonquin.cst2335.finalproject;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import algonquin.cst2335.finalproject.databinding.ActivityTriviaBinding;

public class TriviaActivity extends AppCompatActivity {



    SharedPreferences  prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);




    ActivityTriviaBinding binding;


    String selectedCategory;
    RequestQueue queue = null;
String defaultValue = "" ;

    String[] categoryIDs ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        binding = ActivityTriviaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences  prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);


        RecyclerView triviaList = binding.triviaList;
        categoryIDs = getResources().getStringArray(R.array.category_ids);
        Toast.makeText(this, "Welcome to the trivia game", Toast.LENGTH_LONG).show();

        setupSpinner();
        binding.randomButton.setOnClickListener(click -> {
            String questionNumber = binding.questionText.getText().toString();
            int qNumber = Integer.parseInt(questionNumber);

            int selectedPosition = binding.categorySpinner.getSelectedItemPosition(); // get the position

            selectedCategory = categoryIDs[selectedPosition];

            String url = buildTriviaURL(qNumber, selectedCategory);


            fetchTriviaQuestions(url);
        });

        triviaList.setLayoutManager(new LinearLayoutManager(this));
    }

    private String buildTriviaURL(int numQuestions, String selectedCategory) {
        return "https://opentdb.com/api.php?amount=" + numQuestions + "&category=" + selectedCategory + "&type=multiple";
    }

    private void fetchTriviaQuestions(String url) {
        StringRequest request = new StringRequest(url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray questionArray = jsonObject.getJSONArray("results");
                        TriviaRecyclerViewAdapter adapter = new TriviaRecyclerViewAdapter(questionArray);
                        binding.triviaList.setAdapter(adapter);  // Set the adapter to the RecyclerView
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Error fetching questions", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(adapter);
    }



    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog")
                .setMessage("This is an AlertDialog!")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showSnackbar(View view) {
        Snackbar.make(view, "This is a Snackbar!", Snackbar.LENGTH_SHORT).show();
    }
}