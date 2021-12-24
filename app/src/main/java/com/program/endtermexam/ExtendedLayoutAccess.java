package com.program.endtermexam;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public final class ExtendedLayoutAccess{
    private static ImageView imageView_icon2;
    private static TextView textView_current_activityTitle;
    private ExtendedLayoutAccess(){
//        imageView_icon2
    }
//    public ExtendedLayoutAccess(Toolbar actionbar_pacefy, Activity activity, String appbarTitle){
////        if (actionbar_pacefy == null)
////            actionbar_pacefy = activity.findViewById(R.id.actionbar_pacefy);
//
//        imageView_icon2 = actionbar_pacefy.findViewById(R.id.imageView_icon2);
//        textView_current_activityTitle = actionbar_pacefy.findViewById(R.id.textView_current_activityTitle);
//
//        if (activity.getClass().equals(Dashboard.class))
//            imageView_icon2.setImageResource(R.drawable.ic_icon_2);
//        if (appbarTitle != null)
//            textView_current_activityTitle.setText(appbarTitle);
//        imageView_icon2.setOnClickListener( v -> { activity.finish(); });
//    }
    public static void AccessAppBar(Toolbar actionbar_pacefy, Activity activity, String appbarTitle){
        if (actionbar_pacefy == null)
            actionbar_pacefy = activity.findViewById(R.id.actionbar_pacefy);
        imageView_icon2 = actionbar_pacefy.findViewById(R.id.imageView_icon2);
        textView_current_activityTitle = actionbar_pacefy.findViewById(R.id.textView_current_activityTitle);

        if (activity.getClass().equals(Dashboard.class))
            imageView_icon2.setImageResource(R.drawable.ic_icon_2);
        if (appbarTitle != null)
            textView_current_activityTitle.setText(appbarTitle);

        imageView_icon2.setOnClickListener( v -> { activity.finish(); });
    }
}
