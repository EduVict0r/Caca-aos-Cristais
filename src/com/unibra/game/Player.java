package com.unibra.game;
import java.awt.*;
public class Player {
    public static final int SIZE = 28, INITIAL_LIVES = 3, IMMUNITY_MS = 1000;
    public static final double SPEED = 4;
    private double x, y;
    private int lives = INITIAL_LIVES, immunity;
    public Player(int x, int y) { this.x=x; this.y=y; }
    public void update(int dx, int dy, Rectangle arena, int elapsed) {
        immunity = Math.max(0, immunity-elapsed);
        double length = Math.hypot(dx,dy);
        // Normalização evita velocidade maior nas diagonais.
        if(length>0) { x+=dx/length*SPEED; y+=dy/length*SPEED; }
        x=Math.max(arena.x,Math.min(x,arena.x+arena.width-SIZE));
        y=Math.max(arena.y,Math.min(y,arena.y+arena.height-SIZE));
    }
    public void takeDamage() {
        if(immunity==0 && lives>0) { lives--; immunity=IMMUNITY_MS; }
    }
    public int getLives() { return lives; }
    public Rectangle getBounds() { return new Rectangle((int)x,(int)y,SIZE,SIZE); }
    public void draw(Graphics2D g) {
        g.setColor(immunity>0 && immunity/100%2==0 ? Color.WHITE : Color.CYAN);
        g.fillRoundRect((int)x,(int)y,SIZE,SIZE,8,8);
        g.setColor(Color.BLACK);
        g.fillRect((int)x+6,(int)y+8,5,5);
        g.fillRect((int)x+18,(int)y+8,5,5);
        if(immunity>0) { g.setColor(Color.WHITE); g.drawOval((int)x-5,(int)y-5,SIZE+10,SIZE+10); }
    }
}
