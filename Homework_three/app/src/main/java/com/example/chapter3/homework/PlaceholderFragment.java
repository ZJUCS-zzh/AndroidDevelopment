package com.example.chapter3.homework;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.airbnb.lottie.LottieAnimationView;

public class PlaceholderFragment extends Fragment {
    private AnimatorSet animatorSet;
    private String[] mName = { "xiaohua","liming", "xiaohong", "xiaoming", "xiaomei" ,"xiaohua","xiaoli","xiaoxin","xiaojun","xiaowu"};
    private String[] mNum = { "0000","1111", "2222", "3333", "4444","5555","6666","7777","8888","9999" };
    private ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        return inflater.inflate(R.layout.fragment_placeholder, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view=getView();
        final LottieAnimationView animation_View=view.findViewById(R.id.animation_view);
        final  ListView listView=view.findViewById(R.id.ListView);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                ObjectAnimator animator1=ObjectAnimator.ofFloat(animation_View,
                        "alpha",1f,0f);
                animator1.setRepeatCount(0);
                animator1.setDuration(1000);

                for(int i = 0; i < mNum.length; i ++){
                    Map<String,Object> item = new HashMap<String,Object>();
                    item.put("name", mName[i]);
                    item.put("num", mNum[i]);
                    mData.add(item);
                }
                listView.setAlpha(0);
                SimpleAdapter adapter = new SimpleAdapter(getActivity(),mData,android.R.layout.simple_expandable_list_item_2,
                        new String[]{"name","num"},new int[]{android.R.id.text1,android.R.id.text2});
                listView.setAdapter(adapter);
                ObjectAnimator animator2=ObjectAnimator.ofFloat(listView,
                        "alpha",0f,1f);
                animator2.setRepeatCount(0);
                animator2.setDuration(1000);
                animatorSet = new AnimatorSet();
                animatorSet.playSequentially(animator1,animator2);
                animatorSet.start();
            }
        }, 5000);
    }
}
