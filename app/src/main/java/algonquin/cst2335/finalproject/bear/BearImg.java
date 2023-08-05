package algonquin.cst2335.finalproject.bear;

import android.graphics.Bitmap;

public class BearImg {
    protected Bitmap pictureBear;
    BearImg(){}
    BearImg(Bitmap m){
        pictureBear = m;
    }
    public Bitmap getPictureBear(){
        return pictureBear;
    }
    public void setPictureBear(Bitmap pictureBear){
        this.pictureBear = pictureBear;
    }
}
