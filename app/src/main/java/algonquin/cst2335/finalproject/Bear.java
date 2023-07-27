package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;

public class Bear extends AppCompatActivity {
    private Bitmap bearImg = null;
    private RequestQueue bearQueue = null;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bearQueue = Volley.newRequestQueue(this);
        ActivityBearBinding bearBinding = ActivityBearBinding.inflate(getLayoutInflater());
        sharedPreferences = getSharedPreferences("bearPref", MODE_PRIVATE);
        SharedPreferences.Editor bearEdit = sharedPreferences.edit();
        AtomicInteger bearNum = new AtomicInteger(sharedPreferences.getInt("bearNum", 0));

        bearBinding.bearGen.setOnClickListener(clk -> {
            //generate bear
            boolean validInt = false;
            while(validInt == false) {
                try {
                    int bearWidth = Integer.parseInt(String.valueOf(bearBinding.bearWidth));
                    int bearHeight = Integer.parseInt(String.valueOf(bearBinding.bearHeight));
                    String bearURL = "https://placebear.com/" + bearWidth + "/" + bearHeight + "";
                    /*
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, bearURL, null,
                            (response) -> {
                                //
                            },
                            (error) -> {
                                //
                            });

                    bearQueue.add(request);
                    */
                    ImageRequest bearImg = new ImageRequest(bearURL, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            FileOutputStream bearOut = null;
                            try{
                                bearOut = openFileOutput("bear" + bearNum + "PNG", Context.MODE_PRIVATE);
                                response.compress(Bitmap.CompressFormat.PNG, 100, bearOut);
                                bearOut.flush();
                                bearOut.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                            (error)->{});
                    bearQueue.add(bearImg);
                    validInt = true;
                }catch(NumberFormatException e){
                    Toast.makeText(this, "Invalid input; Please enter integer values", Toast.LENGTH_SHORT).show();
                }
            }

            bearNum.getAndIncrement();
            bearEdit.putInt("bearNum", bearNum.get());
            bearEdit.apply();
            //save resolution used previously to shared pref
            Toast.makeText(this, "Bear has been generated!", Toast.LENGTH_SHORT).show();
        });
        bearBinding.bearSave.setOnClickListener(clk -> {
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