package hinder.hinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
    private static int ROW_COUNT = 3;
    private static int COL_COUNT = 4;
    private Context context;
    private Drawable backImage;
    private int [] [] cards;
    private List<Drawable> images;
    private Card firstCard;
    private Card secondCard;
    private ButtonListener buttonListener;

    private static Object lock = new Object();

    int turns;
    private TableLayout mainTable;
    private UpdateCardsHandler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new UpdateCardsHandler();
        loadImages();
        setContentView(R.layout.activity_memory);
        backImage = getResources().getDrawable(R.drawable.icon);
       /*
       ((Button)findViewById(R.id.ButtonNew)).setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			newGame();
		}
	});*/

        buttonListener = new ButtonListener();
        mainTable = (TableLayout) findViewById(R.id.TableLayout03);
        context = mainTable.getContext();
        newGame(ROW_COUNT, COL_COUNT);
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

        turns=0;
        ((TextView)findViewById(R.id.tries)).setText("Tries: "+turns);
    }

    private void loadImages() {
        images = new ArrayList<Drawable>();

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
    }

    private void loadCards(){
        try{
            int size = ROW_COUNT*COL_COUNT;

            Log.i("loadCards()","size=" + size);

            ArrayList<Integer> list = new ArrayList<Integer>();
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

                turns++;
                ((TextView)findViewById(R.id.tries)).setText("Tries: "+turns);

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
/*
    ImageButton c1Button;
    ImageButton c2Button;
    ImageButton c3Button;
    ImageButton c4Button;
    ImageButton c5Button;
    ImageButton c6Button;
    ImageButton c7Button;
    ImageButton c8Button;
    ImageButton c9Button;
    ImageButton c10Button;
    ImageButton c11Button;
    ImageButton c12Button;
    TextView instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        instruction = (TextView) findViewById(R.id.instruction);

        c1Button = (ImageButton) findViewById(R.id.card1);
        c1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemoryActivity.this, ColorActivity.class);
                startActivity(intent);
            }
        });
        c2Button = (ImageButton) findViewById(R.id.card2);
        c2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c3Button = (ImageButton) findViewById(R.id.card3);
        c3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c4Button = (ImageButton) findViewById(R.id.card4);
        c4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c5Button = (ImageButton) findViewById(R.id.card5);
        c5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c6Button = (ImageButton) findViewById(R.id.card6);
        c6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c7Button = (ImageButton) findViewById(R.id.card7);
        c7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c8Button = (ImageButton) findViewById(R.id.card8);
        c8Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c9Button = (ImageButton) findViewById(R.id.card9);
        c9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c10Button = (ImageButton) findViewById(R.id.card10);
        c10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c11Button = (ImageButton) findViewById(R.id.card11);
        c11Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c12Button = (ImageButton) findViewById(R.id.card12);
        c12Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
   }
    */
