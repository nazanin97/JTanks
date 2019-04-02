import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * This class represents the weapon in the game
 * @version 1
 * @author Mana Atarod & Nazanin Akhtarian
 */
public abstract class Weapon {
    protected int strength;
    protected int speed;
    protected int gap;
    protected BufferedImage shootingIcon1;
    protected BufferedImage shootingIcon2;
    public BufferedImage shootingIcon;
    protected BufferedImage weapon1Image;
    protected BufferedImage weapon1ImageEnemy;
    protected BufferedImage weapon2Image;
    public BufferedImage weaponImage;
    protected Point imagePosition = new Point(150,150);//change -> with tank according to the map
    protected Point shootingIconPosition = new Point(imagePosition.x , imagePosition.y);
    protected double imageAngleRad = 0;
    protected double shootingAngleRad = 0;
    private long latestShootingTime;
    private ArrayList<ShootingItem>shootingItems;

    Weapon() {

        shootingItems = new ArrayList<>();
        latestShootingTime = System.currentTimeMillis();
        try{
            weapon1Image = ImageIO.read(new File(getClass().getResource("/gun01.png").toURI()));
            weapon2Image = ImageIO.read(new File(getClass().getResource("/gun02.png").toURI()));
            weapon1ImageEnemy = ImageIO.read(new File(getClass().getResource("/SmallEnemyGun.png").toURI()));
            shootingIcon1 = ImageIO.read(new File(getClass().getResource("/HeavyBullet.png").toURI()));
            shootingIcon2 = ImageIO.read(new File(getClass().getResource("/LightBullet.png").toURI()));
        }
        catch(IOException | URISyntaxException e){
            e.printStackTrace();
        }
    }

    /**
     * shoot
     */
    public void shoot(){

        for (ShootingItem shootingItem:shootingItems) {
            shootingItem.shoot();
        }
    }


    /**
     * get weapon's strength
     * @return strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * get weapon's speed
     * @return speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Get the gap between shootings
     * @return gap
     */
    public int getGap() {
        return gap;
    }

    /**
     * set the gap between shootings
     * @param gap between shootings
     */
    public void setGap(int gap) {
        if (getGap() > 1)
            this.gap = gap;
    }

    /**
     * set the weapon's strength
     * @param strength strength of weapon
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * get all shooting items of the weapon
     * @return shooting items
     */
    public ArrayList<ShootingItem> getShootingItems() {
        return shootingItems;
    }

    /**
     * get the last time the weapon shot a bullet
     * @return last shooting time
     */
    public long getLatestShootingTime() {
        return latestShootingTime;
    }

    /**
     * set the last shooting time as the given parameter
     * @param latestShootingTime last time shot
     */
    public void setLatestShootingTime(long latestShootingTime) {
        this.latestShootingTime = latestShootingTime;
    }
}
