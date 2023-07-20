package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class flightTracker extends AppCompatActivity {
    private Button toastButton;
    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_USER_NAME = "UserName";

    private EditText editTextUserName;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_tracker);

        editTextUserName = findViewById(R.id.airport_option);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Load the user's name from Shared Preferences (if available)
        String savedUserName = sharedPreferences.getString(KEY_USER_NAME, "");
        editTextUserName.setText(savedUserName);

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
        String userName = editTextUserName.getText().toString();
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
