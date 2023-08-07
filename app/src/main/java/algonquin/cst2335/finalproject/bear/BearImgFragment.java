package algonquin.cst2335.finalproject.bear;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.BearimagedetailBinding;
import algonquin.cst2335.finalproject.viewModel.BearViewModel;

/**
 * class for fragment, which will hold bigger sized selected image and a delete button to
 * delete the selected image
 */
public class BearImgFragment extends Fragment {
    /**
     * BearImg that is selected
     */
    private BearImg bearimg;
    /**
     * Bear activity to use some functions from
     */
    private Bear bear;
    /**
     * SharedPreference to retrieve selected BearImg object's ID and position from array
     */
    private SharedPreferences sharedPref;

    /**
     * empty constructor of fragment class
     */
    public BearImgFragment(){}

    /**
     * constructor of fragment class
     * @param m BearImg object that is selected
     * @param bear Bear activity where the Bear program runs
     */
    public BearImgFragment(BearImg m, Bear bear) {
        bearimg = m;
        this.bear = bear;
    }

    /**
     * delete button in the fragment
     */
    Button delBear;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        BearimagedetailBinding bearFrag = BearimagedetailBinding.inflate(inflater);
        sharedPref = requireActivity().getSharedPreferences("bearPref", MODE_PRIVATE);
        delBear = bearFrag.delBear;
        if (bearimg != null) {
            bearFrag.picBear.setImageBitmap(bearimg.getPictureBear());
            bearFrag.picBear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() { //used to fit the image to the device screen width
                    //get screen size
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int screenHeight = displayMetrics.heightPixels;
                    // Calculate the aspect ratio of the image
                    int imageWidth = bearimg.getPictureBear().getWidth();
                    int imageHeight = bearimg.getPictureBear().getHeight();
                    float aspectRatio = (float) imageHeight / imageWidth;
                    int maxWidth = displayMetrics.widthPixels; // Maximum width is the screen width
                    int maxHeight = (int) (maxWidth * aspectRatio);

                    // Calculate the new height based on the parent width and original aspect ratio
                    int parentWidth = bearFrag.picBear.getWidth();
                    int newHeight = (int) (parentWidth / aspectRatio);

                    bearFrag.picBear.setMaxHeight(maxHeight);
                    // Set the new height to the ImageView
                    ViewGroup.LayoutParams layoutParams = bearFrag.picBear.getLayoutParams();
                    layoutParams.height = newHeight;
                    bearFrag.picBear.setLayoutParams(layoutParams);

                    // Remove the listener to prevent multiple calls
                    bearFrag.picBear.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

    }
        /**
         * onClickListener for the delete button. function is in Bear class
         */
        bearFrag.delBear.setOnClickListener(clk->{
            bear.setDelBearClickListener();
        });

        return bearFrag.getRoot();
    }

}