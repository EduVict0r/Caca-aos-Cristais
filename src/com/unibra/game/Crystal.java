package com.unibra.game;
import java.awt.*;
public class Crystal {
    public static final int SIZE=22;
    private final int x,y;
    private final Color color;
    private boolean collected;
    public Crystal(int x,int y,Color color) { this.x=x; this.y=y; this.color=color; }
    public boolean isCollected() { return collected; }
    public void collect() { collected=true; }
    public Rectangle getBounds() { return new Rectangle(x,y,SIZE,SIZE); }
    public void draw(Graphics2D g) {
        if(collected) return;
        Polygon diamond=new Polygon(new int[]{x+11,x+22,x+11,x},new int[]{y,y+11,y+22,y+11},4);
        g.setColor(color); g.fillPolygon(diamond);
        g.setColor(Color.WHITE); g.drawPolygon(diamond);
    }
}
