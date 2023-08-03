package algonquin.cst2335.finalproject.viewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.R;

public class BearViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Bitmap>> bearImages = new MutableLiveData<>();
    public MutableLiveData<Bitmap> bearImage = new MutableLiveData<>();
}