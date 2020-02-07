package com.picaproject.pica.Util;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class PermissionChecker {
    // 받고싶은 권한을 배열로 전달받음
    private String[] permission_list;
    // 권한을 요구하고싶은 액티비티
    private AppCompatActivity activity;
    // 권한을 사용자가 허용 / 거절 했을때 행동
    private PermissioEventCallback eventCallback;

    public PermissionChecker(String[] permission_list, AppCompatActivity activity) {
        this.permission_list = permission_list;
        this.activity = activity;
    }

    public PermissionChecker(String[] permission_list, AppCompatActivity activity, PermissioEventCallback eventCallback) {
        this.permission_list = permission_list;
        this.activity = activity;
        this.eventCallback = eventCallback;
    }

    // OnCreate에서 API 버전과 상관없이 무조건 호출필요
    // API 버전에 따른 체크는 함수내부에서 진행
    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = activity.checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                activity.requestPermissions(permission_list,0);
            }
        }
    }
    // 각 AppCompatActivity의 onRequestPermissionsResult 메소드를 재정의 한 후 이 PermissionChecker의 requestPermissionsResult 호출
    public void requestPermissionsResult(int requestCode,int[] grantResults){
        if(requestCode==0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //허용됬다면
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){

                }
                else {
                    if(eventCallback!=null){
                        eventCallback.OnDenial();
                    }
                    else{
                        Toast.makeText(activity.getApplicationContext(),"저장소 접근 권한을 허용해주셔야 이용이 가능합니다.",Toast.LENGTH_LONG).show();
                        activity.finish();
                    }
                    return;
                }
            }
            if(eventCallback!=null){
                eventCallback.OnPermit();
            }
        }
    }

}
