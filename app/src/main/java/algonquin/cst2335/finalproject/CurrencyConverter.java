package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import algonquin.cst2335.finalproject.databinding.ActivityCurrencyConverterBinding;

public class CurrencyConverter extends AppCompatActivity {
    RequestQueue queue = null;
    private Button submitButton;
    private ActivityCurrencyConverterBinding binding;
    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_BASE_CURRENCY = "BaseCurrency";
    private static final String KEY_END_CURRENCY = "EndCurrency";
    private static final String KEY_AMOUNT = "Amount";
    protected ArrayList<String> conversions =new ArrayList<>();
    private EditText editTextAmount;
    private EditText editTextEndCurrency;
    private EditText editTextBaseCurrency;
    private SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_1) {
                helpDialog();
        }

        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        queue = Volley.newRequestQueue(this);

        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);

        binding.currencyRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            String obj = conversions.get(position);

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });

        editTextAmount = findViewById(R.id.amount_editText);
        editTextEndCurrency = findViewById(R.id.endCurrency_editText);
        editTextBaseCurrency = findViewById(R.id.baseCurrency_editText);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Load the user's name from Shared Preferences (if available)
        String savedBaseCurrency = sharedPreferences.getString(KEY_BASE_CURRENCY, "");
        editTextBaseCurrency.setText(savedBaseCurrency);

        String savedEndCurrency = sharedPreferences.getString(KEY_END_CURRENCY, "");
        editTextEndCurrency.setText(savedEndCurrency);

        String savedAmount = sharedPreferences.getString(KEY_AMOUNT, "");
        editTextAmount.setText(savedAmount);

        submitButton = findViewById(R.id.currency_submitButton);

        submitButton.setOnClickListener(clk -> {
           String base = editTextBaseCurrency.getText().toString();
           String end = editTextEndCurrency.getText().toString();
           String amt = editTextAmount.getText().toString();


            String stringURL = null;
            try {
                stringURL = "https://api.getgeoapi.com/v2/currency/convert?format=json&from="
                        + URLEncoder.encode(base, "UTF-8")
                        + "&to="
                        + URLEncoder.encode(end, "UTF-8")
                        +"&amount="
                        + URLEncoder.encode(amt, "UTF-8")
                        + "&api_key=0372d94f591a92da01254d9f6f4b013fa24b313a&format=json";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {

                        try {
                            String updatedDate = response.getString("updated_date");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        JSONObject ratesObject = null;

                        try {
                            ratesObject = response.getJSONObject("rates");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        String currencyCode = ratesObject.keys().next();

                        JSONObject currencyData = null;
                        try {
                            currencyData = ratesObject.getJSONObject(currencyCode);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            String currencyName = currencyData.getString("currency_name");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            String rate = currencyData.getString("rate");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        String rateForAmount = null;
                        try {
                            rateForAmount = currencyData.getString("rate_for_amount");
                            String finalRateForAmount = rateForAmount;
                            runOnUiThread( (  )  -> {
                                binding.resultTextView.setText("The resulting currency is: $" + finalRateForAmount);
                                binding.resultTextView.setVisibility(View.VISIBLE);
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }



                    }, (error) -> { });
            queue.add(request);


//                Toast.makeText(this, "This is a Toast message!", Toast.LENGTH_SHORT).show();
//                Snackbar.make(binding.getRoot(), "This is a Snackbar!", Snackbar.LENGTH_SHORT).show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("Alert Dialog")
//                    .setMessage("This is an AlertDialog!")
//                    .setPositiveButton("OK", null)
//                    .show();


        });
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Save Shared Preferences when the activity is paused
        String baseCurrency = editTextBaseCurrency.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_BASE_CURRENCY, baseCurrency);
        editor.apply();
    }

    protected void helpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.helpMessage);
        builder.setTitle(R.string.helpTitle);
        builder.setPositiveButton("OK",(dialog, cl)->{ });
        builder.show();
    }


    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView conversionText;
        public MyRowHolder (@NonNull View itemView) {
            super(itemView);

        conversionText = itemView.findViewById(R.id.currencyTextView);
        }
    }
}