package algonquin.cst2335.finalproject;
import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 *Database class that extends Room DB.
 * @author Connor McHugh
 * @version 1.0
 */
@Database(entities = {SingleConversion.class}, version=2)
public abstract class ConversionDatabase extends RoomDatabase {
    /**
     * Data access object
     * @return currency DAO
     */
    public abstract SingleConversionDAO cDAO();
}
