package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * SingleConversionDAO interface that is implemented by calling the insert and query methods.
 * @author Connor McHugh
 * @version 1.0
 */
@Dao
public interface SingleConversionDAO {

    /**
     *  Method to insert the conversion into the db.
     * @param c one conversion
     * @return
     */
    @Insert
    public long insertConversion(SingleConversion c);

    /**
     * Method to retrieve all the conversions
     * @return list of conversions
     */
    @Query("Select * from SingleConversion")
    List<SingleConversion> getAllConversions();

    @Delete
    public int deleteConversion(SingleConversion s);
}
