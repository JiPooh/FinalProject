package algonquin.cst2335.finalproject.bear;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.R;

public class Bear extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bear);
        findViewById(R.id.bearGen).setOnClickListener(clk->{
            Toast.makeText(this, "Bear has been generated!", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.bearSave).setOnClickListener(clk->{
            AlertDialog.Builder builder = new AlertDialog.Builder(Bear.this);
            builder.setMessage("Do you wish to save this image?")
                    .setTitle("Question")
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        //sharedPref save to phone

                        Snackbar.make(findViewById(R.id.bearIMGRV), "You have saved this image, are you sure?", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", clck -> {
                                    //remove from phone
                                })
                                .show();
                    })
                    .setNegativeButton("No", (dialog, cl) -> {
                    })
                    .create().show();
        });
        //sharedpref
        //toast somewhere

    }
}