package algonquin.cst2335.finalproject.bear;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;
import algonquin.cst2335.finalproject.databinding.ActivityBearViewModelBinding;
import algonquin.cst2335.finalproject.viewModel.BearViewModel;

public class Bear extends AppCompatActivity {
    private Bitmap bearImg = null;
    private RequestQueue bearQueue = null;
    private SharedPreferences sharedPreferences;
    private RecyclerView.Adapter bearAdapter;
    private BearViewModel bearViewModel;
    private ArrayList<Bitmap> bearImages;
    private Bitmap pictureGen = null;
    int position = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bear_menu, menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bearQueue = Volley.newRequestQueue(this);
        ActivityBearBinding bearBinding = ActivityBearBinding.inflate(getLayoutInflater());
        setContentView(bearBinding.getRoot());
        bearViewModel = new ViewModelProvider(this).get(BearViewModel.class);
        bearImages = bearViewModel.bearImages.getValue();
        sharedPreferences = getSharedPreferences("bearPref", MODE_PRIVATE);
        SharedPreferences.Editor bearEdit = sharedPreferences.edit();
        position = sharedPreferences.getInt("bearNum", 0);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        if(bearImages == null){
            bearViewModel.bearImages.postValue(bearImages = new ArrayList<Bitmap>());
        }
        bearImages.clear();

        bearBinding.bearIMGRV.setLayoutManager(layoutManager);
        bearBinding.bearIMGRV.setAdapter(bearAdapter = new RecyclerView.Adapter<BearRowHolder>() {
            @NonNull
            @Override
            public BearRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ActivityBearViewModelBinding bVBinding = ActivityBearViewModelBinding.inflate(getLayoutInflater());
                return new BearRowHolder(bVBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull BearRowHolder holder, int position) {
                Bitmap obj = bearImages.get(position);
                holder.bearPic.setImageBitmap(obj);
                int imageWidth = obj.getWidth(); // Width of the bitmap
                int imageHeight = obj.getHeight(); // Height of the bitmap
                int targetHeight = 400; // Adjust the target height as needed
                int targetWidth = (int) ((float) targetHeight * (float) imageWidth / (float) imageHeight);
                holder.bearPic.getLayoutParams().width = targetWidth;
                holder.bearPic.getLayoutParams().height = targetHeight;
                holder.fileName = "bear" + position + ".PNG";
            }

            @Override
            public int getItemCount() {
                return bearImages.size();
            }
        });

        setSupportActionBar(bearBinding.bearTool);
        File[] savedImageFiles = getFilesDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("bear") && name.endsWith(".PNG");
            }
        });
        if (savedImageFiles != null) {
            for (File imageFile : savedImageFiles) {
                try {
                    FileInputStream fis = new FileInputStream(imageFile);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    bearImages.add(bitmap);
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        bearBinding.bearGen.setOnClickListener(clk -> {
            try {
                String bearWidth = bearBinding.bearWidth.getText().toString();
                String bearHeight = bearBinding.bearHeight.getText().toString();
                String bearURL = "https://placebear.com/" + Integer.parseInt(bearWidth) + "/" + Integer.parseInt(bearHeight) + "";
                ImageRequest bearImg = new ImageRequest(bearURL, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        pictureGen = response;
                        bearViewModel.bearImage.postValue(pictureGen);
                        bearBinding.bearView.setImageBitmap(pictureGen);
                    }
                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                        (error) -> {
                        });
                bearQueue.add(bearImg);
                Toast.makeText(this, "Bear has been generated!", Toast.LENGTH_SHORT).show();
            }catch (NumberFormatException e){
                Toast.makeText(this, "Please enter integer as values", Toast.LENGTH_SHORT).show();
            }
            //save resolution used previously to shared pref
        });
        bearBinding.bearSave.setOnClickListener(clk -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Bear.this);
            builder.setMessage("Do you wish to save this image?")
                    .setTitle("Question")
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        if(pictureGen != null) {
                            FileOutputStream bearOut = null;
                            try {
                                bearOut = openFileOutput("bear" + position + ".PNG", Context.MODE_PRIVATE);
                                pictureGen.compress(Bitmap.CompressFormat.PNG, 100, bearOut);
                                bearOut.flush();
                                bearOut.close();
                                bearImages.add(pictureGen);
                                bearAdapter.notifyItemInserted(bearImages.size()-1);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Snackbar.make(bearBinding.bearIMGRV, "You have saved this image, are you sure?", Snackbar.LENGTH_LONG)
                                    .setAction("UNDO", clck -> {
                                        deleteFile("bear" + position + ".PNG");
                                        bearImages.remove(pictureGen);
                                        bearAdapter.notifyItemRemoved(position);
                                    })
                                    .show();
                        }else{
                            Toast.makeText(this, "No image is generated", Toast.LENGTH_SHORT).show();
                        }

            })
                    .setNegativeButton("No", (dialog, cl) -> {
                    })
                    .create().show();

            position++;
            bearEdit.putInt("bearNum", position);
            bearEdit.apply();
        });
        //display img on recycler view
        bearViewModel.bearImage.observe(this, a->{
            bearBinding.bearView.setImageBitmap(a);
        });
//        bearBinding.bearIMGRV.observe(this, c->{
//            BearImgFragment bearFrag = new BearImgFragment(position, bearViewModel, bearIMGRV);
//            FragmentManager bFMan = getSupportFragmentManager()
//            .beginTransaction()
//            .replace(R.id., bearFrag)
//            .addToBakStack("")
//            .commit();
//        })

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.bear_help){
            AlertDialog.Builder builder = new AlertDialog.Builder(Bear.this);
            builder.setMessage("Input the value of height and width of the desired image as an " +
                    "integer (the value will be in pixels) \n" +
                    "Tap on the GENERATE button to generate a random image of a bear \n" +
                    "Tap the save button to save the generated image")
                    .setTitle("HELP")
                    .setPositiveButton("OK", (dialog, clk)->{})
                    .create()
                    .show();
        }
        return true;
    }

    class BearRowHolder extends RecyclerView.ViewHolder {
        ImageView bearPic;
        String fileName;
        public BearRowHolder(@NonNull View bearView){
            super(bearView);
            bearPic = bearView.findViewById(R.id.bearImgView);
        }
    }

}