import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.io.*;
import java.util.Iterator;

/**
 * This class represents a wall in the game
 * @version 1
 * @author Mana Atarod & Nazanin Akhtarian
 */
public class Wall {
    private BufferedImage softWall;
    private BufferedImage softWall1;
    private BufferedImage softWall2;
    private BufferedImage softWall3;
    private BufferedImage hardWall;
    private BufferedImage wallIcon;
    private Point location;
    private boolean soft;
    private int damage;

    /**
     * create a new wall
     * @param x x position
     * @param y y position
     * @param soft is a softwall or not
     */
    public Wall(int x , int y, boolean soft) {
        try {
            softWall = ImageIO.read(new File(getClass().getResource("/softWall.png").toURI()));
            softWall1 = ImageIO.read(new File(getClass().getResource("/softWall1.png").toURI()));
            softWall2 = ImageIO.read(new File(getClass().getResource("/softWall2.png").toURI()));
            softWall3 = ImageIO.read(new File(getClass().getResource("/softWall3.png").toURI()));
            hardWall = ImageIO.read(new File(getClass().getResource("/hardWall.png").toURI()));

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        damage = 0;
        this.soft = soft;
        location = new Point(x , y);
        if (this.soft){
            wallIcon = softWall;
        }
        else {
            wallIcon = hardWall;
        }

    }

    /**
     * read walls from file
     * @return walls
     */
    public static ArrayList<Wall> readWalls(int fileNum){
        ArrayList<Wall> myWalls = new ArrayList<>();
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(new File(fileNum == 1? "walls.txt" : "walls2.txt")));
            String lineText;
            while ((lineText = lineReader.readLine()) != null) {
                String nums[] = lineText.split(" ");
                boolean soft;
                if (nums[2].equals("1"))
                   soft  = true;
                else
                    soft  = false;

                Wall newWall = new Wall(Integer.valueOf(nums[0]), Integer.valueOf(nums[1]), soft);
                myWalls.add(newWall);
            }
            lineReader.close();
        }catch (IOException e1) {
            e1.printStackTrace();
        }
        return myWalls;
    }

    /**
     * get the boundaries of the wall
     * @return boundaries
     */
    public Rectangle getBounds(){
        return new Rectangle(location.x , location.y, wallIcon.getWidth() , wallIcon.getHeight());
    }

    /**
     * get wall's icon
     * @return wall icon
     */
    public BufferedImage getWallIcon() {
        return wallIcon;
    }

    /**
     * get wall's location
     * @return location
     */
    public Point getLocation() {
        return location;
    }

    /**
     * check if the wall is soft or not
     * @return true if soft false if not
     */
    public boolean isSoft() {
        return soft;
    }

    /**
     * get the damage to the wall
     * @return damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * set the damage done to the wall
     * @param damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * change the wall's image according to the damage
     */
    public void changeWallImage (){
        GameState.playSound("softwall.wav");
        if (damage == 1)
            wallIcon = softWall;
        else if (2 <= damage && damage < 4)
            wallIcon = softWall1;
        else if (4 <= damage && damage < 6)
            wallIcon = softWall2;
        else if (6 <= damage && damage < 8)
            wallIcon = softWall3;
        else {
            GameFrame.walls.remove(this);
        }

    }
}
