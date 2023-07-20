package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class flightTracker extends AppCompatActivity {
    private Button toastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_tracker);
        toastButton = findViewById(R.id.airport_option_button);
        toastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast();
                showAlertDialog();
                showSnackbar(v);

//        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//        prefs.getString("VariableName", String defaultValue);
//        String airportCode = prefs.getString("Airport Code", "");
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(String airportCode, String value);
//        editor.apply();
            }
        });
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
