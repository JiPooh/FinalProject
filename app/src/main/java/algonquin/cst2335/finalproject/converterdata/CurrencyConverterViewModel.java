package algonquin.cst2335.finalproject.converterdata;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.finalproject.SingleConversion;

/**
 *Currency converter viewmodel class that extends viewmodel.
 * @author Connor McHugh
 * @version 1.0
 */
public class CurrencyConverterViewModel extends ViewModel {
    public MutableLiveData<ArrayList<SingleConversion>> conversions = new MutableLiveData<>();
    public MutableLiveData<SingleConversion> selectedConversion = new MutableLiveData< >();
}
