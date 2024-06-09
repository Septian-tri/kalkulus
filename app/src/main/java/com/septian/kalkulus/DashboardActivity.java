package com.septian.kalkulus;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.septian.kalkulus.helper.ImageSetterFromStream;
import com.septian.kalkulus.helper.SignCheckCallback;
import org.afree.data.xy.XYSeries;
import org.afree.data.xy.XYSeriesCollection;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardActivity extends AppCompatActivity {

    private RadioGroup signGroup;
    private XYSeriesCollection dataset;
    private String sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dataset = new XYSeriesCollection();
        signGroup = findViewById(R.id.signGroup);
        XYSeries series = new XYSeries("Graph");
        Button solveBtn = findViewById(R.id.solveBtn);
        TextView solutionTv = findViewById(R.id.solutionTv);
        Button shutdownBtn = findViewById(R.id.shutdownApps);
        EditText coefficientA = findViewById(R.id.coefficientA);
        EditText coefficientB = findViewById(R.id.coefficientB);
        EditText constantCTv = findViewById(R.id.coefficientC);
        FrameLayout graphContainer = findViewById(R.id.graphContainer);
        ImageSetterFromStream setterFromStream = new ImageSetterFromStream(this);

        setterFromStream.setAsImageDrawable("background_dashboard.png", R.id.dashboardBackground);
        dataset.addSeries(series);

        signButton();

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

                int coeA = numberCheck(coefficientA.getText().toString());
                int coeB = numberCheck(coefficientB.getText().toString());
                int constantC = numberCheck(constantCTv.getText().toString());

                if(sign == null){
                    Toast.makeText(getApplicationContext(), R.string.sign_notice, Toast.LENGTH_LONG).show();
                }else{
                    solutionTv.setText(solveInequalityEquation(coeA, coeB, constantC, true));
                }


//
//                    String results = solveInequalityEquation(coeA, coeB, coeC, true);
//                    solutionTv.setText("asjhdjaksdhasjkdajk");

//                    if(!results.equals("")){
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                GraphView canvas = new GraphView(getApplicationContext(), dataset);
//                                graphContainer.addView(canvas);
//                            }
//                        });
//                    }

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

    private void signButton(){
        int childSignCount = signGroup.getChildCount();

        for(int i =0; i < childSignCount; i++){
            LinearLayout view = (LinearLayout) signGroup.getChildAt(i);
            RadioButton singleSignBtn = (RadioButton) view.getChildAt(1);
            int singleBtnClick = singleSignBtn.getId();
            singleSignBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        sign = ((TextView) view.getChildAt(0)).getText().toString() + " 0";

                        for(int j=0; j < childSignCount; j++){
                            LinearLayout view = (LinearLayout) signGroup.getChildAt(j);
                            RadioButton singleSignBtn = (RadioButton) view.getChildAt(1);
                            int singleBtnId = singleSignBtn.getId();

                            if(singleBtnClick != singleBtnId){
                                ((RadioButton) signGroup.findViewById(singleBtnId)).setChecked(false);
                            }
                        }
                    }
                }
            });
        }
    }

    private String decimal2Digits(double val){
        return String.format("%.1f", val).replace(",", ".");
    }

    private String solveInequalityEquation(int a, int b, int c, boolean showDiscriminant){
        String tempSign = "";
        String tempSteps = null;
        String tempStringCoeA = "";
        String tempStringCoeB = "";
        String constantStringC = "";
        String discriminantLabel = "D = ";

        if(a == 0){
            Toast.makeText(getApplicationContext(), R.string.coe_zero, Toast.LENGTH_LONG).show();
        }else{
            tempStringCoeA = (a == 1) ? "x²" : (a == -1) ? "-x²" : a + "x²";
            tempStringCoeB = (b == 1) ? "+x" : (b == -1) ? "-x"  :
                             (b == 0) ? "" : (b > 0) ? "+" + b + "x" : b + "x";
            constantStringC = (c == 0) ? "" : (c < 0) ? String.valueOf(c) : "+" + c;
            tempSteps = tempStringCoeA + tempStringCoeB + constantStringC + " " + sign;

            System.out.println(tempSteps);

            if (a < 0){
                String[] tempStepsArray = tempSteps.split("");
                StringBuilder tempNewSteps = new StringBuilder();

                a *= -1;
                b *= -1;
                c *= -1;

                for(int i = 0; i < tempStepsArray.length; i++){
                    String singleString = tempStepsArray[i];

                    if (singleString.equals("-") && i == 1) {
                        tempNewSteps.append("");
                    } else if (singleString.equals("-")) {
                        tempNewSteps.append("+");
                    } else if (singleString.equals("+")) {
                        tempNewSteps.append("-");
                    } else if (singleString.equals(">")) {
                        tempNewSteps.append("<");
                        tempSign = "<";
                    } else if (singleString.equals("<")) {
                        tempNewSteps.append(">");
                        tempSign = ">";
                    } else if (singleString.equals("≥")) {
                        tempNewSteps.append("≤");
                        tempSign = "≤";
                    } else if (singleString.equals("≤")) {
                        tempNewSteps.append("≥");
                        tempSign = "≥";
                    }else {
                        tempNewSteps.append(singleString);
                    }
                }

                tempSteps += "\n\n" + tempNewSteps;
                tempSteps += "\n\n" + "Koefisien a < 0 Maka Kalikan Koefisien a,b dan konstanta c dengan -1";
                tempSteps += "\n" + tempNewSteps.toString()
                        .replace('>', '=')
                        .replace('<', '=')
                        .replace('≥', '=')
                        .replace('≤', '=');
            } else {
                tempSign = sign.substring(0, 1);
                tempSteps += "\n\n" + tempSteps.replace('>', '=')
                       .replace('<', '=')
                       .replace('≥', '=')
                       .replace('≤', '=');
            }

            Double tempRootX1 = null;
            Double tempRootX2 = null;
            double tempDiscriminantSqrt = 0;
            int calcDiscriminant = b * b - 4 * a * c;
            String coeAString = String.valueOf(a);
            String coeBString = String.valueOf(b);
            String constantCString = String.valueOf(c);

            tempSteps += "\n\nMencari Diskriminan Dengan Rumus D = b²-4ac";
            discriminantLabel += (b < 0) ? "(" + coeBString + ")" :  coeBString;
            discriminantLabel += (c < 0) ? "²+4*" + coeAString + "*" + (c * -1) + "=" : "²-4*" + a + "*" + c + "=";
            discriminantLabel += String.valueOf(calcDiscriminant);
            tempSteps += "\n" + discriminantLabel;

            if(calcDiscriminant >= 0){
                tempSteps += "\n\nTemukan Akar Kuadrat Jika Diskriminan Lebih Besar Atau Sama dengan 0";

                tempDiscriminantSqrt = Math.sqrt((double) calcDiscriminant);
                tempSteps += "\n".concat("√D= √" + calcDiscriminant + " = " + decimal2Digits(tempDiscriminantSqrt));
            }

            if(calcDiscriminant == 0){
                tempSteps += "\n\nTemukan Akar Jika Diskriminant Sama Dengan 0";

                int rootX = (b * -1) / (2 * a);
                tempRootX1 = (double) rootX;
                tempRootX2 = (double) rootX;

                tempSteps += "\nx = -b / 2 * a = " +rootX;
            } else if (calcDiscriminant > 0) {
                tempSteps += "\n\nTemukan Akar Jika Diskriminan Lebih Besar Dari 0";

                tempRootX1 = ((b * -1) + tempDiscriminantSqrt) / (2 * a);
                tempRootX2 = ((b * -1) - tempDiscriminantSqrt) / (2 * a);

                tempSteps += "\nx1 = (-b + √D) / 2 * a = " + tempRootX1;
                tempSteps += "\nx2 = (-b - √D) / 2 * a = " + tempRootX2;
            }

            if(tempRootX1 != null && tempRootX1 > tempRootX2){
                tempRootX1 = tempRootX1 + tempRootX2;
                tempRootX2 = tempRootX1 - tempRootX2;
                tempRootX1 = tempRootX1 - tempRootX2;
            }

            int finalA = a;
            int finalB = b;
            int finalC = c;
            Double tempNewRoot = (tempRootX1 != null) ? tempRootX2 + 1 : 1;
            Double calFinalResult = (a * (tempNewRoot * tempNewRoot)) + (b * tempNewRoot) + c;
            Double finalTempRootX1 = tempRootX1;
            Double finalTempRootX2 = tempRootX2;

            if(calcDiscriminant > 0){
                tempSteps += "\n" + checkSign(tempSign, new SignCheckCallback() {
                    @Override
                    public String moreThanSign() {
                       if(calFinalResult > 0){
                           return "\n(-∞, " + finalTempRootX1 + "), (" + finalTempRootX2 + " ,+∞)";
                       }
                       return "\n(" + finalTempRootX1 + ", " + finalTempRootX2 + ")";
                    }

                    @Override
                    public String lessThanSign() {
                        if(calFinalResult < 0){
                            return "\n(-∞," + finalTempRootX1 + "), (" + finalTempRootX2 + " ,+∞)";
                        }
                        return "\n(" + finalTempRootX1 + ", " + finalTempRootX2 + ")";
                    }

                    @Override
                    public String moreThanEquals() {
                        if(calFinalResult >= 0){
                            return "\n(-∞, " + finalTempRootX1 + "], [" + finalTempRootX2 + " ,+∞)";
                        }
                        return "\n[" + finalTempRootX1 + ", " + finalTempRootX2 + "]";
                    }

                    @Override
                    public String lessThanEquals() {
                        if(calFinalResult <= 0){
                            return "\n(-∞, " + finalTempRootX1 + "], [" + finalTempRootX2 + " ,+∞)";
                        }
                        return "\n[" + finalTempRootX1 + ", " + finalTempRootX2 + "]";
                    }
                });
            } else if (calcDiscriminant == 0) {
                tempSteps += "\n" + checkSign(tempSign, new SignCheckCallback() {
                    @Override
                    public String moreThanSign() {
                        if(calFinalResult > 0){
                            return "\n(-∞ ," + finalTempRootX1 + "), (" + finalTempRootX2 + ", +∞)";
                        }
                        return "\nTidak Ditemukan Solusi";
                    }

                    @Override
                    public String lessThanSign() {
                        return "\nTidak Ditemukan Solusi";
                    }

                    @Override
                    public String moreThanEquals() {
                        if(calFinalResult >= 0){
                            return "\n(-∞ , +∞)";
                        }
                        return "\n" + finalTempRootX1;
                    }

                    @Override
                    public String lessThanEquals() {
                        if(calFinalResult <= 0 || (finalA * (finalTempRootX1 * finalTempRootX1)) + (finalB * finalTempRootX1) + finalC <= 0){
                            return "\n" + finalTempRootX1;
                        }
                        return "\nTidak Ada Solusi";
                    }
                });
            }else {
                tempSteps += "\n" + checkSign(tempSign, new SignCheckCallback() {
                    @Override
                    public String moreThanSign() {
                        if(calFinalResult > 0){
                            return "\n(-∞ , +∞)";
                        }
                        return "\nTidak Ada Solusi";
                    }

                    @Override
                    public String lessThanSign() {
                       return "\nTidak Ada Solusi";
                    }

                    @Override
                    public String moreThanEquals() {
                        if(calFinalResult >= 0){
                            return "\n(-∞ , +∞)";
                        }
                        return "\nTidak Ada Solusi";
                    }

                    @Override
                    public String lessThanEquals() {
                        if(calFinalResult <= 0){
                            return "\n(-∞ , +∞)";
                        }
                        return "\nTidak Ada Solusi";
                    }
                });
            }
        }

        return tempSteps;
    }

    public String checkSign(String sign, SignCheckCallback signCheckCallback){
        switch (sign) {
            case ">":
            return signCheckCallback.moreThanSign();

            case "<":
            return signCheckCallback.lessThanSign();

            case "≥":
            return signCheckCallback.moreThanEquals();

            case "≤":
            return signCheckCallback.lessThanEquals();

            default:
            return null;
        }
    }

    private int numberCheck(String number){
        if(number != null){
            Pattern pattern = Pattern.compile("^-{0,1}[0-9]{1,}$");
            Matcher matcher = pattern.matcher(number);

            if(matcher.matches()){
                return Integer.parseInt(number);
            }
        }
        return 0;
    }
}