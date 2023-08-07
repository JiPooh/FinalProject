package algonquin.cst2335.finalproject.bear;

import android.graphics.Bitmap;

public class BearImg {
    protected Bitmap pictureBear;
    protected int ID;
    BearImg(){}
    BearImg(int n, Bitmap m){
        ID = n;
        pictureBear = m;
    }
    public int getID(){
        return ID;
    }
    public Bitmap getPictureBear(){
        return pictureBear;
    }
    public void setPictureBear(Bitmap pictureBear){
        this.pictureBear = pictureBear;
    }
    public void setID(int n){this.ID = n;}
}
