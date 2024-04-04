package com.septian.kalkulus;

import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.septian.kalkulus.helper.ImageSetterFromStream;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageSetterFromStream imageSetterFromStream = new ImageSetterFromStream(this);
        imageSetterFromStream.setAsImageDrawable("logoUndira.png", R.id.universityLogo);
    }
}