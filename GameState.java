import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.*;

/**
 * This class determines the state of all of the game's components
 * @author Mana Atarod & Nazanin Akhtarian
 */
public class GameState {

    public boolean nextLevel;

    public boolean paused;

    public int locX, locY, diam;
    public boolean gameOver;
    public boolean won;
    public boolean shot;

    private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT;
    public boolean shooting;
    private int mouseX, mouseY;
    private KeyHandler keyHandler;
    private MouseHandler mouseHandler;

    public boolean started;
    public boolean started2;
    static int mode;
    public static boolean mapChosen;

    public int chooseModeY;
    public int chooseModeY1;

    public GameState() {

        paused = false;

        nextLevel = false;

        mapChosen = false;

        started = false;
        started2 = false;
        chooseModeY = 375;
        chooseModeY1 = 260;
        mode = 0;

        locX = 0;
        locY = GameFrame.GAME_HEIGHT - 100;
        diam = 90;
        gameOver = false;
        won = false;
        shooting = false;
        shot = false;

        //
        keyUP = false;
        keyDOWN = false;
        keyRIGHT = false;
        keyLEFT = false;
        //
        mouseX = 0;
        mouseY = 0;
        //
        keyHandler = new KeyHandler();
        mouseHandler = new MouseHandler();
    }

