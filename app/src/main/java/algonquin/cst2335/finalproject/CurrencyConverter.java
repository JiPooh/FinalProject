package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.finalproject.databinding.ActivityCurrencyConverterBinding;

public class CurrencyConverter extends AppCompatActivity {
    private Button currencySubmitButton;

    private ActivityCurrencyConverterBinding binding;
    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_BASE_CURRENCY = "BaseCurrency";

    private EditText editTextBaseCurrency;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.currencyRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
        editTextBaseCurrency = findViewById(R.id.currency_editText);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Load the user's name from Shared Preferences (if available)
        String savedBaseCurrency = sharedPreferences.getString(KEY_BASE_CURRENCY, "");
        editTextBaseCurrency.setText(savedBaseCurrency);

        currencySubmitButton = findViewById(R.id.currency_submitButton);
        currencySubmitButton.setOnClickListener(new View.OnClickListener() {
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
        String baseCurrency = editTextBaseCurrency.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_BASE_CURRENCY, baseCurrency);
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

    class MyRowHolder extends RecyclerView.ViewHolder {
        public MyRowHolder (@NonNull View itemView) {
            super(itemView);
        }
    }
}