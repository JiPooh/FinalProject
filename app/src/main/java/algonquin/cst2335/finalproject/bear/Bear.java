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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

/**
 * class for bear activity where a random bear image is generated using user input for height and width value
 * the user can then save the image generated if they want
 * the user can also interact with already saved images by tapping on them
 */
public class Bear extends AppCompatActivity {
    /**
     * request for bear image from the url
     */
    private RequestQueue bearQueue = null;
    /**
     * shared preference to store the bear number for image naming  when saving to storage
     * and position of selected image from recycler view for fragment
     */
    private SharedPreferences sharedPreferences;
    /**
     * recycler view to
     */
    private RecyclerView.Adapter bearAdapter;
    private BearViewModel bearViewModel;
    private ArrayList<BearImg> bearImages;
    private Bitmap pictureGen = null;
    private BearImgFragment bearFragment;
    int position = 0;
    int openFrag = 0;

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

        if (bearImages == null) {
            bearViewModel.bearImages.postValue(bearImages = new ArrayList<BearImg>());
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
                BearImg bearImgObj = bearImages.get(position);
                Bitmap obj = bearImgObj.getPictureBear();
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
                    BearImg bearimg = new BearImg(bitmap);
                    bearImages.add(bearimg);
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        position = bearImages.size();

        bearBinding.bearGen.setOnClickListener(clk -> {
            try {
                openFrag = 1;
                int bearWidth = Integer.parseInt(bearBinding.bearWidth.getText().toString());
                int bearHeight = Integer.parseInt(bearBinding.bearHeight.getText().toString());
                if (bearHeight < 0 || bearWidth < 0) {
                    Toast.makeText(this, "INVALID VALUE FOR HEIGHT/WIDTH, MUST BE A POSITIVE INTEGER", Toast.LENGTH_SHORT).show();
                } else if (bearHeight > 7599 || bearWidth > 7599) {
                    Toast.makeText(this, "INVALID VALUE FOR HEIGHT/WIDTH, MAXIMUM VALUE 7599", Toast.LENGTH_SHORT).show();
                } else {
                    String bearURL = "https://placebear.com/" + bearWidth + "/" + bearHeight;
                    ImageRequest bearImg = new ImageRequest(bearURL, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            pictureGen = response;
                            BearImg bearImgObj = new BearImg(pictureGen);
                            bearViewModel.bearImage.postValue(bearImgObj);
                            bearBinding.bearView.setImageBitmap(pictureGen);
                            findViewById(R.id.bearFGMT).setVisibility(View.GONE);
                        }
                    }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                            (error) -> {
                            });
                    bearQueue.add(bearImg);
                    Toast.makeText(this, "Bear has been generated!", Toast.LENGTH_SHORT).show();
                    }
                } catch(NumberFormatException e){
                    Toast.makeText(this, "Please enter an integer as values", Toast.LENGTH_SHORT).show();
                }

        });

        bearBinding.bearSave.setOnClickListener(clk -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Bear.this);
            builder.setMessage("Do you wish to save this image?")
                    .setTitle("Question")
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        if (pictureGen != null) {
                            FileOutputStream bearOut = null;
                            try {
                                position = sharedPreferences.getInt("bearNum", 0);
                                int newPosition = 0;
                                for (int x = 0; x < position; x++) {
                                    String fileName = "bear" + x + ".PNG";
                                    File imageFile = new File(getFilesDir(), fileName);
                                    if (!imageFile.exists()) {
                                        position = newPosition;
                                        break;
                                    }
                                }
                                String fileName = "bear" + position + ".PNG";
                                bearOut = openFileOutput(fileName, Context.MODE_PRIVATE);
                                pictureGen.compress(Bitmap.CompressFormat.PNG, 100, bearOut);
                                bearOut.flush();
                                bearOut.close();
                                BearImg bearimg = new BearImg(pictureGen);
                                bearImages.add(bearimg);
                                bearAdapter.notifyItemInserted(bearImages.size() - 1);
                                // Update the shared preferences after saving
                                position++;
                                bearEdit.putInt("bearNum", position);
                                bearEdit.apply();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Snackbar.make(bearBinding.bearIMGRV, "You have saved this image, are you sure?", Snackbar.LENGTH_LONG)
                                    .setAction("UNDO", clck -> {
                                        int lastPosition = bearImages.size() - 1;
                                        if (lastPosition >= 0) {
                                            bearImages.remove(lastPosition);
                                            bearAdapter.notifyItemRemoved(lastPosition);

                                            // Delete the image file from storage
                                            String fileName = "bear" + (position) + ".PNG";
                                            File imageFile = new File(getFilesDir(), fileName);
                                            imageFile.delete();
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(this, "No image is generated", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, cl) -> {
                    })
                    .create().show();


        });

        bearBinding.bearIMGRV.setLayoutManager(new GridLayoutManager(this, 3));
        bearViewModel.bearImage.observe(this, a -> {
            if (openFrag == 0) {
                if (bearFragment == null || !bearFragment.isVisible()) {
                    bearFragment = new BearImgFragment(a, this);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.bearFGMT, bearFragment)
                            .addToBackStack("")
                            .commit();
                    findViewById(R.id.bearFGMT).setVisibility(View.VISIBLE);
                }
            } else {
                openFrag = 0;
            }
        });
    }
    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            // If there are fragments in the back stack, pop the fragment and go back
            getSupportFragmentManager().popBackStack();
            findViewById(R.id.bearFGMT).setVisibility(View.GONE);
        } else {
            // If there are no fragments in the back stack, handle back navigation normally
            super.onBackPressed();
        }
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
            bearView.setOnClickListener(clk->{
                position = getAbsoluteAdapterPosition();
                BearImg bearimg = bearImages.get(position);
                bearViewModel.bearImage.postValue(bearimg);
                sharedPreferences = getSharedPreferences("bearPref", MODE_PRIVATE);
                SharedPreferences.Editor bearEdit = sharedPreferences.edit();
                bearEdit.putInt("selected", position);
                bearEdit.apply();
            });
        }
    }
    public void setDelBearClickListener() {
        if (bearFragment != null) {
                int selectedPos = sharedPreferences.getInt("selected", -1);
                bearImages.remove(selectedPos);
                bearAdapter.notifyItemRemoved(selectedPos);
                String fileName = "bear" + (selectedPos + 1) + ".PNG";
                File imageFile = new File(getFilesDir(), fileName);
                imageFile.delete();

                Toast.makeText(this, "Image Deleted!", Toast.LENGTH_SHORT).show();
        }
    }
}