    /**
     * create a shooting item
     * @param weapon weapon shooting
     * @param emenyShootingItem whether the on shooting is an enemy or not
     */
    private void createShootingItem(Weapon weapon, boolean emenyShootingItem){
        GameState.playSound(weapon instanceof Cannon ? "cannon.wav" : "machinegun.wav");
        ShootingItem shootingItem = new ShootingItem(weapon, emenyShootingItem);
        shootingItem.shootingAngleRad = weapon.shootingAngleRad;
        weapon.getShootingItems().add(shootingItem);
        weapon.setLatestShootingTime(System.currentTimeMillis());
    }
    /**
     * The method which updates the game state.
     */
    public void update() {
        if (!paused){
            int pastX = locX;
            int pastY = locY;

            if (shooting) {
                if (GameFrame.getTank().getWeapon().getLatestShootingTime() + 50 * GameFrame.getTank().getWeapon().getGap() < System.currentTimeMillis() ) {

                    if(GameFrame.getTank().getWeapon() instanceof Cannon && GameFrame.getTank().cannonNum > 0){
                        GameFrame.getTank().cannonNum--;
                        createShootingItem(GameFrame.getTank().getWeapon(), false);
                    }
                    else if (GameFrame.getTank().getWeapon() instanceof MachineGun && GameFrame.getTank().machineGunNum > 0){
                        GameFrame.getTank().machineGunNum--;
                        createShootingItem(GameFrame.getTank().getWeapon(), false);
                    }
                }
            }

            else {
                GameFrame.getTank().getWeapon().shootingIconPosition.setLocation(locX , locY);
                GameFrame.getTank().getWeapon().shootingAngleRad = 0;
            }
            if (shot){
                GameFrame.getTank().getWeapon().shoot();
            }
            if (keyUP){
                if (!started && mode > 0 && !started2){
                        mode--;
                        chooseModeY -= 40;
                        playSound("select.wav");
                }
                else if (!started && started2 && nextLevel){
                    nextLevel = false;
                    chooseModeY1 -= 105;
                    playSound("select.wav");
                }
                else if (started)
                    locY -= 8;
            }
            if (keyDOWN){
                if (!started && mode < 2 && !started2){
                    mode++;
                    chooseModeY += 40;
                    playSound("select.wav");
                }
                else if (!started && started2 && !nextLevel){
                    nextLevel = true;
                    chooseModeY1 += 105;
                    playSound("select.wav");
                }
                else if (started)
                    locY += 8;
            }
            if (keyLEFT){
                if (started)
                    locX -= 8;
            }
            if (keyRIGHT){
                if (started)
                    locX += 8;
            }

            //check collisions
            Rectangle tankRectangle = new Rectangle(locX, locY, GameFrame.getTank().getTankImage().getWidth(), GameFrame.getTank().getTankImage().getHeight());
            for (Wall wall: GameFrame.walls){
                if (wall.getBounds().intersects(tankRectangle)){
                    locX = pastX;
                    locY = pastY;
                }
            }
            for (EnemyTank enemyTank: GameFrame.enemies){
                if (enemyTank.getBounds().intersects(tankRectangle)){
                    locX = pastX;
                    locY = pastY;
                }
            }
            for (Prize prize: GameFrame.prizes){
                if (prize.getBounds().intersects(tankRectangle)){
                    prize.getPrize();
                    break;
                }
            }
            locX = Math.max(locX, 0);
            locX = Math.min(locX, GameFrame.GAME_WIDTH - GameFrame.getTank().getTankImage().getWidth());
            locY = Math.max(locY, 0);
            locY = Math.min(locY, GameFrame.GAME_HEIGHT - GameFrame.getTank().getTankImage().getHeight());



            //rotate tank
            if (locY != pastY || locX != pastX){
                GameFrame.getTank().imageAngleRad = Math.atan2(locY - pastY , locX - pastX);
            }

            if (GameFrame.startingPointY >= GameFrame.getTank().getTankPosition().y + 50 && GameFrame.finishPointY < GameFrame.getTank().getTankPosition().y - 450){
                if (locY - pastY != 0){
                    GameFrame.minYView += (locY - pastY)/2;
                }
                else
                    GameFrame.minYView = 0;
                locY = pastY;
            }
            else
                GameFrame.minYView = 0;
//        else
//            locY += (pastY - locY) / 2;

            GameFrame.getTank().setTankPosition(locX, locY);
            if (GameFrame.mine.getBounds().intersects(new Rectangle(locX, locY, GameFrame.getTank().getTankImage().getWidth(), GameFrame.getTank().getTankImage().getHeight()))){
                GameFrame.getTank().health -= 10;
                GameFrame.mine.exploded = true;
            }

            if (started){
                for (EnemyTank enemy: GameFrame.enemies){
                    if (enemy.getTankPosition().y >= 0 && enemy.getTankPosition().y <= GameFrame.GAME_HEIGHT){
                        enemy.setInPanel(true);
                    }
                    enemy.setImageAngleRad(Math.atan2(locY - enemy.getTankPosition().y , locX - enemy.getTankPosition().x));
                    enemy.getWeapon().imageAngleRad = enemy.getImageAngleRad();
                    enemy.getWeapon().shootingAngleRad = enemy.getWeapon().imageAngleRad;
                    if (enemy.isInPanel()) {
                        if (enemy.getWeapon().getLatestShootingTime() + 70 * enemy.getWeapon().getGap() < System.currentTimeMillis() ) {
                            createShootingItem(enemy.getWeapon(), true);
                        }
                        if (enemy.getEnemyType() == 1 || enemy.getEnemyType() == 3){
                            enemy.move();
                        }
                        enemy.getWeapon().shoot();
                    }
                }
            }

            if (GameFrame.getTank().health <= 0){
                gameOver = true;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (GameFrame.getTank().getTankPosition().y <= GameFrame.finishPointY + 50 && GameFrame.getTank().getTankPosition().x >= GameFrame.GAME_WIDTH - 100)
                won = true;

        }
    }

    /**
     * get the key listener
     * @return key listener
     */
    public KeyListener getKeyListener() {
        return keyHandler;
    }

    /**
     * get the mouse listener
     * @return mouse listener
     */
    public MouseListener getMouseListener() {
        return mouseHandler;
    }

    /**
     * get the mouse motion listener
     * @return mouse motion listener
     */
    public MouseMotionListener getMouseMotionListener() {
        return mouseHandler;
    }


    /**
     * The keyboard handler.
     */
    class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    keyUP = true;
                    break;
                case KeyEvent.VK_DOWN:
                    keyDOWN = true;
                    break;
                case KeyEvent.VK_LEFT:
                    keyLEFT = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    keyRIGHT = true;
                    break;
                case KeyEvent.VK_ESCAPE:
                    gameOver = true;
                    break;
                case KeyEvent.VK_ENTER:
                    if (started2)
                        started = true;
                    else
                        started2 = true;
                    break;
                case KeyEvent.VK_H:
                    GameFrame.getTank().setHealth(30);
                    break;
                case KeyEvent.VK_C:
                    if (GameFrame.getTank().cannonNum <= 180)
                        GameFrame.getTank().cannonNum += 20;
                    break;
                case KeyEvent.VK_M:
                    if (GameFrame.getTank().machineGunNum <= 360)
                        GameFrame.getTank().machineGunNum += 40;
                    break;
                case KeyEvent.VK_P:
                    if (paused)
                        paused = false;
                    else
                        paused = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    keyUP = false;
                    break;
                case KeyEvent.VK_DOWN:
                    keyDOWN = false;
                    break;
                case KeyEvent.VK_LEFT:
                    keyLEFT = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    keyRIGHT = false;
                    break;
            }
        }
    }

    /**
     * The mouse handler.
     */
    class MouseHandler implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3){
                GameFrame.getTank().changeWeapon();
            }
            else
                GameFrame.getTank().getWeapon().setLatestShootingTime(System.currentTimeMillis());
        }

        @Override
        public void mousePressed(MouseEvent e) {

            if (e.getButton() != MouseEvent.BUTTON3){
                GameFrame.getTank().getWeapon().shootingAngleRad = GameFrame.getTank().getWeapon().imageAngleRad;
                shooting = true;
                shot = true;

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            shooting = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            GameFrame.getTank().getWeapon().imageAngleRad = Math.atan2(mouseY - locY , mouseX - locX);
        }
    }

    /**
     * play a sound
     * @param url sound name
     */
    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            Main.class.getResourceAsStream(url));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}

