package com.picaproject.pica.Util;

import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;

import java.util.ArrayList;

public class InterverToast {
    private static ArrayList<Toast> toastlist = new ArrayList<>();
    private Context context;
    private String message;
    private int duration;

    public InterverToast(){}

    public InterverToast(Context context, String message, int time){
        this.context = context;
        this.message = message;
        this.duration = time;
    }
    public void show(){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        Toast toast = Toast.makeText(context, message, duration);
        toastlist.add(toast);
        toast.show();
    }

    public static void showText(Context context, String message, int duration){
        Toast toast = Toast.makeText(context, message, duration);
        toastlist.add(toast);
        toast.show();
    }

    public void killAllToast(){
        if (toastlist.isEmpty())
            return;

        for (Toast t : toastlist){
            if (t!=null){
                t.cancel();
            }
        }
        toastlist.clear();
    }
}