package com.example.whattowear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.whattowear.models.ActivityType;

import java.util.List;

public class ActivityAdapter extends ArrayAdapter<ActivityType> {

    private LayoutInflater layoutInflater;
    private int rowTypeResourceID;

    public ActivityAdapter(@NonNull Context context, int resource, @NonNull List<ActivityType> activityTypes) {
        super(context, resource, activityTypes);
        layoutInflater = LayoutInflater.from(context);
        rowTypeResourceID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(rowTypeResourceID, null, true);
        }

        setSingleRowElements(convertView, getItem(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(rowTypeResourceID, parent, false);
        }

        setSingleRowElements(convertView, getItem(position));

        return convertView;
    }

    /**
     * Sets the imageview and textview of a single row in the spinner to show the appropriate icon and description
     * @param view the view representing a single row
     * @param activityType the activity type of the view
     */
    private void setSingleRowElements(View view, ActivityType activityType) {
        TextView activityDescription = view.findViewById(R.id.activity_description_textview);
        activityDescription.setText(activityType.getDescription());

        ImageView activityIcon = view.findViewById(R.id.activity_icon_imageview);
        activityIcon.setImageResource(activityType.getImageID());
    }
}
