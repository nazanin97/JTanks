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
 * This class represents the plants on the frame
 * @version 1
 * @author Mana Atarod & Nazanin Akhtarian
 */
public class Plant {
    private BufferedImage plantImage;
    private Point location;

    public Plant(int x, int y) {
        location = new Point(x , y);
        try {
            plantImage = ImageIO.read(new File(getClass().getResource("/bush.png").toURI()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * read the plants from file
     * @return arraylist of plants
     */
    public static ArrayList<Plant> readPlants(int fileNum){
        ArrayList<Plant> plants = new ArrayList<>();
        try {
            File file = new File(fileNum == 1 ? "plants.txt": "plants2.txt");
            BufferedReader lineReader = new BufferedReader(new FileReader(file));
            String lineText;
            while ((lineText = lineReader.readLine()) != null) {
                String nums[] = lineText.split(" ");
                Plant plant = new Plant(Integer.valueOf(nums[0]), Integer.valueOf(nums[1]));
                plants.add(plant);
            }
            lineReader.close();
        }catch (IOException e1) {
            e1.printStackTrace();
        }
        return plants;
    }

    /**
     * get plant's image
     * @return image of plant
     */
    public BufferedImage getPlantImage() {
        return plantImage;
    }

    /**
     * get plant's location
     * @return location
     */
    public Point getLocation() {
        return location;
    }
}
