package com.example.cumslide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collections;
import java.util.List;

public class CumSlideView extends View {
    private CumSlide cumSlide;
    private Paint paint;
    private int blockWidth, blockHeight;
    private int screenWidth, screenHeight;
    private int numRows, numCols;
    private int playerRow, playerCol;
    private Jugador player;

    public CumSlideView(Context context, AttributeSet attr) {
        super(context, attr);
        cumSlide = new CumSlide(20);
        player = new Jugador();
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
        linePaint.setColor(Color.MAGENTA);
        linePaint.setStrokeWidth(5);

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

        for (Cuarteto c : cumSlide.positions.values()){
            Float startingX = Float.valueOf(c.getAx()*columnWidth+columnWidth/2);
            Float startingY = Float.valueOf(c.getAy()*rowWidth);
            Float stoppingX = Float.valueOf((c.getBx()*columnWidth+columnWidth/2));
            Float stoppingY = Float.valueOf(c.getBy()*rowWidth);
            canvas.drawLine(startingX, startingY, stoppingX, stoppingY, linePaint);
        }
    }

    public void movePlayer(int dx, int dy) {
        Integer xpos = player.getX();
        Integer ypos = player.getY();
        Integer pos = cumSlide.matrix.get(xpos).get(ypos);
        if (pos!=0 && !player.getMov()){
            player.setMovimiento(Boolean.TRUE);
            player.setCuarteto(cumSlide.positions.get(pos), new Pair(xpos, ypos));
        }
        else{
            if (xpos == player.posicionFinal.getX() && ypos == player.posicionFinal.getY() && player.getMov()==Boolean.TRUE) {
                player.setMovimiento(Boolean.FALSE);
            }
            else{
                player.setCuarteto(new Cuarteto(0,0,0,0), new Pair(0,0));
            }

        }
        player.setY(player.getY()+1);
    }
//
//    public void reset() {
//        cumSlide = new CumSlide(20);
//        numRows = cumSlide.matrix.size();
//        numCols = cumSlide.matrix.get(0).size();
//        playerRow = 0;
//        playerCol = 0;
//        invalidate();
//    }
}
