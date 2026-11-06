package com.unibra.game;
import java.awt.*;
public class Enemy {
    public static final int SIZE=32,SPEED=3;
    private int x,y,direction=1;
    private final boolean horizontal;
    public Enemy(int x,int y,boolean horizontal) { this.x=x; this.y=y; this.horizontal=horizontal; }
    public void update(Rectangle arena) {
        int minimum=horizontal?arena.x:arena.y;
        int maximum=horizontal?arena.x+arena.width-SIZE:arena.y+arena.height-SIZE;
        int position=(horizontal?x:y)+direction*SPEED;
        if(position<=minimum || position>=maximum) {
            position=Math.max(minimum,Math.min(position,maximum)); direction*=-1;
        }
        if(horizontal) x=position; else y=position;
    }
    public Rectangle getBounds() { return new Rectangle(x,y,SIZE,SIZE); }
    public void draw(Graphics2D g) {
        g.setColor(new Color(255,80,110)); g.fillRoundRect(x,y,SIZE,SIZE,6,6);
        g.setColor(Color.WHITE);
        g.drawLine(x+8,y+8,x+24,y+24); g.drawLine(x+24,y+8,x+8,y+24);
    }
}
