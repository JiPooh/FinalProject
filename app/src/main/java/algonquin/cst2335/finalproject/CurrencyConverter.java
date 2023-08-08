package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import algonquin.cst2335.finalproject.converterdata.CurrencyConverterViewModel;
import algonquin.cst2335.finalproject.databinding.ActivityCurrencyConverterBinding;
import algonquin.cst2335.finalproject.databinding.CurrencyLayoutBinding;

/**
 * Main currency conversion class, prompts for 3 inputs and on click, retrieves information from an API.
 * @author Connor McHugh
 * @version 1.0
 */
public class CurrencyConverter extends AppCompatActivity {

    RequestQueue queue = null;
    private Button submitButton;
    RecyclerView.Adapter convAdapter;
    private Button currencySubmitButton;

    private ActivityCurrencyConverterBinding binding;
    private static final String PREF_NAME = "MyConversionPreferences";
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Perform the necessary schema changes and data migration here
            database.execSQL("ALTER TABLE SingleConversion ADD COLUMN currencyName TEXT");
            database.execSQL("ALTER TABLE SingleConversion ADD COLUMN rate TEXT");
            database.execSQL("ALTER TABLE SingleConversion ADD COLUMN status TEXT");
        }
    };
    private static final String KEY_BASE_CURRENCY = "BaseCurrency";
    private static final String KEY_END_CURRENCY = "EndCurrency";
    private static final String KEY_AMOUNT = "Amount";
    protected ArrayList<SingleConversion> conversions = new ArrayList<>();

       ConversionDatabase convDB;

       SingleConversionDAO convDAO;
    CurrencyConverterViewModel convModel;
    private EditText editTextAmount;
    private EditText editTextEndCurrency;
    private EditText editTextBaseCurrency;
    private SharedPreferences sharedPreferences;

    /**
     *  method for the toolbar menu items
     * @param menu The options menu in which you place your items.
     *
     * @return true
     */
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    /**
     * Method for item selection on toolbar
     * @param item The menu item that was selected.
     *
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_1) {
                helpDialog();
        }
        return true;
    }
        protected void helpDialog() {
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.helpMessage);
            builder.setTitle(R.string.helpTitle);
            builder.setPositiveButton("OK",(dialog, cl)->{ });
            builder.show();
    }

    /**
     *  when the app is launched, this method is called.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        convDB = Room.databaseBuilder(getApplicationContext(), ConversionDatabase.class, "currency-converter-database")
                .addMigrations(MIGRATION_1_2)
                .build();
        convDAO = convDB.cDAO();
        convModel = new ViewModelProvider(this).get(CurrencyConverterViewModel.class);
        conversions = convModel.conversions.getValue();
        if (conversions == null) {

            convModel.conversions.setValue( conversions = new ArrayList<SingleConversion>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                conversions.addAll( convDAO.getAllConversions() ); //Once you get the data from database

                runOnUiThread( () ->  binding.currencyRecyclerView.setAdapter( convAdapter )); //You can then load the RecyclerView
            });
            convModel.selectedConversion.observe(this, (newConversionValue) -> {
                if (newConversionValue != null){
                    FragmentManager fMgr = getSupportFragmentManager();

                    FragmentTransaction tx = fMgr.beginTransaction();
                    ConversionDetailsFragment frag = new ConversionDetailsFragment( newConversionValue );
                    tx.add(R.id.currency_fragment, frag);
                    tx.addToBackStack("anything here");
                    tx.commit();
                }

            });
        }
        queue = Volley.newRequestQueue(this);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        binding.baseCurrencyEditText.setText(sharedPreferences.getString(KEY_BASE_CURRENCY, ""));
        binding.endCurrencyEditText.setText(sharedPreferences.getString(KEY_END_CURRENCY,""));
        binding.amountEditText.setText(sharedPreferences.getString(KEY_AMOUNT,""));
        submitButton = binding.currencySubmitButton;


        editTextBaseCurrency = binding.baseCurrencyEditText;
        editTextEndCurrency = binding.endCurrencyEditText;
        editTextAmount = binding.amountEditText;
        binding.currencyRecyclerView.setAdapter(convAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            /**
             *  viewholder method for assigning a new row
             * @param parent   The ViewGroup into which the new View will be added after it is bound to
             *                 an adapter position.
             * @param viewType The view type of the new View.
             * @return
             */
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate the view for the ViewHolder with attachToRoot set to false
            CurrencyLayoutBinding binding = CurrencyLayoutBinding.inflate(getLayoutInflater());
            return new MyRowHolder(binding.getRoot());
            }


            /**
             *  Method to display the rows
             * @param holder   The ViewHolder which should be updated to represent the contents of the
             *                 item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                SingleConversion obj = conversions.get(position);
                holder.conversionText.setText("The final amount is: " + obj.getFinalRate() + "" +obj.getEndCurrency());
            }

            /**
             * Method to get the number of items stored.
             * @return
             */
            @Override
            public int getItemCount() {
                return conversions.size();
            }
        });

        submitButton.setOnClickListener(clk -> {

                    String base = editTextBaseCurrency.getText().toString();
                    String end = editTextEndCurrency.getText().toString();
                    String amt = editTextAmount.getText().toString();

                    String stringURL = "https://api.getgeoapi.com/v2/currency/convert?format=json&from="
                            + URLEncoder.encode(base)
                            + "&to="
                            + URLEncoder.encode(end)
                            + "&amount="
                            + URLEncoder.encode(amt)
                            + "&api_key=0372d94f591a92da01254d9f6f4b013fa24b313a&format=json";

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                            (response) -> {
                                try {
                                    JSONObject ratesObject = response.getJSONObject("rates");
                                    String currencyCode = ratesObject.keys().next();
                                    JSONObject currencyData = ratesObject.getJSONObject(currencyCode);
                                    String rateForAmount = currencyData.getString("rate_for_amount");

                                    String currencyName = currencyData.getString("currency_name");
                                    String rate = currencyData.getString("rate");

                                    String status = response.getString("status");

                                    SingleConversion newConversion = new SingleConversion(base, end, amt, rateForAmount, currencyName, rate, status);

                                    Executor thread1 = Executors.newSingleThreadExecutor();
                                    thread1.execute(() -> {
                                        newConversion.id = convDAO.insertConversion(newConversion);

                                    });
                                    runOnUiThread(() -> {
                                        conversions.add(newConversion);
                                        convAdapter.notifyItemInserted(conversions.size() - 1);

                                    });
                                } catch (JSONException e) {
                                    Log.e("CurrencyConverter", "JSON Parsing Error: " + e.getMessage());
                                    throw new RuntimeException(e);
                                }
                            }, (error -> {
                        Log.e("CurrencyConverter", "Volley Error: " + error.getMessage());
                    }));
                    queue.add(request);


                    currencySubmitButton = findViewById(R.id.currency_submitButton);



                    try {
                        binding.currencyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    } catch (Exception e) {
                        Log.e("CurrencyConverter", "Error setting RecyclerView LayoutManager: " + e.getMessage());
                    }
                });
    }

    /**
     * MyRowHolder class for the recycler view rows.
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
            TextView conversionText;
        public MyRowHolder (@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                SingleConversion selected = conversions.get(position);

                convModel.selectedConversion.postValue(selected);

            });
            conversionText = itemView.findViewById(R.id.currencyTextView);
        }
    }

    /**
     * onPause method to save the sharedpreferences
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Save Shared Preferences when the activity is paused
        String baseCurrency = editTextBaseCurrency.getText().toString();
        String endCurrency = editTextEndCurrency.getText().toString();
        String amountCurrency = editTextAmount.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_BASE_CURRENCY, baseCurrency);
        editor.putString(KEY_END_CURRENCY, endCurrency);
        editor.putString(KEY_AMOUNT, amountCurrency);
        editor.apply();
    }

}