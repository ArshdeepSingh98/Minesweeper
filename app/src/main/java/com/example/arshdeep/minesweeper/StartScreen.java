package com.example.arshdeep.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StartScreen extends AppCompatActivity {
    Button b_play;
    TextView tv_hello;
    EditText et_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        b_play = (Button) findViewById(R.id.b_play);
        tv_hello = (TextView) findViewById(R.id.tv_hello);
        et_username = (EditText) findViewById(R.id.et_username);

        final SharedPreferences sharedPreferences = getSharedPreferences("tic_tac_toe", MODE_PRIVATE);

        String name = sharedPreferences.getString("userName", null);
        if(name == null){
            tv_hello.setText("Welcome User ");
        }else{
            tv_hello.setText("Welcome " + name);
        }


        b_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_username.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(StartScreen.this, "Enter name !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userName", name);
                editor.commit();

                Intent i = new Intent(StartScreen.this,MainActivity.class);
                i.putExtra("username", et_username.getText().toString());
                startActivity(i);
            }
        });

    }
}
