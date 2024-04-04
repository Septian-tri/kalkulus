package com.septian.kalkulus;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.septian.kalkulus.helper.ImageSetterFromStream;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView creditTvClickable = findViewById(R.id.openPopUpCredits);
        ImageSetterFromStream imageSetterFromStream = new ImageSetterFromStream(this);

        creditTvClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout popUpCredits = findViewById(R.id.creditsPopUpWrapper);
                Button closePopUpBtn = findViewById(R.id.closeButtonCredit);

                closePopUpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpCredits.setVisibility(View.GONE);
                        creditTvClickable.setVisibility(View.VISIBLE);
                    }
                });

                popUpCredits.setVisibility(View.VISIBLE);
                creditTvClickable.setVisibility(View.GONE);
            }
        });
        imageSetterFromStream.setAsImageDrawable("logoUndira.png", R.id.universityLogo);
    }
}