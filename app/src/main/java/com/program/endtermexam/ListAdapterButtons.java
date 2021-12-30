package com.program.endtermexam;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ListAdapterButtons extends ArrayAdapter<ListButton> {
    private static final String TAG = "ListAdapterButtons" ;
    private Context context;

    public ListAdapterButtons(@NonNull Context context, int resource, @NonNull ArrayList<ListButton> objects) {
        super(context, resource, objects);
        this.context = context;
    }
}
