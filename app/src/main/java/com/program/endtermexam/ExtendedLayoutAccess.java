package com.program.endtermexam;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

public final class ExtendedLayoutAccess{
    private static ImageView imageView_icon2;
    private static TextView textView_current_activityTitle;
    private static RelativeLayout relativeLayout_modalMessage;
    private static Activity current_activity;
    private static TextView textView_messageTitle;
    private static TextView textView_message;

    private ExtendedLayoutAccess(){
//        imageView_icon2
    }
    public static void AccessAppBar(Toolbar actionbar_pacefy, Activity activity, String appbarTitle){
        current_activity = activity;
        if (actionbar_pacefy == null)
            actionbar_pacefy = current_activity.findViewById(R.id.actionbar_pacefy);
        imageView_icon2 = actionbar_pacefy.findViewById(R.id.imageView_icon2);
        textView_current_activityTitle = actionbar_pacefy.findViewById(R.id.textView_current_activityTitle);

        if (current_activity.getClass().equals(Dashboard.class))
            imageView_icon2.setImageResource(R.drawable.ic_icon_2);
        if (appbarTitle != null) {
            textView_current_activityTitle.setText(appbarTitle);
            imageView_icon2.setOnClickListener(v -> { current_activity.finish(); });
        }
    }
    public static void InitializeModal(RelativeLayout relativeLayout){
        relativeLayout_modalMessage = relativeLayout;
        textView_messageTitle = relativeLayout.findViewById(R.id.textView_messageTitle);
        textView_message = relativeLayout.findViewById(R.id.textView_message);
    }
    public static void ShowModal(String messageTitle, String message){
        relativeLayout_modalMessage.setVisibility(View.VISIBLE);
        textView_messageTitle.setText(messageTitle);
        textView_message.setText(message);
    }
    public static void HideModal(){
        relativeLayout_modalMessage.setVisibility(View.GONE);
        textView_messageTitle.setText("");
        textView_message.setText("");
    }

    private static boolean IsConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork= connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork= connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting()) || (mobileNetwork != null & mobileNetwork.isConnectedOrConnecting());
    }

    static boolean isConnected = true;
    public static boolean monitoringConnectivity = false;

    public static ConnectivityManager.NetworkCallback connectivityCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            isConnected = true;
        }

        @Override
        public void onLost(Network network) {
            isConnected = false;
        }
    };

    public static void CheckConnetion(){


    }

    public static void CheckConnectivity(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!isConnected){
            ShowModal("Detecting Available Network", "Please wait for awhile or try to check your internet connection.");
            connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(), connectivityCallback);
            monitoringConnectivity = true;
        }
        else
            HideModal();

    }
//    public static void CheckConnection(Context context){
//        if (IsConnected(context))
//            HideModal();
//        else
//            ShowModal("Detecting Available Network", "Please wait for awhile or try to check your internet connection.");
//        Log.d("Network Status", Boolean.toString(IsConnected(context)));
//    }
}
