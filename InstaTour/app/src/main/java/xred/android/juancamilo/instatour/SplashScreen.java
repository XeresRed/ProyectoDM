package xred.android.juancamilo.instatour;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends Activity {
    private TextView t1;
    private TextView t2;
    private ImageView Iv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        t1 = findViewById(R.id.insta);
        t2 = findViewById(R.id.tour);
        Iv1 = findViewById(R.id.log);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.transicion);
        t1.startAnimation(anim);
        t2.startAnimation(anim);
        Iv1.startAnimation(anim);
        final Intent i = new Intent(SplashScreen.this, MainActivity.class);
        Thread t = new Thread(){
            public void run(){
                try {
                    sleep(3200);

                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };

        t.start();

    }
}
