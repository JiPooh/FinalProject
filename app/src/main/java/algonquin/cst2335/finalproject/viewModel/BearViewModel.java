package algonquin.cst2335.finalproject.viewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.bear.BearImg;

/**
 * ViewModel to hold BearImg and Array of BearImg to survive rotation change and saving & deleting
 * of BearImg
 */
public class BearViewModel extends ViewModel {
    /**
     * MutableLiveData of array of BearImg
     */
    public MutableLiveData<ArrayList<BearImg>> bearImages = new MutableLiveData<>();
    /**
     * MutableLiveData of BearImg
     */
    public MutableLiveData<BearImg> bearImage = new MutableLiveData<>();
}