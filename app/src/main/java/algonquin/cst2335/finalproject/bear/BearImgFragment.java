package algonquin.cst2335.finalproject.bear;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import algonquin.cst2335.finalproject.databinding.BearimagedetailBinding;
import algonquin.cst2335.finalproject.viewModel.BearViewModel;

public class BearImgFragment extends Fragment {
    BearViewModel bearMod;
    int position;
    Bitmap bearPic;
    String fileName;
    RecyclerView bearIMGRV;
    BearImgFragment(int position, BearViewModel bearMod, RecyclerView bearIMGRV){
        this.position = position;
        this.bearMod = bearMod;
        this.bearPic = bearMod.bearImages.getValue().get(position);
        this.bearIMGRV = bearIMGRV;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        BearimagedetailBinding bearFrag = BearimagedetailBinding.inflate(inflater);
        bearFrag.imageView.setImageBitmap(bearPic);
        Bear.BearRowHolder holder = (Bear.BearRowHolder) bearIMGRV.findViewHolderForAdapterPosition(position);
        if (holder != null) {
            fileName = holder.fileName;
        }
        bearFrag.bearDel.setOnClickListener(clk->{
            deleteFile(fileName);
            bearMod.bearImages.getValue().remove(position);
            bearIMGRV.getAdapter().notifyItemRemoved(position);
        });
        return bearFrag.getRoot();

    }
    private void deleteFile(String fileName) {
        if (fileName != null) {
            requireContext().deleteFile(fileName);
        }
    }
}
