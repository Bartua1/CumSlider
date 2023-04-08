package com.example.cumslide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CumSlideView extends View {
    private CumSlide cumSlide;

    public static List<Pair> screenPositions = new ArrayList<Pair>();
    private Paint paint;
    private int blockWidth, blockHeight;
    private int screenWidth, screenHeight;
    private int numRows, numCols;
    private int playerRow, playerCol;
    private MainActivity mainActivity;

    public CumSlideView(Context context, AttributeSet attr) {
        super(context, attr);
        mainActivity = (MainActivity) context;
        cumSlide = new CumSlide(20);
        numRows = cumSlide.matrix.size();
        numCols = cumSlide.matrix.get(0).size();
        paint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        blockWidth = screenWidth / numCols;
        blockHeight = screenHeight / numRows;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(List<Integer> a : cumSlide.matrix){
            System.out.println(a);
        }
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        // Define the paint for the lines
        Paint linePaint = new Paint();
        linePaint.setColor(Color.argb(255, 155, 184, 145));
        linePaint.setStrokeWidth(50);

        // Define the paint for the bridges
        Paint bridgePaint = new Paint();
        bridgePaint.setColor(Color.MAGENTA);
        bridgePaint.setStyle(Paint.Style.FILL);

        // Define the paint for the player
        Paint playerPaint = new Paint();
        playerPaint.setColor(Color.MAGENTA);
        playerPaint.setStyle(Paint.Style.FILL);

        int width = getWidth();
        int height = getHeight();

        // Calculate the width of each column
        int columnWidth = width / 4;
        int rowWidth = height / cumSlide.matrix.size();

        // Draw the vertical lines that separate the columns
        for (int i = 0; i < 4; i++) {
            int x = i * columnWidth + columnWidth / 2;
            canvas.drawLine(x, 0, x, height, linePaint);
        }


        // Draw the oblique line between the two cells
        int maxKey = Collections.max(cumSlide.positions.keySet());

        System.out.println("Numero de puentes " + cumSlide.positions.values().size());
        System.out.println("==============================");
        System.out.println(cumSlide.positions);
        System.out.print("==============================");
        for (Bridge c : cumSlide.positions.values()) {
            //System.out.println(c.getSourceX() + " - " + c.getSourceY());
            //System.out.println(c.getTargetX() + " - " + c.getTargetY());
            Float startingX = Float.valueOf(c.getSourceX()*columnWidth+columnWidth/2);
            Float startingY = Float.valueOf(c.getSourceY()*rowWidth);
            Float stoppingX = Float.valueOf((c.getTargetX()*columnWidth+columnWidth/2));
            Float stoppingY = Float.valueOf(c.getTargetY()*rowWidth);
            canvas.drawLine(startingX, startingY, stoppingX, stoppingY, linePaint);
        }
        int dpHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 115, getResources().getDisplayMetrics());

        for (int i = 0; i < cumSlide.matrix.size(); i++) {
            for (int j = 0; j < cumSlide.matrix.get(i).size(); j++) {
                screenPositions.add(new Pair(j*columnWidth+columnWidth/2, i*rowWidth - dpHeight));
            }
        }

    }
}

