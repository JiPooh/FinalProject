package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import algonquin.cst2335.finalproject.databinding.ActivityFlightTrackerBinding;
import algonquin.cst2335.finalproject.viewModel.BearViewModel;

public class flightTracker extends AppCompatActivity {
    private Button toastButton;
    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_USER_NAME = "UserName";
//    private FlightViewModel FlightViewModel;
    private ActivityFlightTrackerBinding binding;
//    private FlightAdapter flightAdapter;
//    private FlightService flightService;
    RequestQueue queue = null;
    private SharedPreferences sharedPreferences;
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Perform the necessary schema changes and data migration here
            database.execSQL("ALTER TABLE FlightData ADD COLUMN Destination TEXT");
            database.execSQL("ALTER TABLE FlightData ADD COLUMN Terminal TEXT");
            database.execSQL("ALTER TABLE FlightData ADD COLUMN Gate TEXT");
            database.execSQL("ALTER TABLE FlightData ADD COLUMN Delay TEXT");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlightTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.airportOption.findViewById(R.id.airport_option);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Load the user's name from Shared Preferences (if available)
        String savedUserName = sharedPreferences.getString(KEY_USER_NAME, "");
       binding.airportOption.setText(savedUserName);
        binding.airportOptionButton.setOnClickListener(clk ->{
        String url = "http://api.aviationstack.com/v1/flights?access_key=a739c1a3655ac307307decd9e8892131?dep_iata=YOW";
        JsonObjectRequest request = JsonObjectRequest(Request.Method.GET, url, null,
                (response) -> {
                    try {
                        JSONObject flightsObject = response.getJSONObject();

                    }
                }, (error ->{

                }));
        queue.add(request);
        });
        toastButton = findViewById(R.id.airport_option_button);
        toastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast();
                showAlertDialog();
                showSnackbar(v);
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Save the user's name to Shared Preferences when the activity is paused
        String userName = binding.airportOption.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }
    private void showToast() {
        Toast.makeText(this, "This is a Toast message!", Toast.LENGTH_SHORT).show();
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
