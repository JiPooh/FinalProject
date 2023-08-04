package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Bear extends AppCompatActivity {
    private Bitmap bearImg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bear);

        findViewById(R.id.bearGen).setOnClickListener(clk -> {
            //generate bear
            //save to shared pref

            Toast.makeText(this, "Bear has been generated!", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.bearSave).setOnClickListener(clk -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Bear.this);
            builder.setMessage("Do you wish to save this image?")
                    .setTitle("Question")
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        //sharedPref save to phone
                        //saveBearToSharedPreferences(bearImg);
                        FileOutputStream bearOut = null;
                        try {
                            bearOut = openFileOutput("bearImg.png", Context.MODE_PRIVATE);
                            bearImg.compress(Bitmap.CompressFormat.PNG, 100, bearOut);
                            bearOut.flush();
                            bearOut.close();
                            Toast.makeText(this, "toast to check if this try block activated", Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Snackbar.make(findViewById(R.id.bearIMGRV), "You have saved this image, are you sure?", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", clck -> {
                                    //remove from phone
/*
    private void deleteBearImage() {
        // Get the image file path from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        String imagePath = prefs.getString(IMAGE_FILE_PATH_KEY, "");

        if (!TextUtils.isEmpty(imagePath)) {
            // Create a File object representing the image file
            File imageFile = new File(imagePath);

            // Check if the file exists and then delete it
            if (imageFile.exists()) {
                if (imageFile.delete()) {
                    Toast.makeText(this, "Image deleted successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to delete image!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Image file not found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No image to delete!", Toast.LENGTH_SHORT).show();
        }
    }
}
*/
                                })
                                .show();
                    })
                    .setNegativeButton("No", (dialog, cl) -> {
                    })
                    .create().show();
        });
        //display img on recycler view

    }

//    private void saveBearToSharedPreferences(Bitmap bearImg) {
//        SharedPreferences preferences = getSharedPreferences("bearImg.png", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        Toast.makeText(this, "sharedpref started", Toast.LENGTH_SHORT).show();
//        if(bearImg != null){
//            ByteArrayOutputStream bearOutputStream = new ByteArrayOutputStream();
//            bearImg.compress(Bitmap.CompressFormat.PNG, 100, bearOutputStream);
//            byte[] byteArray = bearOutputStream.toByteArray();
//            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
//            editor.putString("bearImg.png", base64Image);
//            editor.apply();
//            Toast.makeText(this, "sharedpref executed", Toast.LENGTH_SHORT).show();
//        }
//    }
}