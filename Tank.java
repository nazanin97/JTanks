import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * This class represents the Tank
 * @version 1
 * @author Mana Atarod & Nazanin Akhtarian
 */
public class Tank {
    public static int FULL_CANNON;
    public static int FULL_MACHINEGUN;
    private BufferedImage tankImage;
    private static Weapon weapon;
    protected double imageAngleRad = 0;
    public int cannonNum;
    public int machineGunNum;
    public int health;
    private Point tankPosition;

    Tank() {
        weapon = new Cannon(1);
        FULL_CANNON = 50;
        FULL_MACHINEGUN = 100;
        cannonNum = FULL_CANNON;
        machineGunNum = FULL_MACHINEGUN;
        health = 30;
        tankPosition = new Point();
        try{
            tankImage = ImageIO.read(new File(getClass().getResource("/tank.png").toURI()));
        }
        catch(IOException | URISyntaxException e){
            e.printStackTrace();
        }
    }

    /**
     * Change tank's weapon
     */
    public void changeWeapon(){
        if (weapon instanceof Cannon)
            weapon = new MachineGun();
        else
            weapon = new Cannon(1);
    }

    /**
     * get tank's boundaries
     * @return bounds
     */
    public Rectangle getBounds(){
        return new Rectangle(tankPosition.x , tankPosition.y, tankImage.getWidth() , tankImage.getHeight());
    }

    /**
     * get tank's image
     * @return image
     */
    public BufferedImage getTankImage() {
        return tankImage;
    }

    /**
     * get tank's weapon
     * @return
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * set tank's position
     * @param x x position
     * @param y y position
     */
    public void setTankPosition(int x , int y) {
        tankPosition.setLocation(x,y);
    }

    /**
     * set tank's health
     * @param health health of tank
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * get tnak's position
     * @return tank's position
     */
    public Point getTankPosition() {
        return tankPosition;
    }
}
