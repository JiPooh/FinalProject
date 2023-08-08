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
     * recycler view to display all the saved images
     */
    private RecyclerView.Adapter bearAdapter;
    /**
     * class that holds all the MutableLiveData of BearImg
     */
    private BearViewModel bearViewModel;
    /**
     * Array of BearImg objects
     */
    private ArrayList<BearImg> bearImages;
    /**
     * generated image of bear
     */
    private Bitmap pictureGen = null;
    /**
     * fragment to be used for user interaction with saved image files
     */
    private BearImgFragment bearFragment;
    /**
     * BearImg object that is made with the generate button
     */
    private BearImg newMadeBear = null;
    /**
     * position of BearImg object in the Array bearImages
     */
    int position = 0;
    /**
     * variable to check if the fragment is open
     */
    int openFrag = 0;
    /**
     * shared preference to store the bear number for image naming  when saving to storage
     * and position of selected image from recycler view for fragment
     */
    protected SharedPreferences sharedPreferences;
    /**
     * editor of SharedPreference to update its values
     */
    protected SharedPreferences.Editor bearEdit;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bear_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bearQueue = Volley.newRequestQueue(this); // Volley for retrieving data from url
        ActivityBearBinding bearBinding = ActivityBearBinding.inflate(getLayoutInflater());
        setContentView(bearBinding.getRoot());
        //initiate SharedPreference and its editor
        sharedPreferences = getSharedPreferences("bearPref", MODE_PRIVATE);
        bearEdit = sharedPreferences.edit();
        //initiate ViewModel and its MutableLiveData
        bearViewModel = new ViewModelProvider(this).get(BearViewModel.class);
        bearImages = bearViewModel.bearImages.getValue();
        //retrieve bearNum from SharedPreference
        position = sharedPreferences.getInt("bearNum", 0);
        //Layout for Recyclerview to be grid to fit multiple images per line
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        //checks if Array of BearImg is null, creates new if null
        if (bearImages == null) {
            bearViewModel.bearImages.postValue(bearImages = new ArrayList<>());
        }
        bearImages.clear(); //prevents the duplication of recycler view objects on rotation
        //initiates recycler view
        bearBinding.bearIMGRV.setLayoutManager(layoutManager);
        /**
         * initiates recycler view to display all the images in the BearImages
         */
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
        //initiates toolbar
        setSupportActionBar(bearBinding.bearTool);
        //initiates File for access to device storage to retrieve saved images
        File[] savedImageFiles = getFilesDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("bear") && name.endsWith(".PNG");
            }
        });
        //checks if there is any saved images in the device storage
        if (savedImageFiles != null) {
            //a for loop to retrieve information of all saved images from storage
            for (File imageFile : savedImageFiles) {
                /**
                 * a try block to extract information of each saved image files and save them in the
                 * BearImages Array
                 * ID from name of file and bitmap from image
                 */
                try {
                    FileInputStream fis = new FileInputStream(imageFile);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    String fileName = imageFile.getName();
                    int startPos = fileName.indexOf("bear") + "bear".length();
                    int endPos = fileName.indexOf(".PNG");
                    String positionStr = fileName.substring(startPos, endPos);
                    int IDofFile = Integer.parseInt(positionStr);
                    BearImg bearimg = new BearImg(IDofFile, bitmap);
                    bearImages.add(bearimg);
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //sets bearNum as position for ID of BearImg
        position = sharedPreferences.getInt("bearNum", 0);
        /**
         * onClickListener for Button for generating new BearImg
         */
        bearBinding.bearGen.setOnClickListener(clk -> {
            /**
             * try block to request bitmap data from url created with user input
             */
            try {
                openFrag = 1;
                int bearWidth = Integer.parseInt(bearBinding.bearWidth.getText().toString());
                int bearHeight = Integer.parseInt(bearBinding.bearHeight.getText().toString());
                if (bearHeight < 0 || bearWidth < 0) {
                    Toast.makeText(this, "INVALID VALUE FOR HEIGHT/WIDTH, MUST BE A POSITIVE INTEGER", Toast.LENGTH_SHORT).show();
                } else if (bearHeight > 7599 || bearWidth > 7599) { //maximum value
                    Toast.makeText(this, "INVALID VALUE FOR HEIGHT/WIDTH, MAXIMUM VALUE 7599", Toast.LENGTH_SHORT).show();
                } else {
                    String bearURL = "https://placebear.com/" + bearWidth + "/" + bearHeight;
                    ImageRequest bearImg = new ImageRequest(bearURL, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            pictureGen = response;
                            newMadeBear = new BearImg(position, pictureGen);
                            bearViewModel.bearImage.postValue(newMadeBear);
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
        /**
         * onClickListener for button for saving generated bitmap data
         */
        bearBinding.bearSave.setOnClickListener(clk -> {
            /**
             * alert to check with user if they are certain of saving image
             */
            AlertDialog.Builder builder = new AlertDialog.Builder(Bear.this);
            builder.setMessage("Do you wish to save this image?")
                    .setTitle("Question")
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        if (newMadeBear != null) {
                            FileOutputStream bearOut = null;
                            try {
                                if(pictureGen != null) {
                                    position = sharedPreferences.getInt("bearNum", 0);
                                    int ID = position;
                                    String fileName = "bear" + ID + ".PNG";
                                    bearOut = openFileOutput(fileName, Context.MODE_PRIVATE);
                                    pictureGen.compress(Bitmap.CompressFormat.PNG, 100, bearOut);
                                    bearOut.flush();
                                    bearOut.close();
                                    bearImages.add(newMadeBear);
                                    bearAdapter.notifyItemInserted(ID);
                                    /**
                                     * snackbar to undo the save in case user changes mind
                                     */
                                    Snackbar.make(bearBinding.bearIMGRV, "You have saved this image!", Snackbar.LENGTH_LONG)
                                            .setAction("UNDO", clck -> {
                                                    File imageFile = new File(getFilesDir(), fileName);
                                                    imageFile.delete();
                                                    bearImages.remove(newMadeBear);
                                                    bearAdapter.notifyItemRemoved(ID);
                                            })
                                            .show();
                                }else{
                                    Toast.makeText(this, "You have already saved this image!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
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
        //initiate observer for MutableLiveData in BearViewModel class
        bearBinding.bearIMGRV.setLayoutManager(new GridLayoutManager(this, 3));
        /**
         * observer which watches for changes in MutableLiveData in BearViewModel
         * and creates fragment for the recyclerview for user interaction
         */
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

    /**
     * ViewHolder for the recycler view to display the image of saved files
     */
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

    /**
     * onClickListener for the delete button in the BearImgFragment to delete the selected image file
     * from storage
     */
    public void setDelBearClickListener() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Bear.this);
        builder.setMessage("Do you wish to delete this image?")
                .setTitle("Question")
                .setPositiveButton("Yes", (dialog, cl) -> {
                        int selectedPos = sharedPreferences.getInt("selected", -1);
                        BearImg forDel = bearImages.get(selectedPos);
                        int IDforDel = forDel.getID();
                        String fileName = "bear" + (IDforDel) + ".PNG";
                        File imageFile = new File(getFilesDir(), fileName);
                        imageFile.delete();
                        bearImages.remove(selectedPos);
                        bearAdapter.notifyItemRemoved(selectedPos);
                        position = bearImages.size() + 1;
                        bearEdit.putInt("bearNum", position);
                        bearEdit.apply();

                        Toast.makeText(this, "Image Deleted!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, cl) -> {
                })
                .create().show();
    }
}