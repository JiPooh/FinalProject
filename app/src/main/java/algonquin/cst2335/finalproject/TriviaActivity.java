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
import android.view.Menu;
import android.view.MenuItem;
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



     private SharedPreferences sharedPreferences;




    ActivityTriviaBinding binding;


    String selectedCategory;
    RequestQueue queue = null;


    String[] categoryIDs ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        binding = ActivityTriviaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    sharedPreferences = this.getSharedPreferences("YourPrefsName", MODE_PRIVATE);

        int defaultQuestions = sharedPreferences.getInt("numQuestions", 10);
        binding.questionText.setText(String.valueOf(defaultQuestions)) ;

        setSupportActionBar(binding.myToolbar) ;

        RecyclerView triviaList = binding.triviaList;
        categoryIDs = getResources().getStringArray(R.array.category_ids);
        Toast.makeText(this, "Welcome to the trivia game", Toast.LENGTH_LONG).show();

        setupSpinner();
        binding.randomButton.setOnClickListener(click -> {
            String questionNumber = binding.questionText.getText().toString();
            int qNumber = Integer.parseInt(questionNumber);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("numQuestions", qNumber);
            editor.apply();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true ;
    }

    /**
     * @param item The menu item that was selected.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == R.id.item1) {

            Toast.makeText(this, "Version 1.0 created by Malek " , Toast.LENGTH_LONG).show();

            return true;
        } else {
           showAlertDialog();
            return true;
        }

        //return super.onOptionsItemSelected(item);
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