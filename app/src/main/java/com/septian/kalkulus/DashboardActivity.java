package com.septian.kalkulus;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.septian.kalkulus.helper.ImageSetterFromStream;

import java.lang.reflect.Array;
import java.util.Arrays;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ImageSetterFromStream setterFromStream = new ImageSetterFromStream(this);
        Button solveBtn = findViewById(R.id.solveBtn);
        Button shutdownBtn = findViewById(R.id.shutdownApps);
        EditText coefficientA = findViewById(R.id.coefficientA);
        EditText coefficientB = findViewById(R.id.coefficientB);
        EditText coefficientC = findViewById(R.id.coefficientC);

        setterFromStream.setAsImageDrawable("background_dashboard.png", R.id.dashboardBackground);

        solveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Activity activity = DashboardActivity.this;
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = getCurrentFocus();

                    if (view == null) {
                        view = new View(activity);
                    }

                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }catch (Exception e){
                    System.out.println("Failed Hidden Keyboard");
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }

                String coeA = coefficientA.getText().toString();
                String coeB = coefficientB.getText().toString();
                String coeC = coefficientC.getText().toString();

                if(coeA.equals("") || coeB.equals("") || coeC.equals("")){
                    Toast.makeText(
                        DashboardActivity.this,
                        R.string.invalid_field,
                        Toast.LENGTH_LONG
                    ).show();
                }else{
                    String stepsLog = "";
                    TextView solutionTv = findViewById(R.id.solutionTv);
                    int valCoefficientA = Integer.parseInt(coeA);
                    int valCoefficientB = Integer.parseInt(coeB);
                    int valCoefficientC = Integer.parseInt(coeC);
                    double discriminant = valCoefficientB * valCoefficientB - 4 * valCoefficientA * valCoefficientC;

                    if(valCoefficientA == 0){
                        Toast.makeText(
                            DashboardActivity.this,
                            R.string.coe_zero,
                            Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    stepsLog += getString(R.string.steps_1_log);
                    stepsLog += valCoefficientA + getString(R.string.xsqrt2_title) + " + " + valCoefficientB + "x + " + valCoefficientC + " > 0\n\n";

                    stepsLog += getString(R.string.steps_2_log);
                    stepsLog += getString(R.string.calc_discriminant);
                    stepsLog += "D = " + valCoefficientB + getString(R.string.sqrt_title) + " - 4 * " + valCoefficientA + " * " + valCoefficientC + "\n";
                    stepsLog += "    = " + discriminant + "\n\n";

                    if (discriminant > 0) {
                        double root1 = (-valCoefficientB + Math.sqrt(discriminant)) / (2 * valCoefficientA);
                        double root2 = (-valCoefficientB - Math.sqrt(discriminant)) / (2 * valCoefficientA);
                        String decimal2DigitSqrt1 = decimal2Digits(root1);
                        String decimal2DigitSqrt2 = decimal2Digits(root2);

                        stepsLog += getString(R.string.steps_3_log);
                        stepsLog +=  getString(R.string.discriminant_positive);
                        stepsLog += getString(R.string.steps_4_log);
                        stepsLog += getString(R.string.find_2square);
                        stepsLog += "x1 = (-" + valCoefficientB + " + √" + discriminant + ") / (2 * " + valCoefficientA + ")\n";
                        stepsLog += "     = " + decimal2DigitSqrt1 + "\n";
                        stepsLog += "x2 = (-" + valCoefficientB + " - √" + discriminant + ") / (2 * " + valCoefficientA + ")\n";
                        stepsLog += "     = " + decimal2DigitSqrt2 + "\n\n";
                        stepsLog += getString(R.string.steps_5_log);
                        stepsLog += getString(R.string.determine_interval_inequality);
                        stepsLog += getString(R.string.steps_6_log);
                        stepsLog += "(" + decimal2DigitSqrt1 + ", " + decimal2DigitSqrt2 + ")\n";
                    } else if (discriminant == 0) {
                        double root = -valCoefficientB / (2 * valCoefficientA);

                        stepsLog += getString(R.string.steps_3_log);
                        stepsLog += getString(R.string.zero_discriminant);
                        stepsLog += getString(R.string.steps_4_log);
                        stepsLog += getString(R.string.find_quadratic_equation);
                        stepsLog += "Akar (x) = " + root + "\n\n";
                        stepsLog += getString(R.string.steps_5_log);
                        stepsLog += getString(R.string.determine_fulfill_inequality_interval);
                        stepsLog += getString(R.string.positive_inequality_around_quadratic) + " (" + decimal2Digits(root) + ").\n\n";
                        stepsLog += getString(R.string.steps_6_log);
                        stepsLog += "(" + root + ", ∞ Tidak Terbatas)\n";
                    } else {
                        stepsLog += getString(R.string.steps_3_log);
                        stepsLog += getString(R.string.negative_discriminant_ho_have_quadratic);
                        stepsLog += getString(R.string.steps_4_log);
                        stepsLog += getString(R.string.no_solution_solve);
                    }

                    solutionTv.setText(stepsLog);
                }
            }
        });

        shutdownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finishAffinity();
              System.exit(0);
              finish();
            }
        });

    }

    private String decimal2Digits(double val){
        return String.format("%.1f", val).replace(",", ".");
    }
}