package algonquin.cst2335.finalproject.bear;

import android.graphics.Bitmap;

/**
 * images that are created from this program as objects to be used in other functions
 */
public class BearImg {
    /**
     * image data of the picture
     */
    protected Bitmap pictureBear;
    /**
     * ID of each pictures
     */
    protected int ID;

    /**
     * empty constructor
     */
    BearImg(){}

    /**
     * constructor of the object BearImg
     * @param n ID of the picture
     * @param m bitmap of the picture
     */
    BearImg(int n, Bitmap m){
        ID = n;
        pictureBear = m;
    }

    /**
     * getter for the ID
     * @return ID of the BearImg
     */
    public int getID(){
        return ID;
    }

    /**
     * getter for the bitmap of BearImg
     * @return bitmap data of BearImg
     */
    public Bitmap getPictureBear(){
        return pictureBear;
    }

    /**
     * setter for the bitmap data for BearImg
     * @param pictureBear bitmap image
     */
    public void setPictureBear(Bitmap pictureBear){
        this.pictureBear = pictureBear;
    }

    /**
     * setter for the ID for BearImg
     * @param n ID of BearImg
     */
    public void setID(int n){this.ID = n;}
}
