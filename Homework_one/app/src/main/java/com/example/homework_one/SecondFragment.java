package com.example.homework_one;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


public class SecondFragment extends Fragment {
    String str1="皮城女警 凯特琳";
    String str2="放逐之刃 瑞雯";
    String str3="暗裔剑魔 亚托克斯";
    String str4="九尾狐妖 阿狸";
    String str5="刀锋舞者 艾瑞莉娅";
    int i=0;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });




        final Button btn0=view.findViewById(R.id.button_second);
        final Button btn1 =view.findViewById(R.id.button_third);
        final Button btn2 =view.findViewById(R.id.button_fourth);
        final TextView tv1=view.findViewById(R.id.textview_second);
        final ImageView tv2=view.findViewById(R.id.imageView_second);
        final TextView tv3=view.findViewById(R.id.textview_third);

        SeekBar sb= view.findViewById(R.id.seekBar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv3.setText("The progress is "+i+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("MainActivity", "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("MainActivity", "onStopTrackingTouch");
            }
        });

        Switch sc=view.findViewById(R.id.switch1);
        sc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tv1.setTextColor(getResources().getColor(R.color.black));
                    tv3.setTextColor(getResources().getColor(R.color.black));
                    btn0.setTextColor(getResources().getColor(R.color.rosybrown));
                    btn1.setTextColor(getResources().getColor(R.color.rosybrown));
                    btn2.setTextColor(getResources().getColor(R.color.rosybrown));

                } else {
                    tv1.setTextColor(getResources().getColor(R.color.gold));
                    tv3.setTextColor(getResources().getColor(R.color.gold));
                    btn0.setTextColor(getResources().getColor(R.color.mediumslateblue));
                    btn1.setTextColor(getResources().getColor(R.color.mediumslateblue));
                    btn2.setTextColor(getResources().getColor(R.color.mediumslateblue));
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(i<4)i++;
                switch(i){
                    //case 0:tv1.setText(str1);tv2.setImageResource(R.mipmap.katherine);break;
                    case 1:tv1.setText(str2);tv2.setImageResource(R.mipmap.riven);break;
                    case 2:tv1.setText(str3);tv2.setImageResource(R.mipmap.thedarkinblade);break;
                    case 3:tv1.setText(str4);tv2.setImageResource(R.mipmap.ninetailedfox);break;
                    case 4:tv1.setText(str5);tv2.setImageResource(R.mipmap.bladedancer);break;
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(i>0)i--;
                switch(i){
                    case 0:tv1.setText(str1);tv2.setImageResource(R.mipmap.katherine);break;
                    case 1:tv1.setText(str2);tv2.setImageResource(R.mipmap.riven);break;
                    case 2:tv1.setText(str3);tv2.setImageResource(R.mipmap.thedarkinblade);break;
                    case 3:tv1.setText(str4);tv2.setImageResource(R.mipmap.ninetailedfox);break;
                    //case 4:tv1.setText(str5);tv2.setImageResource(R.mipmap.bladedancer);break;
                }
            }
        });
    }
}