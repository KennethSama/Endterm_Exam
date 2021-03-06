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
import android.widget.Toast;

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
    private static ImageView imageView_icon2, imageView_menu;
    private static TextView textView_current_activityTitle;
    private static RelativeLayout relativeLayout_modalMessage;
    private static Activity current_activity;
    private static TextView textView_messageTitle, textView_message;
    private static LinearLayout navbar_header;
    private static TextView textView_fullname, textView_email, textView_location, textView_program;

    private static LinkedList<MaterialButton> nav_buttons;
    private static LinearLayout navbar_menu;
    private static Handler handler = new Handler();
    private static Handler delayHandler = new Handler();
    private static Runnable runnable;
    private static int delay = 5*1000;
    private static MaterialButton current_navBarButton, dashboard_navBarButton, profile_navBarButton,
            settings_navBarButton, help_navBarButton, logout_navBarButton;
    private static ImageView imageView_goBack;

    private static RelativeLayout nav_bar;

    private ExtendedLayoutAccess(){ }
    public static void AccessAppBar(Toolbar actionbar_pacefy, Activity activity, String appbarTitle){
        current_activity = activity;
        if (actionbar_pacefy == null)
            actionbar_pacefy = current_activity.findViewById(R.id.actionbar_pacefy);

        imageView_icon2 = actionbar_pacefy.findViewById(R.id.imageView_icon2);
        imageView_menu = actionbar_pacefy.findViewById(R.id.imageView_menu);
        textView_current_activityTitle = actionbar_pacefy.findViewById(R.id.textView_current_activityTitle);

        if (current_activity.getClass().equals(Dashboard.class) ||
            current_activity.getClass().equals(ProfileMenu.class) ||
            current_activity.getClass().equals(TeacherDashboard.class)
        )
            imageView_icon2.setImageResource(R.drawable.ic_icon_2);
        if ( appbarTitle != null && !(
            current_activity.getClass().equals(Dashboard.class) ||
            current_activity.getClass().equals(ProfileMenu.class) ||
            current_activity.getClass().equals(TeacherDashboard.class)) )
        {
            textView_current_activityTitle.setText(appbarTitle);
            imageView_icon2.setOnClickListener(v -> { current_activity.finish(); });
        }

        imageView_menu.setOnClickListener(v -> ShowNavBar());
        Log.d("ClassName", current_activity.getClass().toString());
    }

    public static void AccessNavBar(RelativeLayout relativeLayout_navbar, String activityTitle, String fullname, String email, String location, String program, String type){
        if (relativeLayout_navbar == null)
            relativeLayout_navbar = current_activity.findViewById(R.id.navbar_layout);

        nav_bar = relativeLayout_navbar;
        nav_buttons = new LinkedList<>();
        navbar_menu = nav_bar.findViewById(R.id.navbar_menu);
        navbar_header = nav_bar.findViewById(R.id.navbar_header);
        imageView_goBack = nav_bar.findViewById(R.id.button_goBack);

        logout_navBarButton = navbar_menu.findViewById(R.id.nav_logout);
        dashboard_navBarButton = navbar_menu.findViewById(R.id.nav_dashboard);
        profile_navBarButton = navbar_menu.findViewById(R.id.nav_profile);
        settings_navBarButton = navbar_menu.findViewById(R.id.nav_settings);
        help_navBarButton = navbar_menu.findViewById(R.id.nav_help);

        textView_fullname = navbar_header.findViewById(R.id.textView_fullname);
        textView_email = navbar_header.findViewById(R.id.textView_email);
        textView_location = navbar_header.findViewById(R.id.textView_location);
        textView_program = navbar_header.findViewById(R.id.textView_program);

        Log.d("Data", String.format("%s %s %s %s", fullname, email, location, program));

        textView_fullname.setText(fullname);
        textView_email.setText(email);
        textView_location.setText(location);
        textView_program.setText(program);

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

        if (type.equals("Student")) {
            if (current_activity.getClass().equals(Dashboard.class))
                dashboard_navBarButton.setOnClickListener(v -> HideNavBar());
            else
                dashboard_navBarButton.setOnClickListener(v -> OnDashboard(current_activity));
        }
        else {
            if (current_activity.getClass().equals(TeacherDashboard.class))
                dashboard_navBarButton.setOnClickListener(v -> HideNavBar());
            else
                dashboard_navBarButton.setOnClickListener(v -> OnTeacherDashboard(current_activity));
        }

        if (current_activity.getClass().equals(ProfileMenu.class))
            profile_navBarButton.setOnClickListener(v-> HideNavBar() );
        else
            profile_navBarButton.setOnClickListener(v-> OnProfile(current_activity) );

        logout_navBarButton.setOnClickListener(v -> OnLogout(current_activity) );
        help_navBarButton.setOnClickListener(v -> OnUnavailable(current_activity) );
        settings_navBarButton.setOnClickListener(v -> OnUnavailable(current_activity) );
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
    public static void OnProfile(Activity activity){
        Intent intent = new Intent(activity.getBaseContext(), ProfileMenu.class);
        activity.startActivity(intent);
        activity.finish();
    }
    public static void OnDashboard(Activity activity){
        Intent intent = new Intent(activity.getBaseContext(), Dashboard.class);
        activity.startActivity(intent);
        activity.finish();
    }
    public static void OnTeacherDashboard(Activity activity){
        Intent intent = new Intent(activity.getBaseContext(), TeacherDashboard.class);
        activity.startActivity(intent);
        activity.finish();
    }
    public static void OnUnavailable(Context context){ Toast.makeText(context, "Feature not avaible on free Accounts. \n Please subscribe to unlock.", Toast.LENGTH_LONG).show(); }

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
                    new CurrentUser().LogoutSession();
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
