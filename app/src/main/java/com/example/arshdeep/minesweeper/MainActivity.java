package com.example.arshdeep.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.service.notification.NotificationListenerService;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private int n = 15;
    private int m = 10;
    private MyButton buttons[][];
    private int totalBombs = 10;
    LinearLayout rowLayouts[];
    LinearLayout mainLayout;
    boolean gameOver = false;
    Button smiley;
    public TextView tv_timer;
    TextView tv_flags;
    private int flagLeft = totalBombs;
    private int counter = 0;
    String username;
    private int correctFlag = 0;
    boolean firstClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        smiley = (Button) findViewById(R.id.smiley);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_flags = (TextView) findViewById(R.id.tv_flags);
        tv_flags.setText("" + flagLeft);
        tv_timer.setText("00");
        Bundle start_data = getIntent().getExtras();
        if(start_data == null){
            return;
        }
        username = start_data.getString("username");

        setUpBoard();
        setUpBombs(totalBombs);
        smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.newGame){
            newGame();
        }else if(id == R.id.easy){
            totalBombs = 10;
            newGame();
        }else if(id == R.id.medium){
            totalBombs = 15;
            newGame();
        }else if(id == R.id.hard){
            totalBombs = 20;
            newGame();
        }
        return true;
    }

    private void newGame(){
        firstClick = true;
        smiley.setBackgroundResource(R.drawable.smiley_up);
        flagLeft = totalBombs;
        tv_flags.setText(String.valueOf(flagLeft));
        counter = 0;
        correctFlag = 0;
        for(int i=0;i<n;i++) {
            for (int j = 0; j < m; j++) {
                buttons[i][j].setFlagged(false);
                buttons[i][j].setRevealed(false);
                buttons[i][j].setBombed(false);
                buttons[i][j].setXp(0);
                buttons[i][j].setYp(0);
                buttons[i][j].setValue(0);
            }
        }
        mainLayout.removeAllViews();
        setUpBoard();
        setUpBombs(totalBombs);
    }

    private void setUpBombs(int tB) {
        gameOver = false;
        Random r = new Random();
        while(tB > 0){
            int rx = r.nextInt(n);
            int ry = r.nextInt(m);
            if(!buttons[rx][ry].isBombed){
                tB--;
                buttons[rx][ry].setValue(-1);
                buttons[rx][ry].setBombed(true);
                for(int i = -1;i <= 1;i++){
                    for(int j =-1;j<=1;j++){
                        if(rx + i >= 0 && ry + j >= 0 && rx + i < n && ry + j < m && buttons[rx+i][ry+j].getValue() != -1){
                            buttons[rx+i][ry+j].setValue(buttons[rx+i][ry+j].getValue()+1);
                        }
                    }
                }
            }else{
                continue;
            }
        }
    }

    private void setUpBoard() {
        gameOver = false;
        buttons = new MyButton[n][m];
        rowLayouts = new LinearLayout[n];

        mainLayout.removeAllViews();

        for(int i = 0; i < n; i++){
            rowLayouts[i] = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1f);
            rowLayouts[i].setLayoutParams(params);
            rowLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
            mainLayout.addView(rowLayouts[i]);
        }

        for(int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                buttons[i][j] = new MyButton(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                buttons[i][j].setLayoutParams(params);
                buttons[i][j].setXp(i);
                buttons[i][j].setYp(j);
                buttons[i][j].setBackgroundResource(R.drawable.button);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setOnLongClickListener(this);
                rowLayouts[i].addView(buttons[i][j]);
            }
        }

    }

    private void Click(int x , int y){
        if(x >= 0 && y >= 0 && x < n && y < m && !buttons[x][y].isRevealed) {
            if (buttons[x][y].isBombed) {
                buttons[x][y].setRevealed(true);
                buttons[x][y].setBackgroundResource(R.drawable.bomb_exploded);
                gameOver = true;
                resetGame(x, y);
                return;
            }

            if(!buttons[x][y].isFlagged) {

                buttons[x][y].setRevealed(true);

                setImage(buttons[x][y]);

                if (buttons[x][y].getValue() == 0) {
                    for (int xt = -1; xt <= 1; xt++) {
                        for (int yt = -1; yt <= 1; yt++) {
                            if (xt != yt) {
                                Click(x + xt, y + yt);
                            }
                        }
                    }
                }

            }
        }
    }

    private void setImage(MyButton button){
        if(button.getValue() == 0){
            button.setBackgroundResource(R.drawable.number_0);
        }else if(button.getValue() == 1){
            button.setBackgroundResource(R.drawable.number_1);
        }else if(button.getValue() == 2){
            button.setBackgroundResource(R.drawable.number_2);
        }else if(button.getValue() == 3){
            button.setBackgroundResource(R.drawable.number_3);
        }else if(button.getValue() == 4){
            button.setBackgroundResource(R.drawable.number_4);
        }else if(button.getValue() == 5){
            button.setBackgroundResource(R.drawable.number_5);
        }else if(button.getValue() == 6){
            button.setBackgroundResource(R.drawable.number_6);
        }else if(button.getValue() == 7){
            button.setBackgroundResource(R.drawable.number_7);
        }else if(button.getValue() == 8){
            button.setBackgroundResource(R.drawable.number_8);
        }
    }

    private void resetGame(int a , int b){
        smiley.setBackgroundResource(R.drawable.smiley_down);
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                if(buttons[i][j].isBombed && !(i == a && j == b)){
                    buttons[i][j].setBackgroundResource(R.drawable.bomb_normal);
                }
                buttons[i][j].setEnabled(false);
            }
        }

    }

    private void checkGameWon(){
        //Log.e("this" , correctFlag + " " + flagLeft);
        if(correctFlag == totalBombs && flagLeft == 1){
            smiley.setBackgroundResource(R.drawable.smiley_upup);
            Toast.makeText(this, "Game Won " + username, Toast.LENGTH_SHORT).show();
            for(int i=0;i<n;i++){
                for(int j=0;j<m;j++){
                    /*
                    if(!buttons[i][j].isRevealed){
                        setImage(buttons[i][j]);
                        buttons[i][j].setRevealed(true);
                    }
                    if(buttons[i][j].isBombed && !buttons[i][j].isFlagged){
                        buttons[i][j].setBackgroundResource(R.drawable.flag);
                    }
                    */
                    buttons[i][j].setEnabled(false);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        MyButton mButton = (MyButton)v;
        if(mButton.isFlagged){
            return;
        }
        int x = mButton.getXp();
        int y = mButton.getYp();
        Click(x,y);
    }

    @Override
    public boolean onLongClick(View v) {
        MyButton mButton = (MyButton)v;
        if(mButton.isFlagged){
            mButton.setFlagged(false);
            flagLeft++;
            tv_flags.setText(String.valueOf(flagLeft));
            mButton.setBackgroundResource(R.drawable.button);
            return true;
        }
        if(!mButton.isRevealed && !mButton.isFlagged && flagLeft >= 1) {
            if(mButton.isBombed){
                correctFlag++;
                checkGameWon();
            }
            mButton.setFlagged(true);
            flagLeft--;
            tv_flags.setText("" + flagLeft);
            mButton.setBackgroundResource(R.drawable.flag);
        }
        return true;
    }
}
