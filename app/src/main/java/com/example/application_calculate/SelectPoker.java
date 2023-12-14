package com.example.application_calculate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SelectPoker extends AppCompatActivity {

    // 四种花色的扑克牌的名字
    private final String[] poke = {"club", "diamond", "heart", "spade"};
    // 已经选择了的扑克牌的数量
    private int selectNum = 0;
    // 选择的扑克牌
    private int[] number = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_poker);

        layPoker();  // 放置52张牌

        // 删除按钮
        ImageButton imageButton = findViewById(R.id.clearOneBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectNum == 0) {
                    showMessage("暂时未选择扑克！！");
                    return;
                }
                ImageButton targetButton = findViewById(getResources().getIdentifier("lay"+(selectNum), "id", getPackageName()));
                // 恢复扑克位置图片
                ImageButton imageButton = findViewById(getResources().getIdentifier(targetButton.getTag().toString().replaceAll(" ", ""), "id", getPackageName()));
                imageButton.setVisibility(View.VISIBLE);
                // 恢复放置框的图片
                targetButton.setImageResource(R.drawable.lay);
                selectNum --;
            }
        });

        // 清除全部
        imageButton = findViewById(R.id.clearAllBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectNum == 0) {
                    showMessage("暂时未选择扑克！！");
                    return;
                }
                while(selectNum != 0) {
                    ImageButton targetButton = findViewById(getResources().getIdentifier("lay"+(selectNum), "id", getPackageName()));
                    // 恢复扑克位置图片
                    ImageButton imageButton = findViewById(getResources().getIdentifier(targetButton.getTag().toString().replaceAll(" ", ""), "id", getPackageName()));
                    imageButton.setVisibility(View.VISIBLE);
                    // 恢复放置框的图片
                    targetButton.setImageResource(R.drawable.lay);
                    selectNum --;
                }
            }
        });

        imageButton = findViewById(R.id.comfireBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectNum != 4) {
                    showMessage("未选择满四张扑克！！");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(SelectPoker.this, Result.class);
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });
    }

    // 弹出提示框
    private void showMessage(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setCancelable(true);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // 放置52张牌
    private void layPoker(){
        float density = getResources().getDisplayMetrics().scaledDensity;  // 获取像素点，转化单位
        int pixels = (int) (60 * density + 0.5f);
        // 4种花色
        for (int j = 0; j < 4; j ++) {
            String s = poke[j];
            int id_frame = getResources().getIdentifier("frame" + (j+1), "id", getPackageName());
            FrameLayout frameLayout  = findViewById(id_frame);  // 找到framelayout
            // 每种花色13张牌
            for (int i = 0; i < 13; i++) {
                ImageButton imageButton = new ImageButton(this);  // 新建一个imagebutton
                imageButton.setId(getResources().getIdentifier(s+(i+1), "id", getPackageName()));  // 设置id
                imageButton.setTag(s + " " + (i + 1));  // 设置一个唯一的字符串标识符 如："club 1" "diamond 1"
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(pixels, FrameLayout.LayoutParams.MATCH_PARENT); // 设置宽高
                layoutParams.leftMargin = (int) (28 * i * density + 0.5f);  // 设置边距
                imageButton.setLayoutParams(layoutParams);
                imageButton.setBackgroundResource(android.R.color.transparent);  // button背景色改成透明
                imageButton.setAdjustViewBounds(true);
                imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);  // 图片的填充方式
                int id = getResources().getIdentifier(s + (i + 1), "drawable", getPackageName());
                imageButton.setImageResource(id); // 填充扑克图片
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(selectNum == 4) {
                            showMessage("已经选满四张牌！！"); // 弹出提示框
                            return;
                        }
                        ImageButton imageButton = (ImageButton)view;
                        // 找到移动到的位置
                        ImageButton targetButton = findViewById(getResources().getIdentifier("lay"+(selectNum+1), "id", getPackageName()));
                        // ImageButton 将不再显示，并且不占据布局空间
                        imageButton.setVisibility(View.GONE);
                        // 设置目标按钮的图片为当前按钮的图片，并且设置tag
                        targetButton.setTag(imageButton.getTag());
                        targetButton.setImageDrawable(imageButton.getDrawable());
                        // 通过唯一字符串标识符tag提取点数
                        number[selectNum] = Integer.parseInt(imageButton.getTag().toString().split(" ")[1]);
                        selectNum ++;
                    }
                });
                frameLayout.addView(imageButton);  // 加入frameloyout框架
            }
        }
    }
}