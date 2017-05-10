package hinder.hinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;


public class MemoryActivity extends AppCompatActivity {
    //intent-extras
    String username;
    int mathOneCount;
    int mathOneTotal;
    int mathTwoCount;
    int mathTwoTotal;

    private int countCorrectAnswers = 0;

    private int gameCount = 0;

    private int gameOneCount = 0;
    private int gameOneTime = 0;

    private int gameTwoCount = 0;
    private int gameTwoTime= 0;

    //timer
    TextView timer;
    private static final String FORMAT = "%02d:%02d:%02d";

    private static int ROW_COUNT = 4;
    private static int COL_COUNT = 5;
    private Context context;
    private Drawable backImage;
    private int [] [] cards;
    private List<Drawable> images;
    private Card firstCard;
    private Card secondCard;
    private ButtonListener buttonListener;

    private static Object lock = new Object();

    private TableLayout mainTable;
    private UpdateCardsHandler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveDataIntent();
        loadActivity();
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    private void loadActivity(){
        //clear variables
        countCorrectAnswers = 0;

        //increase game count
        gameCount++;

        handler = new UpdateCardsHandler();
        loadImages();
        setContentView(R.layout.activity_memory);
        backImage = getResources().getDrawable(R.drawable.icon);

        buttonListener = new ButtonListener();
        mainTable = (TableLayout) findViewById(R.id.TableLayout03);
        context = mainTable.getContext();
        newGame(ROW_COUNT, COL_COUNT);

        //timer!
        timer =(TextView)findViewById(R.id.text_countdown);
        new CountDownTimer(40000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {

                timer.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timer.setText("Time up!");

                //send backend number of correct answers and finishTime
                //CHECK IF ITs SECOND ITERATION OF GAME!
                if (gameCount < 2) {
                    gameOneCount = countCorrectAnswers;
                    gameOneTime = 40000;

                    Toast.makeText(MemoryActivity.this, "Starting Second Session...", Toast.LENGTH_SHORT).show();
                    loadActivity();
                } else if (gameCount >= 2) {
                    gameTwoCount = countCorrectAnswers;
                    gameTwoTime = 40000;

                    sendDataIntent();

                }
            }
        }.start();
    }

    private void receiveDataIntent(){
        username = getIntent().getStringExtra("USERNAME");
        Log.i("USERNAME:", username);

        mathOneCount = getIntent().getIntExtra("MATH_ONE_COUNT", 0);
        Log.i("MATH_ONE_COUNT:", Integer.toString(mathOneCount));

        mathOneTotal = getIntent().getIntExtra("MATH_ONE_TOTAL", 0);
        Log.i("MATH_ONE_TOTAL:", Integer.toString(mathOneTotal));

        mathTwoCount = getIntent().getIntExtra("MATH_TWO_COUNT", 0);
        Log.i("MATH_TWO_COUNT:", Integer.toString(mathTwoCount));

        mathTwoTotal = getIntent().getIntExtra("MATH_TWO_TOTAL", 0);
        Log.i("MATH_TWO_TOTAL:", Integer.toString(mathTwoTotal));
    }

    private void sendDataIntent(){
        Intent intent = new Intent(MemoryActivity.this, ColorActivity.class);

        intent.putExtra("USERNAME", username);
        Log.i("USERNAME:", username);
        intent.putExtra("MEM_ONE_COUNT", gameOneCount);
        Log.i("MEM_ONE_COUNT:", Integer.toString(gameOneCount));
        intent.putExtra("MEM_ONE_TIME", gameOneTime);
        Log.i("MEM_ONE_TIME:", Integer.toString(gameOneTime));
        intent.putExtra("MEM_TWO_COUNT", gameTwoCount);
        Log.i("MEM_TWO_COUNT:", Integer.toString(gameTwoCount));
        intent.putExtra("MEM_TWO_TIME", gameTwoTime);
        Log.i("MEM_TWO_TIME:", Integer.toString(gameTwoTime));

        intent.putExtra("MATH_ONE_COUNT", mathOneCount);
        Log.i("MATH_ONE_COUNT:", Integer.toString(mathOneCount));
        intent.putExtra("MATH_ONE_TOTAL", mathOneTotal);
        Log.i("MATH_ONE_TOTAL:", Integer.toString(mathOneTotal));
        intent.putExtra("MATH_TWO_COUNT", mathTwoCount);
        Log.i("MATH_TWO_COUNT:", Integer.toString(mathTwoCount));
        intent.putExtra("MATH_TWO_TOTAL", mathTwoTotal);
        Log.i("MATH_TWO_TOTAL:", Integer.toString(mathTwoTotal));

        startActivity(intent);
    }


    private void newGame(int c, int r) {
        cards = new int [COL_COUNT] [ROW_COUNT];

        TableRow tr = ((TableRow)findViewById(R.id.TableRow03));
        tr.removeAllViews();

        for (int y = 0; y < ROW_COUNT; y++) {
            mainTable.addView(createRow(y));
        }

        firstCard=null;
        loadCards();

        ((TextView)findViewById(R.id.tries)).setText("Number of Correct Pairs: "+countCorrectAnswers);
        ((TextView)findViewById(R.id.info)).setText("Find the matching pairs!");

    }

