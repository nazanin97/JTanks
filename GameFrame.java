/*** In The Name of Allah ***/

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Game's main frame
 * @version 1
 * @author Mana Atarod & Nazanin Akhtarian
 */
public class GameFrame extends JFrame {

    public static final int GAME_HEIGHT = 720;                  // 720p game resolution
    public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;  // wide aspect ratio

    public BufferedImage soil;
    public BufferedImage startUp;
    public BufferedImage mapChooser;
    public BufferedImage health;
    public BufferedImage gameOver;
    public BufferedImage won;
    public BufferedImage paused;
    public BufferedImage startingPoint;
    public BufferedImage finishPoint;

    public static int minYView;

    public static int startingPointY;
    public static int finishPointY;

    private BufferStrategy bufferStrategy;

    public static ArrayList<Wall> walls;

    public static ArrayList<EnemyTank> enemies;

    public static ArrayList<Prize> prizes;

    private ArrayList<Plant> plants;

    private static Tank tank;

    public static Mine mine;

    GameFrame(String title) {
        super(title);
        setResizable(false);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        walls = Wall.readWalls(1);
        enemies = EnemyTank.readEnemies();
        prizes = Prize.readPrizes(1);
        plants = Plant.readPlants(1);
        minYView = 0;
        startingPointY = GAME_HEIGHT - 100;
        finishPointY = -1700;
        mine = new Mine(950, 510);

        try {
            soil = ImageIO.read(new File(getClass().getResource("/Area.png").toURI()));
            startUp = ImageIO.read(new File(getClass().getResource("/Startup.png").toURI()));
            health = ImageIO.read(new File(getClass().getResource("/health.png").toURI()));
            gameOver = ImageIO.read(new File(getClass().getResource("/gameOver.png").toURI()));
            won = ImageIO.read(new File(getClass().getResource("/won.png").toURI()));
            paused = ImageIO.read(new File(getClass().getResource("/paused.png").toURI()));
            startingPoint = ImageIO.read(new File(getClass().getResource("/start.png").toURI()));
            finishPoint = ImageIO.read(new File(getClass().getResource("/finish.png").toURI()));
            mapChooser = ImageIO.read(new File(getClass().getResource("/map.png").toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        tank = new Tank();

    }

    /**
     * This must be called once after the JFrame is shown:
     *    frame.setVisible(true);
     * and before any rendering is started.
     */
    public void initBufferStrategy() {
        // Triple-buffering
        createBufferStrategy(3);
        bufferStrategy = getBufferStrategy();
    }

    /**
     * Game rendering with triple-buffering using BufferStrategy.
     */
    public void render(GameState state) {
        // Render single frame
        do {
            // The following loop ensures that the contents of the drawing buffer
            // are consistent in case the underlying surface was recreated
            do {
                // Get a new graphics context every time through the loop
                // to make sure the strategy is validated
                Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
                try {
                    doRendering(graphics, state);
                } finally {
                    // Dispose the graphics
                    graphics.dispose();
                }
                // Repeat the rendering if the drawing buffer contents were restored
            } while (bufferStrategy.contentsRestored());

            // Display the buffer
            bufferStrategy.show();
            // Tell the system to do the drawing NOW;
            // otherwise it can take a few extra ms and will feel jerky!
            Toolkit.getDefaultToolkit().sync();

            // Repeat the rendering if the drawing buffer was lost
        } while (bufferStrategy.contentsLost());
    }

    /**
     * Rendering all game elements based on the game state.
     */
    private void doRendering(Graphics2D g2d, GameState state) {

        if (state.started){
            if (!state.mapChosen){
                if (state.nextLevel){
                    walls = Wall.readWalls(2);
                    prizes = Prize.readPrizes(2);
                    plants = Plant.readPlants(2);
                }
            }
            state.mapChosen = true;
            // Draw background
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
            int horizontalSoil = GAME_WIDTH / soil.getWidth() + 1;
            int verticalSoil = GAME_HEIGHT / soil.getHeight() + 1;
            int startingPointHorizontal = 0;
            int startingPointVertical = 0;
            for (int i = 0; i < verticalSoil; i++) {
                for (int j = 0; j < horizontalSoil; j++) {
                    g2d.drawImage(soil, startingPointHorizontal,startingPointVertical,null);
                    startingPointHorizontal += soil.getWidth();
                }
                startingPointHorizontal = 0;
                startingPointVertical += soil.getHeight();

            }

            startingPointY -= minYView;
            finishPointY -= minYView;
            g2d.drawImage(startingPoint, 0, startingPointY, null);
            g2d.drawImage(finishPoint, GAME_WIDTH - 100, finishPointY, null);

            for(Prize prize:prizes){
                prize.getLocation().setLocation(prize.getLocation().x, prize.getLocation().y - minYView);
                g2d.drawImage(prize.getPrizeImage(), prize.getLocation().x, prize.getLocation().y, null);
            }

            for(Wall w:walls){
                w.getLocation().setLocation(w.getLocation().x, w.getLocation().y - minYView);
                g2d.drawImage(w.getWallIcon(), w.getLocation().x, w.getLocation().y, null);
            }

            mine.setLocation(new Point(mine.getLocation().x, mine.getLocation().y - minYView));
            if (!mine.exploded)
                g2d.drawImage(mine.getMineImage(), mine.getLocation().x, mine.getLocation().y, null);

            for(Plant p :plants){
                p.getLocation().setLocation(p.getLocation().x, p.getLocation().y - minYView);
                g2d.drawImage(p.getPlantImage(), p.getLocation().x, p.getLocation().y, null);
            }


            int startingHealthPoint = 600;
            for (int j = 0; j < tank.health / 10; j++) {
                g2d.drawImage(health, startingHealthPoint,70,null);
                startingHealthPoint += health.getWidth() + 10;
            }
            // Draw ball
            //g2d.setColor(Color.BLACK);
            //g2d.fillOval(state.locX, state.locY, state.diam, state.diam);
            int cx1 = tank.getTankImage().getWidth() / 2;
            int cy1 = tank.getTankImage().getHeight() / 2;
            AffineTransform oldAT1 = g2d.getTransform();
            g2d.translate(cx1 +state.locX, cy1 +state.locY);
            g2d.rotate(tank.imageAngleRad);
            g2d.translate(-cx1, -cy1);
            g2d.drawImage(tank.getTankImage(), 0 , 0 , null);
            g2d.setTransform(oldAT1);

            if (GameFrame.getTank().getWeapon().getShootingItems().size() == 0){
                state.shot = false;
            }
            for (ShootingItem s : GameFrame.getTank().getWeapon().getShootingItems()) {
                s.paintShootingElement(g2d , state, GameFrame.getTank().getTankImage(), false);
            }

            for (EnemyTank enemy: enemies){
                enemy.getTankPosition().setLocation(enemy.getTankPosition().x, enemy.getTankPosition().y - minYView);
                g2d.drawImage(enemy.getEnemyTankImage(), enemy.getTankPosition().x, enemy.getTankPosition().y, null);
                int cx = enemy.getEnemyTankImage().getWidth() / 2;
                int cy = enemy.getEnemyTankImage().getHeight() / 2;
                AffineTransform oldAT = g2d.getTransform();
                g2d.translate(cx +enemy.getTankPosition().x, cy +enemy.getTankPosition().y);
                enemy.getWeapon().imagePosition.setLocation(cx +enemy.getTankPosition().x, cy +enemy.getTankPosition().y);
                g2d.rotate(enemy.getWeapon().imageAngleRad);
                g2d.translate(-cx, -cy);
                g2d.drawImage(enemy.getWeapon().weaponImage, 0 , 0 , null);
                g2d.setTransform(oldAT);

                for (ShootingItem s : enemy.getWeapon().getShootingItems()) {
                    s.paintShootingElement(g2d , state, enemy.getEnemyTankImage(), true);
                }
            }


            int cx3 = tank.getTankImage().getWidth() / 2;
            int cy3 = tank.getTankImage().getHeight() / 2;
            AffineTransform oldAT3 = g2d.getTransform();
            g2d.translate(cx3 +state.locX, cy3 +state.locY);
            tank.getWeapon().imagePosition.setLocation(cx3 +state.locX, cy3 +state.locY);
            g2d.rotate(tank.getWeapon().imageAngleRad);
            g2d.translate(-cx3, -cy3);
            g2d.drawImage(tank.getWeapon().weaponImage, 0 , 0 , null);
            g2d.setTransform(oldAT3);

            BufferedImage cannonNumber = null;
            BufferedImage machineGunNumber = null;
            try {
                cannonNumber = ImageIO.read(new File(getClass().getResource("/NumberOfHeavyBullet.png").toURI()));
                machineGunNumber = ImageIO.read(new File(getClass().getResource("/NumberOfMachineGun.png").toURI()));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
            g2d.drawImage(cannonNumber, 30, 50, null);
            String num1 = tank.cannonNum + "";
            g2d.setColor(Color.YELLOW);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(20.0f));
            g2d.drawString(num1, 100, 90);

            g2d.drawImage(machineGunNumber, 35, 120, null);
            String num2 = tank.machineGunNum + "";
            g2d.setColor(Color.YELLOW);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(20.0f));
            g2d.drawString(num2, 100, 155);

        }

        else if (state.started2){
            g2d.drawImage(mapChooser, 0, 0, null);
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(420, state.chooseModeY1, 20, 20);
            enemies = EnemyTank.readEnemies();
        }

        else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
            g2d.drawImage(startUp , 170 , 100 , null);
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(315, state.chooseModeY, 20, 20);
        }

        if (state.gameOver){
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
            g2d.drawImage(gameOver , 70 , 100 , null);
            GameState.playSound("gameOver.wav");
        }

        if (state.won){
            g2d.drawImage(won , 200 , 150 , null);
            GameState.playSound("endOfGame.wav");
        }

        if (state.paused){
            g2d.drawImage(paused , 200 , 250 , null);
        }

    }

    /**
     * get the main tank
     * @return tank
     */
    public static Tank getTank() {
        return tank;
    }
}
