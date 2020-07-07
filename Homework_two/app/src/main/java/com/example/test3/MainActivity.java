package com.example.test3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.String;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.recycler.LinearItemDecoration;
import com.example.test3.recycler.MyAdapter;
import com.example.test3.recycler.TestData;
import com.example.test3.recycler.TestDataSet;

public class MainActivity extends AppCompatActivity implements MyAdapter.IOnItemClickListener {

    private static final String TAG = "TAG";
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ImageView image1=findViewById(R.id.fans);
        image1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TestData A=new TestData("你的粉丝数是1w","");
                onItemCLick(11,A);
            }
        });
        ImageView image2=findViewById(R.id.likes);
        image2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TestData A=new TestData("XXX赞了你的评论","");
                onItemCLick(12,A);
            }
        });
        ImageView image3=findViewById(R.id.mention);
        image3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TestData A=new TestData("你还没有被@哦","");
                onItemCLick(13,A);
            }
        });
        ImageView image4=findViewById(R.id.comment);
        image4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TestData A=new TestData("zzh(我)：你可真帅","");
                onItemCLick(14,A);
            }
        });
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




    private void initView() {
        recyclerView = findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        //gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(TestDataSet.getData());
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        LinearItemDecoration itemDecoration = new LinearItemDecoration(Color.BLUE);
//        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        DefaultItemAnimator animator = new DefaultItemAnimator();
//        animator.setAddDuration(3000);
//        recyclerView.setItemAnimator(animator);
        View cv = getWindow().getDecorView();
        Log.d("统计页面中的View","页面中一共有"+count(cv)+"个view");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "RecyclerViewActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "RecyclerViewActivity onResume");
        View cv = getWindow().getDecorView();
        Log.d("统计页面中的View","页面中一共有"+count(cv)+"个view");
    }


    @Override
    public void onItemCLick(int position, TestData data) {
        Intent intent = new Intent(this, MassageActivity.class);
        String A=data.getTitle()+"         "+data.getHot();
        intent.putExtra("id",position);
        intent.putExtra("name",A);
        startActivity(intent);
    }
    @Override
    public void onItemLongCLick(int position, TestData data) {
        Toast.makeText(MainActivity.this, "长按了第" + position + "条", Toast.LENGTH_SHORT).show();
        mAdapter.removeData(position);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}