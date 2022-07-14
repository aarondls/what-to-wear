package com.example.whattowear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattowear.models.Accessories;
import com.example.whattowear.models.Clothing;
import com.example.whattowear.models.Footwear;
import com.example.whattowear.models.LowerBodyGarment;
import com.example.whattowear.models.OverBodyGarment;
import com.example.whattowear.models.UpperBodyGarment;

import java.util.ArrayList;
import java.util.List;

public class ClothingAdapter extends RecyclerView.Adapter<ClothingAdapter.ViewHolder> {
    private static final String TAG = "ClothingAdapter";

    private Context context;
    private List<String> clothingTypes;
    private List<String> clothingNames;
    private List<Integer> clothingIcons;

    public ClothingAdapter(Context context) {
        this.context = context;

        // initialize list of names and icons to display
        clothingTypes = new ArrayList<>();
        clothingNames = new ArrayList<>();
        clothingIcons = new ArrayList<>();

        OverBodyGarment overBodyGarment = Clothing.getOverBodyGarment();

        // check if there is clothing data to display
        if (!Clothing.hasPreloadedDataToDisplay()) {
            return;
        }

        Integer overBodyGarmentImage = overBodyGarment.getOverBodyGarmentImage();
        if (overBodyGarmentImage != null) {
            // there is an overbody garment
            clothingTypes.add(OverBodyGarment.getOverBodyGarmentName());
            clothingNames.add(overBodyGarment.getOverBodyGarmentTypeName());
            clothingIcons.add(overBodyGarmentImage);
        }

        UpperBodyGarment upperBodyGarment = Clothing.getUpperBodyGarment();
        clothingTypes.add(UpperBodyGarment.getUpperBodyGarmentName());
        clothingNames.add(upperBodyGarment.getUpperBodyGarmentTypeName());
        clothingIcons.add(upperBodyGarment.getUpperBodyGarmentImage());

        LowerBodyGarment lowerBodyGarment = Clothing.getLowerBodyGarment();
        clothingTypes.add(LowerBodyGarment.getLowerBodyGarmentName());
        clothingNames.add(lowerBodyGarment.getLowerBodyGarmentTypeName());
        clothingIcons.add(lowerBodyGarment.getLowerBodyGarmentImage());

        Footwear footwear = Clothing.getFootwear();
        clothingTypes.add(Footwear.getFootwearName());
        clothingNames.add(footwear.getFootwearTypeName());
        clothingIcons.add(footwear.getFootwearImage());

        Accessories accessories = Clothing.getAccessories();
        List<Integer> accessoriesIcons = accessories.getAccessoriesImages();
        List<String> accessoriesNames = accessories.getAccessoriesTypeNames();
        for (int i=0; i<accessoriesIcons.size(); i++) {
            clothingTypes.add(Accessories.getAccessoriesName());
            clothingNames.add(accessoriesNames.get(i));
            clothingIcons.add(accessoriesIcons.get(i));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clothing_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String clothingType = clothingTypes.get(position);
        String clothingName = clothingNames.get(position);
        Integer clothingIcon = clothingIcons.get(position);

        holder.bind(clothingType, clothingName, clothingIcon);
    }

    @Override
    public int getItemCount() {
        return clothingNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView clothingTypeTextview;
        TextView clothingNameTextview;
        ImageView clothingIconImageview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clothingTypeTextview = itemView.findViewById(R.id.detailed_clothing_type_textview);
            clothingNameTextview = itemView.findViewById(R.id.detailed_clothing_name_textview);
            clothingIconImageview = itemView.findViewById(R.id.detailed_clothing_icon_imageview);
        }

        public void bind(String clothingType, String clothingName, Integer clothingIcon) {
            clothingTypeTextview.setText(clothingType);
            clothingNameTextview.setText(clothingName);
            clothingIconImageview.setImageResource(clothingIcon);
        }
    }
}
