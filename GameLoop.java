import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameLoop implements Runnable {

    /**
     * Frame Per Second.
     */
    private static final int FPS = 30;

    private GameFrame canvas;
    private GameState state;

    GameLoop(GameFrame frame) {
        canvas = frame;
    }

    /**
     * This must be called before the game loop starts.
     */
    public void init() {
        state = new GameState();
        canvas.addKeyListener(state.getKeyListener());
        canvas.addMouseListener(state.getMouseListener());
        canvas.addMouseMotionListener(state.getMouseMotionListener());
    }

    @Override
    public void run() {
        boolean gameOver = false;
        boolean won = false;
        //define server
        while (!gameOver && !won) {
            try {
                long start = System.currentTimeMillis();
                //
                state.update();
                canvas.render(state);
                gameOver = state.gameOver;
                won = state.won;
                //FOR MULTIPLAYER
//                if (state.nextLevel){//WASMULTIPLAYER
//                    try
//                    {
//                        BufferedImage image = new BufferedImage(GameFrame.GAME_WIDTH, GameFrame.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
//                        Graphics2D graphics2D = image.createGraphics();
//                        canvas.paint(graphics2D);
//                        ImageIO.write(image,"png", new File("gameScreen.png"));
//                    }
//                    catch(Exception exception)
//                    {
//                        exception.printStackTrace();
//                    }
//                }
                //
                long delay = (1000 / FPS) - (System.currentTimeMillis() - start);
                if (delay > 0)
                    Thread.sleep(delay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        canvas.render(state);
    }
}
