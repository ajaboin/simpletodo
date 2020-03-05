package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

    public static  final String KEY_ITEM_TEXT = "item text";
    public static  final String KEY_ITEM_POSITION = "item position";
    public static  final int EDITE_TEXT_CODE = 20;

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

       ItemsAdapter.onClickListener  onClickListener = new ItemsAdapter.onClickListener() {
           @Override
           public void onItemClicked(int position) {

               Log.d("MainActivity", "Single click at position" + position);
               Intent i = new Intent(MainActivity.this, EditActivity.class);
               i.putExtra(KEY_ITEM_TEXT, items.get(position));
               i.putExtra(KEY_ITEM_POSITION, items.get(position));
               startActivityForResult(i, EDITE_TEXT_CODE);

           }
       };
      itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDITE_TEXT_CODE) {

            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            items.set(position, itemText);
            itemsAdapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(), "items update successfully", Toast.LENGTH_SHORT);
        } else {
            Log.w("MainActivity", "unknown call");
        }
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
