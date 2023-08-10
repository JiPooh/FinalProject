package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;

public class MainActivity extends AppCompatActivity {
private ActivityBearBinding bear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Welcome :)", Toast.LENGTH_SHORT).show();
        showAlertDialog();

        findViewById(R.id.buttFlight).setOnClickListener(clk->{
            Intent flightIntent = new Intent(MainActivity.this, flightTracker.class);
            startActivity(flightIntent);
            showSnackbar("Flight");
        });
        findViewById(R.id.buttTriv).setOnClickListener(clk->{
            Intent triviaIntent = new Intent(MainActivity.this , TriviaActivity.class);
            startActivity(triviaIntent) ;
            showSnackbar("Trivia");
        });
        findViewById(R.id.buttCurr).setOnClickListener(clk -> {
            Intent currencyIntent = new Intent(MainActivity.this, CurrencyConverter.class);
            startActivity(currencyIntent);
            showSnackbar("Currency");

        });
        findViewById(R.id.buttBear).setOnClickListener(clk -> {
            Intent bearIntent = new Intent(MainActivity.this, Bear.class);
            startActivity(bearIntent);
            showSnackbar("Bear");
        });
    }
        private void showSnackbar(String buttonName) {
            String message = "You have clicked " + buttonName + ". Proceeding to the next page.";
            View rootView = findViewById(android.R.id.content);
            Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
        }
        private void showAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Hello")
                    .setMessage("Please select an option")
                    .setPositiveButton("OK", null)
                    .show();
        }
