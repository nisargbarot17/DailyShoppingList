package com.example.dailyshoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.dailyshoppinglist.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class Home extends AppCompatActivity {

    private FloatingActionButton fabButton;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    private String item;
    private String quantity;
    private String unit;
    private String post_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("List").child(uId);
        mDatabase.keepSynced(true);

        recyclerView = findViewById(R.id.recycler_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);



        fabButton = findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();
            }
        });

    }

    private void customDialog(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(Home.this);

        LayoutInflater inflater = LayoutInflater.from(Home.this);
        View myView = inflater.inflate(R.layout.input_data,null);

        AlertDialog dialog = myDialog.create();
        dialog.setView(myView);


        EditText item = myView.findViewById(R.id.item);
        EditText quantity = myView.findViewById(R.id.quantity);
        Button btnSave = myView.findViewById(R.id.btn_save);
        Spinner spinner = myView.findViewById(R.id.unit);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.units));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mItem = item.getText().toString().trim();
                String mQuantity = quantity.getText().toString().trim();
                String mUnit = spinner.getSelectedItem().toString();


                if(TextUtils.isEmpty(mItem)) {
                    item.setError("Can not be empty");
                    return;
                }
                if(TextUtils.isEmpty(mQuantity)) {
                    quantity.setError("Can not be empty");
                    return;
                }

                String id = mDatabase.push().getKey();

                Data data = new Data(mItem,mQuantity,mUnit,id);

                mDatabase.child(id).setValue(data);

                Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>
        (
                Data.class,
                R.layout.item_data,
                MyViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Data model, int i) {

                viewHolder.setItem(model.getItem());
                viewHolder.setQuantity(model.getQuantity());
                viewHolder.setUnit(model.getUnit());


                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        post_key = getRef(i).getKey();
                        item = model.getItem();
                        quantity = model.getQuantity();

                        updateData();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = itemView;
        }
        public void setItem(String item){
            TextView mItem = myview.findViewById(R.id.tv_item);
            mItem.setText(item);
        }

        public void setQuantity(String quantity){
            TextView mQuantity = myview.findViewById(R.id.tv_quantity);
            mQuantity.setText(quantity);
        }

        public void setUnit(String unit) {
            TextView mUnit = myview.findViewById(R.id.tv_quantity);
            mUnit.setText(mUnit.getText() + " " + unit);
        }
    }

    public void updateData(){

        AlertDialog.Builder myDialog = new AlertDialog.Builder(Home.this);

        LayoutInflater inflater = LayoutInflater.from(Home.this);

        View mView = inflater.inflate(R.layout.update_input,null);

        AlertDialog dialog = myDialog.create();
        dialog.setView(mView);

        EditText updated_item = mView.findViewById(R.id.item_update);
        EditText updated_quantity = mView.findViewById(R.id.quantity_update);
        Spinner updated_unit = mView.findViewById(R.id.unit_update);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.units));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updated_unit.setAdapter(adapter);

        updated_item.setText(item);
        updated_item.setSelection(item.length());

        updated_quantity.setText(quantity);
        updated_quantity.setSelection(quantity.length());
         

        Button update_btn = mView.findViewById(R.id.btn_update);
        Button delete_btn = mView.findViewById(R.id.btn_delete);

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item = updated_item.getText().toString().trim();
                quantity = updated_quantity.getText().toString().trim();
                unit = updated_unit.getSelectedItem().toString();

                if(TextUtils.isEmpty(item)){
                    updated_item.setError("Can not be empty");
                }
                if(TextUtils.isEmpty(quantity)){
                    updated_quantity.setError("Can not be empty");
                }

                Data data = new Data(item,quantity,unit,post_key);

                mDatabase.child(post_key).setValue(data);

                Toast.makeText(getApplicationContext(), "Item Updated", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });
        
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.log_out) {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}