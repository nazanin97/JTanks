import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * This class repesents the prizes
 * @version 1
 * @author Mana Atarod & Nazanin Akhtarian
 */
public class Prize {
    private BufferedImage prizeImage;
    private Point location;
    private int type;

    Prize(int x, int y, int type){
        this.type = type;
        try {
            switch (type){
                case 0:
                    prizeImage = ImageIO.read(new File(getClass().getResource("/RepairFood.png").toURI()));
                    break;
                case 1:
                    prizeImage = ImageIO.read(new File(getClass().getResource("/CannonFood.png").toURI()));
                    break;
                case 2:
                    prizeImage = ImageIO.read(new File(getClass().getResource("/MachineGunFood.png").toURI()));
                    break;
                case 3:
                    prizeImage = ImageIO.read(new File(getClass().getResource("/redstar.png").toURI()));
                    break;
            }
        }catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        location = new Point(x, y);
    }

    /**
     * get the prize's boundaries
     * @return bounds
     */
    public Rectangle getBounds(){
        return new Rectangle(location.x , location.y, prizeImage.getWidth() , prizeImage.getHeight());
    }

    /**
     * read prizes from file
     * @return prizes
     */
    public static ArrayList<Prize> readPrizes(int fileNum){
        ArrayList<Prize> prizes = new ArrayList<>();
        try {
            File file = new File(fileNum == 1 ? "prizes.txt" : "prizes2.txt");;
            BufferedReader lineReader = new BufferedReader(new FileReader(file));
            String lineText;
            while ((lineText = lineReader.readLine()) != null) {
                String nums[] = lineText.split(" ");
                Prize prize = new Prize(Integer.valueOf(nums[0]), Integer.valueOf(nums[1]), Integer.valueOf(nums[2]));
                prizes.add(prize);
            }
            lineReader.close();
        }catch (IOException e1) {
            e1.printStackTrace();
        }
        return prizes;
    }

    /**
     * get the prize
     */
    public void getPrize(){
        switch (type){
            case 0:
                if (GameFrame.getTank().health <= 20)
                    GameFrame.getTank().health += 10;
                else if (GameFrame.getTank().health < 30)
                    GameFrame.getTank().health = 30;
                break;
            case 1:
                if (GameFrame.getTank().cannonNum <= Tank.FULL_CANNON - 10)
                    GameFrame.getTank().cannonNum += 10;
                else if (GameFrame.getTank().cannonNum < Tank.FULL_CANNON)
                    GameFrame.getTank().cannonNum = Tank.FULL_CANNON;
                break;
            case 2:
                if (GameFrame.getTank().machineGunNum <= Tank.FULL_MACHINEGUN - 10)
                    GameFrame.getTank().machineGunNum += 10;
                else if (GameFrame.getTank().machineGunNum < Tank.FULL_MACHINEGUN)
                    GameFrame.getTank().machineGunNum = Tank.FULL_MACHINEGUN;
                break;
            case 3:
                GameFrame.getTank().getWeapon().setStrength(GameFrame.getTank().getWeapon().getStrength() + 2);
                GameFrame.getTank().getWeapon().setGap(GameFrame.getTank().getWeapon().getGap() - 1);
                break;
        }
        GameState.playSound("repair.wav");
        GameFrame.prizes.remove(this);
    }

    /**
     * get prize's image
     * @return image of prize
     */
    public BufferedImage getPrizeImage() {
        return prizeImage;
    }

    /**
     * get prize's location
     * @return location
     */
    public Point getLocation() {
        return location;
    }
}
