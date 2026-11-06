import com.unibra.game.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class GameChecks {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    public static void main(String[] args) throws Exception {
        Rectangle arena = new Rectangle(20,100,760,520);
        Player player = new Player(40,120);
        player.update(1,1,arena,20);
        check(player.getBounds().x==42 && player.getBounds().y==122,"Diagonal");
        for(int i=0;i<1000;i++) player.update(-1,-1,arena,20);
        check(player.getBounds().x==20 && player.getBounds().y==100,"Limite superior");
        for(int i=0;i<1000;i++) player.update(1,1,arena,20);
        check(player.getBounds().x==752 && player.getBounds().y==592,"Limite inferior");
        player.takeDamage(); player.takeDamage();
        check(player.getLives()==2,"Proteção contra dano repetido");
        for(int i=0;i<49;i++) player.update(0,0,arena,20);
        player.takeDamage(); check(player.getLives()==2,"Proteção antes de um segundo");
        player.update(0,0,arena,20); player.takeDamage();
        check(player.getLives()==1,"Fim da proteção");
        Crystal crystal=new Crystal(100,100,Color.CYAN);
        crystal.collect(); crystal.collect(); check(crystal.isCollected(),"Coleta");
        for(boolean horizontal:new boolean[]{true,false}) {
            Enemy enemy=new Enemy(350,270,horizontal);
            boolean returned=false;
            int previous=horizontal?350:270;
            for(int i=0;i<1200;i++) {
                enemy.update(arena);
                check(arena.contains(enemy.getBounds()),"Limite da patrulha");
                int current=horizontal?enemy.getBounds().x:enemy.getBounds().y;
                if(current<previous) returned=true;
                previous=current;
            }
            check(returned,"Inversão da patrulha");
        }
        SwingUtilities.invokeAndWait(()->{
            Game game=new Game();
            game.setVisible(true);
            Scenary panel=(Scenary)game.getContentPane();
            check(panel.getWidth()==800 && panel.getHeight()==640,"Dimensões");
            BufferedImage image=new BufferedImage(800,640,BufferedImage.TYPE_INT_RGB);
            Graphics2D g=image.createGraphics();
            panel.paint(g); g.dispose();
            panel.getActionMap().get("10:false").actionPerformed(null);
            panel.pauseOnFocusLoss();
            game.dispose();
        });
        System.out.println("OK: movimento, limites, proteção, coleta, patrulhas e abertura/renderização Swing.");
        System.exit(0);
    }
}
