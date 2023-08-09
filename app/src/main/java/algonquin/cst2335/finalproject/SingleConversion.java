package algonquin.cst2335.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * SingleConversion class for the conversion entity that will be stored in the db.
 * @author Connor McHugh
 * @version 1.0
 */
@Entity
public class SingleConversion {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;
    @ColumnInfo(name="baseCurrency")
    String base;
    @ColumnInfo(name="endCurrency")
    String end;
    @ColumnInfo(name="amount")
    String amount;
    @ColumnInfo(name="finalrate")
    String finalRate;
    @ColumnInfo(name="currencyName")
    String currencyName;
    @ColumnInfo(name="rate")
    String rate;
    @ColumnInfo(name="status")
    String status;


    /**
     * SingleConversion constructor with the parameters.
     * @param b base currency
     * @param e end currency
     * @param a amount of money
     * @param f final rate converted
     * @param c name of currency
     * @param r rate of conversion
     * @param s status of conversion
     */
    SingleConversion(String b, String e, String a, String f, String c, String r, String s)
    {
        base = b;
        end = e;
        amount = a;
        finalRate = f;
        currencyName = c;
        rate = r;
        status = s;
    }

    /**
     * Method to get the starting currency type
     * @return the starting currency type
     */
    public String getBaseCurrency(){
        return base;
    }

    /**
     *
     * @return the desired currency type
     */
    public String getEndCurrency() {
        return end;
    }

    /**
     * Method to get amount being converted
     * @return amount to convert
     */
    public String getAmount(){
        return amount;
    }

    /**
     * Method to retrieve the final converted amount
     * @return total amount converted
     */
    public String getFinalRate() {
        return finalRate;
    }

    /**
     * Method to get the name of the currency
     * @return currency name
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * Method to get the rate of the conversion
     * @return conversion rate
     */
    public String getRate(){
        return rate;
    }

    /**
     * Method to get the status of the conversion
     * @return conversion status
     */
    public String getStatus(){
        return status;
    }

    /**
     * Constructor for a single conversion
     */
    public SingleConversion()
    {}
}
