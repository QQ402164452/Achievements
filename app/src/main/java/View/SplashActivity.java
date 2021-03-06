package view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/11/22.
 * 引导页
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanced){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanced);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(AVUser.getCurrentUser()!=null){
                    intent=new Intent(SplashActivity.this,MainActivity.class);
                }else{
                    intent=new Intent(SplashActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },3000);
    }

}
