import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * This class represents an enemy tank
 * @version 1
 * @author Mana Atarod & Nazanin Akhtarian
 */
public class EnemyTank {
    private BufferedImage enemyTankImage;
    private static int fullHealth = 0;
    private int health;
    private Point tankPosition;
    private Weapon weapon;
    private double imageAngleRad = 0;
    private boolean inPanel;
    private int enemyType; //1: mobile - 0:immobile

    /**
     * create a new enemy tank
     * @param x x position
     * @param y y position
     * @param type enemy type
     */
    public EnemyTank(int x, int y, int type) {
        enemyType = type;
        health = fullHealth;
        tankPosition = new Point(x , y);
        inPanel = false;
        try {
            switch (type){
                case 0:
                    enemyTankImage = ImageIO.read(new File(getClass().getResource("/BigEnemy.png").toURI()));
                    weapon = new Cannon(2);
                    break;
                case 1:
                    enemyTankImage = ImageIO.read(new File(getClass().getResource("/SmallEnemyBody.png").toURI()));
                    weapon = new Cannon(2);
                    break;
                case 2:
                    enemyTankImage = ImageIO.read(new File(getClass().getResource("/BigEnemy.png").toURI()));
                    weapon = new MachineGun();
                    break;
                case 3:
                    enemyTankImage = ImageIO.read(new File(getClass().getResource("/SmallEnemyBody.png").toURI()));
                    weapon = new MachineGun();
                    break;



            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }

    /**
     * read enemies from file
     * @return enemies
     */
    public static ArrayList<EnemyTank> readEnemies(){
        ArrayList<EnemyTank> enemies = new ArrayList<>();
        try {
            File file = new File("enemies" + GameState.mode + ".txt");
            fullHealth = (GameState.mode + 1) * 10;
            BufferedReader lineReader = new BufferedReader(new FileReader(file));
            String lineText;
            while ((lineText = lineReader.readLine()) != null) {
                String nums[] = lineText.split(" ");
                EnemyTank enemyTank = new EnemyTank(Integer.valueOf(nums[0]), Integer.valueOf(nums[1]), Integer.valueOf(nums[2]));
                enemies.add(enemyTank);
            }
            lineReader.close();
        }catch (IOException e1) {
            e1.printStackTrace();
        }
        return enemies;
    }

    /**
     * move enemy tank
     */
    public void move(){
        int pastX = tankPosition.x;
        int pastY = tankPosition.y;
        tankPosition.setLocation(tankPosition.getX() + (Math.cos(imageAngleRad) * 4), tankPosition.getY() + (Math.sin(imageAngleRad) * 4));
        Rectangle tankRectangle = new Rectangle(tankPosition.x, tankPosition.y, enemyTankImage.getWidth(), enemyTankImage.getHeight());
        for (Wall wall: GameFrame.walls){
            if (wall.getBounds().intersects(tankRectangle))
                tankPosition.setLocation(pastX, pastY);
        }
        if (tankRectangle.intersects(GameFrame.getTank().getBounds()))
            tankPosition.setLocation(pastX, pastY);
    }

    /**
     * get the tank's boundaries
     * @return boundaries
     */
    public Rectangle getBounds(){
        return new Rectangle(tankPosition.x , tankPosition.y, enemyTankImage.getWidth() , enemyTankImage.getHeight());
    }

    /**
     * get tank's image
     * @return tank image
     */
    public BufferedImage getEnemyTankImage() {
        return enemyTankImage;
    }

    /**
     * get enemy's health
     * @return health
     */
    public int getHealth() {
        return health;
    }

    /**
     * get enemy's position
     * @return position point
     */
    public Point getTankPosition() {
        return tankPosition;
    }

    /**
     * get enemy's weapon
     * @return weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * get the angle of the image
     * @return angleRad
     */
    public double getImageAngleRad() {
        return imageAngleRad;
    }

    /**
     * set image's angle
     * @param imageAngleRad angle
     */
    public void setImageAngleRad(double imageAngleRad) {
        this.imageAngleRad = imageAngleRad;
    }

    /**
     * check if the enemy is in view
     * @return in panel
     */
    public boolean isInPanel() {
        return inPanel;
    }

    /**
     * get enemy's type
     * @return type
     */
    public int getEnemyType() {
        return enemyType;
    }

    /**
     * set enemy's health
     * @param health enemy's health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * set whether the enemy is in panel or not
     * @param inPanel boolean in panel
     */
    public void setInPanel(boolean inPanel) {
        this.inPanel = inPanel;
    }
}
