package com.program.endtermexam;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.opengl.Visibility;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.LinkedList;

public final class ExtendedLayoutAccess{
    private static ImageView imageView_icon2;
    private static ImageView imageView_menu;
    private static TextView textView_current_activityTitle;
    private static RelativeLayout relativeLayout_modalMessage;
    private static Activity current_activity;
    private static TextView textView_messageTitle;
    private static TextView textView_message;

    private static LinkedList<MaterialButton> nav_buttons;
    private static LinearLayout navbar_menu;
    private static Handler handler = new Handler();
    private static Handler delayHandler = new Handler();
    private static Runnable runnable;
    private static int delay = 5*1000;
    private static MaterialButton current_navBarButton;
    private static MaterialButton logout_navBarButton;
    private static ImageView imageView_goBack;

    private static RelativeLayout nav_bar;

    private ExtendedLayoutAccess(){
//        imageView_icon2
    }
    public static void AccessAppBar(Toolbar actionbar_pacefy, Activity activity, String appbarTitle){
        current_activity = activity;
        if (actionbar_pacefy == null)
            actionbar_pacefy = current_activity.findViewById(R.id.actionbar_pacefy);

        imageView_icon2 = actionbar_pacefy.findViewById(R.id.imageView_icon2);
        imageView_menu = actionbar_pacefy.findViewById(R.id.imageView_menu);
        textView_current_activityTitle = actionbar_pacefy.findViewById(R.id.textView_current_activityTitle);

        imageView_menu.setOnClickListener(v -> { ShowNavBar();});
        if (current_activity.getClass().equals(Dashboard.class))
            imageView_icon2.setImageResource(R.drawable.ic_icon_2);
        if (appbarTitle != null && !(current_activity.getClass().equals(Dashboard.class))) {
            textView_current_activityTitle.setText(appbarTitle);
            imageView_icon2.setOnClickListener(v -> { current_activity.finish(); });
        }
        Log.d("ClassName", current_activity.getClass().toString());
    }
    public static void AccessNavBar(RelativeLayout relativeLayout_navbar, String activityTitle){
        if (relativeLayout_navbar == null)
            relativeLayout_navbar = current_activity.findViewById(R.id.navbar_layout);

        nav_bar = relativeLayout_navbar;
        nav_buttons = new LinkedList<>();
        navbar_menu = nav_bar.findViewById(R.id.navbar_menu);
        imageView_goBack = nav_bar.findViewById(R.id.button_goBack);

        imageView_goBack.setOnClickListener(v -> { HideNavBar(); });

        int childCount = navbar_menu.getChildCount() - 3;
        for(int x=0;x< childCount; x++) {
            nav_buttons.add((MaterialButton) navbar_menu.getChildAt(x));
            if (nav_buttons.get(x).getText().toString().equals(activityTitle)){
                current_navBarButton = nav_buttons.get(x);
                current_navBarButton.setBackgroundResource(R.color.primary);
                current_navBarButton.setTextColor(ContextCompat.getColor(current_activity, R.color.white_primary));
            }
        }
        logout_navBarButton = (MaterialButton) navbar_menu.findViewById(R.id.nav_logout);
        logout_navBarButton.setOnClickListener(v -> { OnLogout(current_activity); });
    }
    public static void ShowNavBar(){ nav_bar.setVisibility(View.VISIBLE); }
    public static void HideNavBar(){ nav_bar.setVisibility(View.GONE); }
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
    public static void OnLogin(Activity activity){
        Intent intent = new Intent(activity.getBaseContext(), LoginMenu.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void OnSignup(Activity activity){
        Intent intent = new Intent(activity.getBaseContext(), SignupMenu.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void ForgotPassword(Activity activity) {
        Intent intent = new Intent(activity.getBaseContext(), ResetPassword.class);
        activity.startActivity(intent);
        activity.finish();
    }
    public static void OnLogout(Activity activity){
        AlertDialog.Builder alert_logut = new AlertDialog.Builder(activity).setTitle("Account Logout").setMessage("Are you sure you want to logout?").setCancelable(true);

        alert_logut.setPositiveButton(R.string.app_logout,
                (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    OnLogin(activity);
                    dialog.cancel();
                });
        alert_logut.setNegativeButton("Cancel", (dialog, which) -> { dialog.cancel(); });
        AlertDialog alertDialog = alert_logut.create();
        alertDialog.show();
    }

    public static boolean IsInternetAvailable() throws IOException, InterruptedException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
    private static boolean IsNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork= connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork= connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifiNetwork != null && wifiNetwork.isConnected()) || (mobileNetwork != null & mobileNetwork.isConnected());
    }
    public static void RemoveHandler(){
        handler.removeCallbacks(runnable);
    }
    public static void CheckConnection(Context context){
        handler.postDelayed(
                runnable = () -> {
                    try {
                        if (IsNetworkAvailable(context) && IsInternetAvailable())
                            HideModal();
                        else
                            ShowModal("Detecting Available Network", "Please wait for awhile or try to check your internet connection.");
                        Log.d("Network Status", Boolean.toString(IsNetworkAvailable(context)) + ", " + Boolean.toString(IsInternetAvailable()) );
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.postDelayed(runnable, delay);
                }
                , delay);
    }
}
