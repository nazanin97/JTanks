import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Mine {

    private Point location;
    private BufferedImage mineImage;
    public boolean exploded;

    public Mine(int x, int y) {
        location = new Point(x , y);
        exploded = false;
        try {
            mineImage = ImageIO.read(new File(getClass().getResource("/mine.png").toURI()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the boundaries of the mine
     * @return boundaries
     */
    public Rectangle getBounds(){
        return new Rectangle(location.x , location.y, mineImage.getWidth() , mineImage.getHeight());
    }

    /**
     * get mine's location
     * @return location
     */
    public Point getLocation() {
        return location;
    }

    /**
     * set mine's location
     * @param location
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * get mine's image
     * @return image of mine
     */
    public BufferedImage getMineImage() {
        return mineImage;
    }
}
