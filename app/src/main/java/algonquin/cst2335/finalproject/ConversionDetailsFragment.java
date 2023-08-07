package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.DetailsLayoutBinding;


/**
 * Fragment class
 * @author Connor McHugh
 * @version 1.0
 */
public class ConversionDetailsFragment extends Fragment {

    SingleConversion selected;

    /**
     *
     * @param conv
     */
    public ConversionDetailsFragment(SingleConversion conv){
        selected = conv;
    }

    /**
     * Method to create the view for the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the layout binding
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding newBinding = DetailsLayoutBinding.inflate(inflater);

        newBinding.currencyNameView.setText(selected.currencyName);
        newBinding.rateTextView.setText(selected.rate);
        newBinding.statusTextView.setText(selected.status);

        return newBinding.getRoot();
    }


}
