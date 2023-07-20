package algonquin.cst2335.finalproject.bear;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.R;

public class Bear extends AppCompatActivity {
private Bitmap bearImg = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bear);

        findViewById(R.id.bearGen).setOnClickListener(clk->{
            //generate bear
            //save to shared pref

            Toast.makeText(this, "Bear has been generated!", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.bearSave).setOnClickListener(clk->{
            AlertDialog.Builder builder = new AlertDialog.Builder(Bear.this);
            builder.setMessage("Do you wish to save this image?")
                    .setTitle("Question")
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        //sharedPref save to phone
                        //saveBearToSharedPreferences(bearImg);
                        FileOutputStream bearOut = null;
                        try{
                            bearOut = openFileOutput("bearImg.png", Context.MODE_PRIVATE);
                            bearImg.compress(Bitmap.CompressFormat.PNG, 100, bearOut);
                            bearOut.flush();
                            bearOut.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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

    }

//    private void saveBearToSharedPreferences(Bitmap bearImg) {
//        SharedPreferences preferences = getSharedPreferences("bearImg.png", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        if(bearImg != null){
//
//        }
//    }
}