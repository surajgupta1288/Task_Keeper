package com.android.project.taskkeeper;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddTaskActivity extends AppCompatActivity {
    private EditText metTaskTitle;
    private LinearLayout mllListItems;
    private LinearLayout mllAddListitems;
    private Button mbtnAddTask;
    private int itemIDValue=0;
    private ArrayList<com.android.project.taskkeeper.Items> items;
    private com.android.project.taskkeeper.DBHelper dbHelper;
    private boolean isupdate;
    private EditText metItem;
    private CheckBox mchitem;
    private String mtvItem;
    private int taskid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        metTaskTitle=findViewById(R.id.et_task_title);
        mllListItems=findViewById(R.id.ll_list_items);
        mllAddListitems=findViewById(R.id.ll_add_item);
        mbtnAddTask=findViewById(R.id.add_task);



        dbHelper=new com.android.project.taskkeeper.DBHelper(com.android.project.taskkeeper.AddTaskActivity.this);

        items=new ArrayList<>();


        Bundle data =getIntent().getExtras();
        if (data!=null){
            Task task= (Task) data.getSerializable("ITEM");
            isupdate=true;
            metTaskTitle.setText(task.taskTitle);
            final ArrayList<com.android.project.taskkeeper.Items> taskitems=Task.convertStringstoArrayList(task.taskItemsStrings);
            for (final com.android.project.taskkeeper.Items item:taskitems) {
                View view = LayoutInflater.from(com.android.project.taskkeeper.AddTaskActivity.this).inflate(R.layout.cell_insert_item, null);

                CheckBox mchitem = view.findViewById(R.id.ch_insert_item);
                TextView mtvItem = view.findViewById(R.id.et_insert_item);

                mchitem.setChecked(item.isItemChecked);
                mtvItem.setText(item.itemName);

                mllListItems.addView(view);



            }
            taskid=task.taskID;
            items=taskitems;
            itemIDValue++;


        }



    }

    public void onAddItemListClicked(final View view){
        mbtnAddTask.setEnabled(false);
        mllAddListitems.setEnabled(false);
        mbtnAddTask.setAlpha(0.5f);
        mllListItems.setAlpha(0.5f);
        itemIDValue++;

        View view1= LayoutInflater.from(com.android.project.taskkeeper.AddTaskActivity.this).inflate(R.layout.cell_insert_item,null);

        final EditText metItem=view1.findViewById(R.id.et_insert_item);
        final ImageView mIvDone=view1.findViewById(R.id.ic_insert_done);

        mIvDone.setVisibility(view.GONE);

        metItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    mIvDone.setVisibility(view.VISIBLE);
                }else{
                    mIvDone.setVisibility(view.GONE);
                }

            }
        });

        mIvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mbtnAddTask.setEnabled(true);
                mllAddListitems.setEnabled(true);
                mbtnAddTask.setAlpha(1.0f);
                mllListItems.setAlpha(1.0f);

                com.android.project.taskkeeper.Items newitem=new com.android.project.taskkeeper.Items();
                newitem.itemID=itemIDValue;
                newitem.itemName=metItem.getText().toString();

                items.add(newitem);
            }
        });
        mllListItems.addView(view1);

    }
    public void onAddTaskClicked(View view){
        String taskTitle=metTaskTitle.getText().toString();
        if (!taskTitle.isEmpty() && items.size()>0){
            Task task = new Task();
            task.taskTitle= taskTitle;
            task.taskItemsStrings=task.convertArrayListToJSONArrayString(items);
            if (isupdate){
                task.taskID=taskid;
                dbHelper.updateDataToDatabase(dbHelper.getWritableDatabase(),task);

            }else {
                dbHelper.insertDataToDatabase(dbHelper.getWritableDatabase(),task);
            }





            setResult(Activity.RESULT_OK);
            finish();


        }
    }

}
