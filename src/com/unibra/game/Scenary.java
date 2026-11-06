package com.unibra.game;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;

public class Scenary extends JPanel {
    public static final int WIDTH=800,HEIGHT=640,TICK_MS=20,POINTS_PER_CRYSTAL=10;
    private static final Rectangle ARENA=new Rectangle(20,100,760,520);
    private static final int[][] POSITIONS={{80,150},{240,150},{420,150},{600,150},{720,240},
        {100,330},{300,330},{480,480},{660,550},{160,550}};
    private final List<Crystal> crystals=new ArrayList<>();
    private final List<Enemy> enemies=new ArrayList<>();
    private final Set<Integer> keys=new HashSet<>();
    private final Timer timer;
    private Player player;
    private GameState state=GameState.INICIO;
    private int score,collected;

    public Scenary() {
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBackground(new Color(12,18,32));
        resetMatch();
        int[] controls={KeyEvent.VK_W,KeyEvent.VK_A,KeyEvent.VK_S,KeyEvent.VK_D,
            KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT,
            KeyEvent.VK_ENTER,KeyEvent.VK_P,KeyEvent.VK_R};
        for(int key:controls) { bindKey(key,false); bindKey(key,true); }
        // O reinício nunca cria outro Timer.
        timer=new Timer(TICK_MS,e->{updateGame(); repaint();});
        timer.start();
    }
    private void resetMatch() {
        player=new Player(45,110); score=0; collected=0; keys.clear();
        crystals.clear(); enemies.clear();
        Color[] colors={new Color(160,110,255),new Color(70,240,180),new Color(255,210,70)};
        for(int i=0;i<POSITIONS.length;i++)
            crystals.add(new Crystal(POSITIONS[i][0],POSITIONS[i][1],colors[i%colors.length]));
        enemies.add(new Enemy(350,270,true));
        enemies.add(new Enemy(550,420,false));
    }
    private void bindKey(int key,boolean released) {
        String name=key+":"+released;
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key,0,released),name);
        getActionMap().put(name,new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(released) keys.remove(key);
                else if(keys.add(key)) handleCommand(key);
            }
        });
    }
    private void handleCommand(int key) {
        if((key==KeyEvent.VK_ENTER && state==GameState.INICIO)
            || (key==KeyEvent.VK_R && (state==GameState.VITORIA || state==GameState.DERROTA))) {
            resetMatch(); state=GameState.JOGANDO;
        } else if(key==KeyEvent.VK_P) {
            if(state==GameState.JOGANDO) state=GameState.PAUSADO;
            else if(state==GameState.PAUSADO) state=GameState.JOGANDO;
        }
        repaint();
    }
    public void pauseOnFocusLoss() {
        keys.clear();
        if(state==GameState.JOGANDO) state=GameState.PAUSADO;
        repaint();
    }
    private int held(int a,int b) { return keys.contains(a)||keys.contains(b)?1:0; }
    private void updateGame() {
        // A pausa congela inclusive o tempo de invulnerabilidade.
        if(state!=GameState.JOGANDO) return;
        int dx=held(KeyEvent.VK_D,KeyEvent.VK_RIGHT)-held(KeyEvent.VK_A,KeyEvent.VK_LEFT);
        int dy=held(KeyEvent.VK_S,KeyEvent.VK_DOWN)-held(KeyEvent.VK_W,KeyEvent.VK_UP);
        player.update(dx,dy,ARENA,TICK_MS);
        for(Enemy enemy:enemies) {
            enemy.update(ARENA);
            if(player.getBounds().intersects(enemy.getBounds())) player.takeDamage();
        }
        for(Crystal crystal:crystals) {
            if(!crystal.isCollected() && player.getBounds().intersects(crystal.getBounds())) {
                crystal.collect(); collected++; score+=POINTS_PER_CRYSTAL;
            }
        }
        // Derrota tem prioridade sobre a coleta do último cristal.
        if(player.getLives()==0) state=GameState.DERROTA;
        else if(collected==crystals.size()) state=GameState.VITORIA;
    }
    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g=(Graphics2D)graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(25,38,56)); g.fill(ARENA);
        g.setColor(new Color(36,51,70));
        for(int x=20;x<780;x+=40) g.drawLine(x,100,x,620);
        for(int y=100;y<620;y+=40) g.drawLine(20,y,780,y);
        g.setColor(new Color(90,120,150)); g.draw(ARENA);
        g.setColor(Color.WHITE); g.setFont(new Font("SansSerif",Font.BOLD,22));
        g.drawString("CAÇA AOS CRISTAIS",24,33);
        g.setFont(new Font("SansSerif",Font.PLAIN,18));
        g.drawString("Pontos: "+score+"    Vidas: "+player.getLives()+" / "+Player.INITIAL_LIVES+
            "    Cristais: "+collected+" / "+crystals.size(),24,70);
        g.drawString("P: pausar",670,33);
        for(Crystal crystal:crystals) crystal.draw(g);
        for(Enemy enemy:enemies) enemy.draw(g);
        player.draw(g);
        if(state!=GameState.JOGANDO) drawOverlay(g);
        g.dispose();
    }
    private void drawOverlay(Graphics2D g) {
        g.setColor(new Color(8,12,24,225)); g.fillRoundRect(70,205,660,260,24,24);
        String title,detail,command;
        switch(state) {
            case INICIO:
                title="Caça aos Cristais"; detail="Colete 10 cristais e evite os dois guardiões.";
                command="Enter: começar"; break;
            case PAUSADO:
                title="Partida pausada"; detail="A arena está esperando por você.";
                command="P: retomar"; break;
            case VITORIA:
                title="Vitória!"; detail="Todos os cristais coletados! Pontos: "+score;
                command="R: jogar novamente"; break;
            default:
                title="Fim de jogo"; detail="Os guardiões venceram. Pontos: "+score;
                command="R: tentar novamente";
        }
        g.setColor(new Color(100,230,240)); g.setFont(new Font("SansSerif",Font.BOLD,36));
        centered(g,title,265);
        g.setColor(Color.WHITE); g.setFont(new Font("SansSerif",Font.PLAIN,20));
        centered(g,detail,315); centered(g,"Setas ou WASD: mover • P: pausar",355);
        g.setFont(new Font("SansSerif",Font.BOLD,22)); centered(g,command,415);
    }
    private void centered(Graphics2D g,String text,int y) {
        g.drawString(text,(WIDTH-g.getFontMetrics().stringWidth(text))/2,y);
    }
}
