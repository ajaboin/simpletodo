package com.example.simpletodo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

List <String> items;
   Button btnAdd;
   EditText editem;
   RecyclerView rvitem;
    ItemsAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd =findViewById(R.id.btnAdd);
        editem =findViewById(R.id.editem);
        rvitem = findViewById(R.id.rvitem);

        //au lieu d'un arrayList on implemente le methode load et save
        loadItems();
       // items = new ArrayList<>();
       // items.add("Bonsoir");
        //items.add("Alexandra");
        //items.add("aurevoir");

     ItemsAdapter.OnLongClickListener onLongClickListener  =   new ItemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {

                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };


      itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvitem.setAdapter(itemsAdapter);
        rvitem.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = editem.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size()-1);
                editem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    private File getDataFile(){
        return  new File(getFilesDir(), "data.txt");
    }
    private  void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    private  void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

}
