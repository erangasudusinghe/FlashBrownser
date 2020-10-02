package com.example.flash;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class history extends AppCompatActivity {
    ListView ls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();
        ls=(ListView)findViewById(R.id.listView);
        final ArrayList <String> arrayList=new ArrayList<>();
        arrayList.add("www.hirufm.lk");
        arrayList.add("https://www.google.com/");
        arrayList.add("https://www.hirufm.lk/");
        arrayList.add("www.google.com");
        arrayList.add("www.hirufm.lk");
        arrayList.add("www.google.com");
        arrayList.add("www.hirufm.lk");
        arrayList.add("www.google.com");
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        ls.setAdapter(adapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(history.this,arrayList.get(position).toString(),Toast.LENGTH_SHORT).show();
                Intent addressLoad=new Intent(history.this,NewActivity.class);
                NewActivity.historyAddress=arrayList.get(position).toString();
                startActivity(addressLoad);
            }
        });

    }
}