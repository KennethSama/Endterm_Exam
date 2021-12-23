package com.program.endtermexam;

import android.app.Activity;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GoBack extends AppCompatActivity {
    private ImageView imageView_icon2;
    public GoBack(Toolbar actionbar_pacefy, Activity activity){
        if (actionbar_pacefy == null)
            actionbar_pacefy = activity.findViewById(R.id.actionbar_pacefy);
        imageView_icon2 = actionbar_pacefy.findViewById(R.id.imageView_icon2);
        if (activity.getClass().equals(Dashboard.class))
            imageView_icon2.setImageResource(R.drawable.ic_icon_2);
        imageView_icon2.setOnClickListener( v -> { activity.finish(); });
    }
}
