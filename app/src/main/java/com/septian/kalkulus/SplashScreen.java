package com.septian.kalkulus;

import android.content.Intent;
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
        Button nextScreen = findViewById(R.id.dashboardCalculator);
        ImageSetterFromStream imageSetterFromStream = new ImageSetterFromStream(this);

        imageSetterFromStream.setAsImageDrawable("logoUndira.png", R.id.universityLogo);
        imageSetterFromStream.setAsImageDrawable("background.png", R.id.splashBackground);

        creditTvClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout popUpCredits = findViewById(R.id.creditsPopUpWrapper);
                Button closePopUpBtn = findViewById(R.id.closeButtonCredit);

                closePopUpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextScreen.setVisibility(View.VISIBLE);
                        popUpCredits.setVisibility(View.GONE);
                        creditTvClickable.setVisibility(View.VISIBLE);
                    }
                });

                nextScreen.setVisibility(View.GONE);
                popUpCredits.setVisibility(View.VISIBLE);
                creditTvClickable.setVisibility(View.GONE);
            }
        });
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calculatorScreenIntent = new Intent(SplashScreen.this, DashboardActivity.class);
                startActivity(calculatorScreenIntent);
                finish();
            }
        });
    }
}