    private void loadImages() {
        images = new ArrayList<>();

        images.add(getResources().getDrawable(R.drawable.card1));
        images.add(getResources().getDrawable(R.drawable.card2));
        images.add(getResources().getDrawable(R.drawable.card3));
        images.add(getResources().getDrawable(R.drawable.card4));
        images.add(getResources().getDrawable(R.drawable.card5));
        images.add(getResources().getDrawable(R.drawable.card6));
        images.add(getResources().getDrawable(R.drawable.card7));
        images.add(getResources().getDrawable(R.drawable.card8));
        images.add(getResources().getDrawable(R.drawable.card9));
        images.add(getResources().getDrawable(R.drawable.card10));
        images.add(getResources().getDrawable(R.drawable.card11));
        images.add(getResources().getDrawable(R.drawable.card12));
        images.add(getResources().getDrawable(R.drawable.card13));
        images.add(getResources().getDrawable(R.drawable.card14));
        images.add(getResources().getDrawable(R.drawable.card15));
        images.add(getResources().getDrawable(R.drawable.card16));
        images.add(getResources().getDrawable(R.drawable.card17));
        images.add(getResources().getDrawable(R.drawable.card18));
        images.add(getResources().getDrawable(R.drawable.card19));
        images.add(getResources().getDrawable(R.drawable.card20));
        images.add(getResources().getDrawable(R.drawable.card21));
    }

    private void loadCards(){
        try{
            int size = ROW_COUNT*COL_COUNT;

            Log.i("loadCards()","size=" + size);

            ArrayList<Integer> list = new ArrayList<>();
            for(int i=0;i<size;i++){
                list.add(new Integer(i));
            }

            Random r = new Random();
            for(int i=size-1;i>=0;i--){
                int t=0;
                if(i>0){
                    t = r.nextInt(i);
                }
                t=list.remove(t).intValue();
                cards[i%COL_COUNT][i/COL_COUNT]=t%(size/2);

                Log.i("loadCards()", "card["+(i%COL_COUNT)+
                        "]["+(i/COL_COUNT)+"]=" + cards[i%COL_COUNT][i/COL_COUNT]);
            }
        }
        catch (Exception e) {
            Log.e("loadCards()", e+"");
        }

    }

    private TableRow createRow(int y){
        TableRow row = new TableRow(context);
        row.setHorizontalGravity(Gravity.CENTER);
        for (int x = 0; x < COL_COUNT; x++) {
            row.addView(createImageButton(x,y));
        }
        return row;
    }

    private View createImageButton(int x, int y){
        Button button = new Button(context);
        button.setBackgroundDrawable(backImage);
        button.setId(100*x+y);
        button.setOnClickListener(buttonListener);
        return button;
    }

    class ButtonListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            synchronized (lock) {
                if(firstCard!=null && secondCard != null){
                    return;
                }
                int id = v.getId();
                int x = id/100;
                int y = id%100;
                turnCard((Button)v,x,y);
            }
        }

        private void turnCard(Button button,int x, int y) {
            button.setBackgroundDrawable(images.get(cards[x][y]));
            if(firstCard==null){
                firstCard = new Card(button,x,y);
            }
            else{
                if(firstCard.x == x && firstCard.y == y){
                    return; //the user pressed the same card
                }

                secondCard = new Card(button,x,y);

                ((TextView)findViewById(R.id.tries)).setText("Number of Correct Pairs: "+countCorrectAnswers);

                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        try{
                            synchronized (lock) {
                                handler.sendEmptyMessage(0);
                            }
                        }
                        catch (Exception e) {
                            Log.e("E1", e.getMessage());
                        }
                    }
                };

                Timer t = new Timer(false);
                t.schedule(tt, 1300);
            }
        }
    }

    class UpdateCardsHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                checkCards();
            }
        }
        public void checkCards(){
            if(cards[secondCard.x][secondCard.y] == cards[firstCard.x][firstCard.y]){
                firstCard.button.setVisibility(View.INVISIBLE);
                secondCard.button.setVisibility(View.INVISIBLE);
                countCorrectAnswers++;
                ((TextView)findViewById(R.id.tries)).setText("Number of Correct Pairs: "+countCorrectAnswers);
                if (countCorrectAnswers==10){
                    //get current time
                    if (gameCount < 2) {
                        gameOneCount = countCorrectAnswers;
                        gameOneTime = 40000 - (Integer.parseInt(timer.getText().toString().replace(":", ""))*1000);
                        Log.i("MEM_ONE_TIME:", Integer.toString(gameOneTime));

                        try {
                            Toast.makeText(MemoryActivity.this, "Starting Second Session...", Toast.LENGTH_SHORT).show();
                            Thread.sleep(1000);
                            loadActivity();
                        } catch (InterruptedException i) {
                            Toast.makeText(MemoryActivity.this, "Something went wrong:InterruptedException", Toast.LENGTH_SHORT).show();
                        }
                    } else if (gameCount >= 2) {
                        gameTwoCount = countCorrectAnswers;
                        gameTwoTime = 40000 - (Integer.parseInt(timer.getText().toString().replace(":", ""))*1000);
                        Log.i("MEM_TWO_TIME:", Integer.toString(gameTwoTime));

                        try {
                            Thread.sleep(5000);
                            sendDataIntent();
                        } catch (InterruptedException i) {
                            Toast.makeText(MemoryActivity.this, "Something went wrong:InterruptedException", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            else {
                secondCard.button.setBackgroundDrawable(backImage);
                firstCard.button.setBackgroundDrawable(backImage);
            }
            firstCard=null;
            secondCard =null;
        }
    }
}
