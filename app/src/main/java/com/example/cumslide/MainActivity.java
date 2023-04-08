package com.example.cumslide;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    int score;

    private int screenWidth;
    private int screenHeight;
    private ImageView mario;
    private float marioX;
    private float marioY;

    private int dpMario;

    private Bridge currentBridge;

    private Pair NextPosition;

    private CumSlideView cumSlideView;

    private ImageView planta1;

    private ImageView planta2;

    private ImageView planta3;

    private ImageView star;

    private boolean inBridge;

    Pair mStartPoint;
    Pair mEndPoint;
    public Boolean canDraw = false;

    private List<Integer> xPositions = new ArrayList<Integer>();

    private List<ImageView> plants = new ArrayList<ImageView>();

    private Handler handler = new Handler();
    private Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FrameLayout frameLayout = findViewById(R.id.frame_layout);

        //CumSlideView cumSlideView = new CumSlideView(this, null);
        //cumSlideView.setLayoutParams(new FrameLayout.LayoutParams(
        //        FrameLayout.LayoutParams.MATCH_PARENT,
        //        FrameLayout.LayoutParams.MATCH_PARENT));
        //frameLayout.addView(cumSlideView);

        mario = (ImageView)findViewById(R.id.mario);
        planta1 = (ImageView)findViewById(R.id.plant1);
        planta2 = (ImageView)findViewById(R.id.plant2);
        planta3 = (ImageView)findViewById(R.id.plant3);
        star = (ImageView)findViewById(R.id.star);
        score = 0;
        cumSlideView = (CumSlideView)findViewById(R.id.cumslide_view);
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // Create a list that consists of the plants and the star in a random order
        plants = new ArrayList<>();
        plants.add(planta1);
        plants.add(planta2);
        plants.add(planta3);
        plants.add(star);
        Collections.shuffle(plants);

        int columnWidth = screenWidth/4;
        float dpValue = 85; // the value in dp
        float pixelValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());

        int dpHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 115, getResources().getDisplayMetrics());

        int dpMenuBar = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int endpos = screenHeight - dpHeight - dpMenuBar;

        dpMario = (int)  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, getResources().getDisplayMetrics());

        for (int i = 0; i < 4; i++) {
            int x = i * columnWidth + columnWidth / 2 - (int)pixelValue/2;
            plants.get(i).setX(x);
            plants.get(i).setY(endpos);
            xPositions.add(i * columnWidth + columnWidth / 2 - dpMario/2);
        }

        TextView scoreText = (TextView)findViewById(R.id.score);
        scoreText.setText("Score: " + score);

        // Set the position of the score text
        scoreText.setX(140);
        scoreText.setY(100);
        // Set the textColor of the score text
        scoreText.setTextColor(Color.BLACK);
        // Bring the score text to the front
        scoreText.bringToFront();
        // Set the size of the score text
        scoreText.setTextSize(17);

        // Set the position of the mario
        marioX = new Random().nextInt(4) * columnWidth + columnWidth / 2 - dpMario/2;
        mario.setX(marioX);

        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        changePos();
                    }
                });
            }
        }, 0, 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        canDraw = false;
        System.out.println("Touch event");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartPoint = new Pair((int)event.getX(), (int)event.getY());
                mEndPoint = null;
                return true;
            case MotionEvent.ACTION_MOVE:
                mEndPoint = new Pair((int)event.getX(), (int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                mEndPoint = new Pair((int)event.getX(), (int)event.getY());
                canDraw = true;
                break;
        }
        if (canDraw) {
            List<Pair> screenPositions = CumSlideView.screenPositions;
            mStartPoint = PairSorter.getClosestPair(screenPositions, mStartPoint);
            mEndPoint = PairSorter.getClosestPair(screenPositions, mEndPoint);

            System.out.println("Inicio" + mStartPoint == null ? "null" : mStartPoint.toString());
            System.out.println("Fin" + mEndPoint == null ? "null" : mEndPoint.toString());

            if (mStartPoint != null && mEndPoint != null) {
                int columnGap = screenHeight / CumSlide.matrix.size();
                int rowGap = screenWidth / CumSlide.matrix.get(0).size();
                int xAxys1 = mStartPoint.getX() / rowGap;
                int yAxys1 = mStartPoint.getY() / columnGap;
                int xAxys2 = mEndPoint.getX() / rowGap;
                int yAxys2 = mEndPoint.getY() / columnGap;
                mStartPoint.setX(xAxys1);
                mStartPoint.setY(yAxys1);
                mEndPoint.setX(xAxys2);
                mEndPoint.setY(yAxys2);

                // Check if there is already a bridge in the position
                if (CumSlide.positions.values().stream().filter(e -> e.inPosition(xAxys1, yAxys1) || e.inPosition(xAxys2, yAxys2)).count() > 0) {
                    return true;
                } else {
                    // Draw bridge
                    Integer max = CumSlide.positions.keySet().stream().max(Comparator.comparingInt(e -> e)).get();

                    Bridge bridge = new Bridge(mStartPoint, mEndPoint);
                    CumSlide.positions.put(max + 1, bridge);

                    Paint linePaint = new Paint();
                    linePaint.setColor(Color.BLACK);
                    linePaint.setStrokeWidth(50);
                }
            }
            cumSlideView.invalidate();
        }
        return true;
    }

    public void changePos(){
        List<Bridge> bridges = CumSlide.positions.values().stream().collect(Collectors.toList());
        int columnGaps = screenHeight/CumSlide.matrix.size();
        int rowGaps = screenWidth/CumSlide.matrix.get(0).size();

        double lpos = marioY / columnGaps;

        if ((lpos-(int)lpos==0) && lpos!=0) {
            //System.out.println("Mario has reached a matrix point");
            // Mario is in a position of the matrix
            int pos = (int) lpos;
            if (xPositions.contains((int)marioX)){
                int xAxys = xPositions.indexOf((int)marioX);
                Boolean isBridge = NextPosition!=null && NextPosition.equals(new Pair(xAxys, pos));
                // Mario is in a column and not moving in a bridge
                if (!isBridge && bridges.stream().anyMatch(e -> e.inPosition(xAxys, pos))){
                    // Mario is in a bridge
                    inBridge = true;
                    currentBridge = bridges.stream().filter(e -> e.inPosition(xAxys, pos)).findFirst().get();
                    int xel = xPositions.indexOf((int)marioX);
                    Pair p = new Pair(xel, pos);
                    NextPosition = currentBridge.getNext(p);
                }
                else{
                    // Mario is not in a bridge
                    inBridge = false;
                }
                if (isBridge){
                    NextPosition = null;
                    inBridge = false;
                    // Mario arrived to the end of the bridge
                }
            }
        }
        if (inBridge) {
            // Check if the bridge is horizontal
            if (currentBridge.isHorizontal()) {
                if (!currentBridge.rightDirection(NextPosition)) {
                    marioX -= 1;
                    mario.setX(marioX);
                } else {
                    marioX += 1;
                    mario.setX(marioX);
                }
            } else {
                // The bridge is oblique
                if (currentBridge.goesUp(NextPosition)) {
                    System.out.println("Mario Going Down");
                    marioY += 1;
                    mario.setY(marioY - dpMario);
                } else {
                    // The bridge goes down
                    System.out.println("Mario Going Up");
                    marioY -= 1;
                    mario.setY(marioY - dpMario);
                }
                marioX = currentBridge.getScreenPos(rowGaps, columnGaps).getInterception(new Pair((int) marioX, (int) marioY), (NextPosition.getX() * rowGaps + rowGaps / 2 - dpMario/2), currentBridge.rightDirection(NextPosition));
                System.out.println("MarioX: " + marioX);
                mario.setX(marioX);
            }
        }
        else {
            marioY += 1;
            mario.setY(marioY-dpMario);
        }

        if (marioY >= screenHeight) {
            // Mario has reached the end of the screen
            int goalX = plants.indexOf(star);
            int xAxys = xPositions.indexOf((int)marioX);
            TextView scoreText = findViewById(R.id.score);
            if (goalX == xAxys){
                score += 1;
                scoreText.setText("Score: " + score);
            }
            else{
                score = 0;
                scoreText.setText("Score: " + score);
            }
            scoreText.bringToFront();
            marioY = 0;
            mario.setY(marioY-dpMario);
            int columnWidth = screenWidth/4;
            int r = new Random().nextInt(4);
            marioX = r * columnWidth + columnWidth / 2 - dpMario/2;
            mario.setX(marioX);
        }

    }

}
