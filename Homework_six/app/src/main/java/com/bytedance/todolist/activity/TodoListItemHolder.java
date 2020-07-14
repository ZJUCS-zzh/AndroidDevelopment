package com.bytedance.todolist.activity;

import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListEntity;
import com.bytedance.todolist.database.TodoListOperator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class TodoListItemHolder extends RecyclerView.ViewHolder {
    private TextView mContent;
    private TextView mTimestamp;
    private CheckBox checkBox;
    private ImageButton delete;
    private final TodoListOperator operator;

    public TodoListItemHolder(@NonNull View itemView,TodoListOperator operator) {
        super(itemView);
        mContent = itemView.findViewById(R.id.tv_content);
        mTimestamp = itemView.findViewById(R.id.tv_timestamp);
        checkBox = itemView.findViewById(R.id.checkbox);
        delete = itemView.findViewById(R.id.btn_delete);
        this.operator=operator;
    }

    public void bind(final TodoListEntity entity) {
        if(entity==null) return;
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    entity.setDone(true);
//                    mContent.setPaintFlags(mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    operator.updateTodoList(entity);
//                }
//                else{
//                    entity.setDone(false);
//                    mContent.setPaintFlags(mContent.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
//                    operator.updateTodoList(entity);
//                }
//            }
//        });

        checkBox.setOnCheckedChangeListener(null);
        if(entity.getDone()){
            Log.d("getDone:"," "+entity.getDone());
            checkBox.setChecked(true);
            mContent.setPaintFlags(mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            Log.d("getDone:"," "+entity.getDone());
            checkBox.setChecked(false);
           mContent.setPaintFlags(mContent.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
        mTimestamp.setText(formatDate(entity.getTime()));
        mContent.setText(entity.getContent());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("getDone:"," "+entity.getDone());
                entity.setDone(b);
                operator.updateTodoList(entity);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator.deleteTodoList(entity);
            }
        });


    }

    private String formatDate(Date date) {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(date);
    }
}
