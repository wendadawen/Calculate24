package com.example.application_calculate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.application_calculate.calculate24.Calculate24;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        int[] number = getIntent().getIntArrayExtra("number");
        ArrayList<String> ret = Calculate24.solve(number);
        LinearLayout linearLayout = findViewById(R.id.result);
        for (String s : ret) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    convertSpToPx(this, 100)
            ));
            textView.setBackgroundResource(R.color.gray);
            textView.setTextSize(convertSpToPx(this, 15));
            textView.setTextColor(ContextCompat.getColor(this, R.color.black));
            textView.setText(s);
            textView.setPadding(0, 0, 0, convertSpToPx(this, 10));
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
        }
        if(ret.size() == 0) {
            showMessage("没有计算式能够得到24！！");
        }
    }

    private int convertSpToPx(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    private void showMessage(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setCancelable(true);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setClass(Result.this, SelectPoker.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}