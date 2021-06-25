package com.example.hostco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class CovidTestActivity extends AppCompatActivity {

    private int activeContainer=1;
    private ImageView imgBackCovidTestActivity,imgResult;
    private MaterialButton btnNextCovidTestActivity;
    private MaterialTextView txtResult;

    private RelativeLayout container1CovidTestActivity,container2CovidTestActivity,container3CovidTestActivity,container4CovidTestActivity;
    private Switch switch1,switch2,switch3,switch4,switch5,switch6,switch7,switch8,switch9,switch10,switch11,switch12,switch13,switch14;

    private boolean[] state=new boolean [14];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_test);

        btnNextCovidTestActivity=findViewById(R.id.btnNextCovidTestActivity);
        imgBackCovidTestActivity=findViewById(R.id.imgBackCovidTestActivity);
        container4CovidTestActivity=findViewById(R.id.container4CovidTestActivity);


        container1CovidTestActivity=findViewById(R.id.container1CovidTestActivity);
        switch1=findViewById(R.id.switch1);
        switch2=findViewById(R.id.switch2);
        switch3=findViewById(R.id.switch3);
        switch4=findViewById(R.id.switch4);
        switch5=findViewById(R.id.switch5);

        container2CovidTestActivity=findViewById(R.id.container2CovidTestActivity);
        switch6=findViewById(R.id.switch6);
        switch7=findViewById(R.id.switch7);
        switch8=findViewById(R.id.switch8);
        switch9=findViewById(R.id.switch9);

        container3CovidTestActivity=findViewById(R.id.container3CovidTestActivity);
        switch10=findViewById(R.id.switch10);
        switch11=findViewById(R.id.switch11);
        switch12=findViewById(R.id.switch12);
        switch13=findViewById(R.id.switch13);
        switch14=findViewById(R.id.switch14);

        container4CovidTestActivity=findViewById(R.id.container4CovidTestActivity);
        imgResult=findViewById(R.id.imgResult);
        txtResult=findViewById(R.id.txtResult);


        btnNextCovidTestActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeContainer==1){
                    state[0]=switch1.isChecked();
                    state[1]=switch2.isChecked();
                    state[2]=switch3.isChecked();
                    state[3]=switch4.isChecked();
                    state[4]=switch5.isChecked();
                    container1CovidTestActivity.setVisibility(View.GONE);
                    container2CovidTestActivity.setVisibility(View.VISIBLE);
                    activeContainer=2;
                }
                else if(activeContainer==2){
                    state[5]=switch6.isChecked();
                    state[6]=switch7.isChecked();
                    state[7]=switch8.isChecked();
                    state[8]=switch9.isChecked();
                    container2CovidTestActivity.setVisibility(View.GONE);
                    container3CovidTestActivity.setVisibility(View.VISIBLE);
                    btnNextCovidTestActivity.setText("Result");
                    activeContainer=3;
                }
                else if(activeContainer==3){
                    state[9]=switch10.isChecked();
                    state[10]=switch11.isChecked();
                    state[11]=switch12.isChecked();
                    state[12]=switch13.isChecked();
                    state[13]=switch14.isChecked();
                    btnNextCovidTestActivity.setText("End");
                    container3CovidTestActivity.setVisibility(View.GONE);
                    container4CovidTestActivity.setVisibility(View.VISIBLE);
                    activeContainer=4;
                    checkTest();
                }

                else if(activeContainer==4){
                    startActivity(new Intent(CovidTestActivity.this,MainUserActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

            }
        });

        imgBackCovidTestActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeContainer==1){
                    btnNextCovidTestActivity.setText("NEXT");
                    startActivity(new Intent(CovidTestActivity.this,MainUserActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                else if(activeContainer==2){
                    btnNextCovidTestActivity.setText("NEXT");
                    container2CovidTestActivity.setVisibility(View.GONE);
                    container1CovidTestActivity.setVisibility(View.VISIBLE);
                    activeContainer=1;
                }
                else if(activeContainer==3){
                    btnNextCovidTestActivity.setText("NEXT");
                    container3CovidTestActivity.setVisibility(View.GONE);
                    container2CovidTestActivity.setVisibility(View.VISIBLE);
                    activeContainer=2;
                }
                else if(activeContainer==4){
                    btnNextCovidTestActivity.setText("Result");
                    container4CovidTestActivity.setVisibility(View.GONE);
                    container3CovidTestActivity.setVisibility(View.VISIBLE);
                    activeContainer=3;
                }
            }
        });


    }

    private void checkTest() {
        int isTrue=0;
        for (int i=0;i<state.length;i++){
            if(state[i]){
                isTrue++;
            }
        }

        if(isTrue>5){
            txtResult.setText(R.string.resultBad);
            txtResult.setTextColor(Color.parseColor("#CD102C"));
            imgResult.setImageResource(R.drawable.red1);
        }
        else{
            txtResult.setTextColor(Color.parseColor("#338E02"));
            imgResult.setImageResource(R.drawable.green1);
            if(isTrue==0){
                txtResult.setText(R.string.resultOk);
            }
            else{
                txtResult.setText(R.string.resultGood);
            }
        }


    }



}