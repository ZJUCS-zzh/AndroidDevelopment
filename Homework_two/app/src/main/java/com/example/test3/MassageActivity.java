package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MassageActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage);

        TextView wait=findViewById(R.id.changed);
        Intent intent=getIntent();
        String first=intent.getStringExtra("name");
        int id=intent.getIntExtra("id",0);
        if(id>10){
            TextView wait2=findViewById(R.id.waste);
            wait2.setText(first);
            wait.setText("");
        }
        else{
            wait.setText(id+"   "+first);
        }
        View cv = getWindow().getDecorView();
        Log.d("统计页面中的View","页面中一共有"+count(cv)+"个view");
    }


    /**
     * 递归统计一个View的子View数(包含自身)
     *
     * @param root
     * @return
     */
    public int count(View root) {
        int viewCount = 0;
        if (null == root) {
            return 0;
        }
        if (root instanceof ViewGroup) {
            viewCount++;
            for (int i = 0; i < ((ViewGroup) root).getChildCount(); i++) {
                View view = ((ViewGroup) root).getChildAt(i);
                if (view instanceof ViewGroup) {
                    viewCount += count(view);
                } else {
                    viewCount++;
                }
            }
        }
        return viewCount;
    }

}
