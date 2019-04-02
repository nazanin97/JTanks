import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * This class represents a shooting item
 * @version 1
 * @author Mana Atarod & Nazanin Akhtarian
 */
public class ShootingItem {

    public double shootingAngleRad;
    private Point shootingIconPosition;
    private boolean collided;
    private boolean enemyShootingItem;
    private int speed;
    private int strength;
    private BufferedImage shootingIcon;

    ShootingItem(Weapon weapon, boolean enemyShootingItem){
        collided = false;
        this.enemyShootingItem = enemyShootingItem;
        speed = weapon.getSpeed();
        strength = weapon.getStrength();
        shootingAngleRad = 0;
        shootingIconPosition  = new Point(weapon.imagePosition.x - 100, weapon.imagePosition.y - 100);
        shootingIcon = weapon.shootingIcon;

    }

    /**
     * shoot a bullet
     */
    public void shoot(){
        if (!collided) {
            shootingIconPosition.setLocation(shootingIconPosition.getX() + (Math.cos(shootingAngleRad) * speed), shootingIconPosition.getY() + (Math.sin(shootingAngleRad) * speed));
            if (shootingIconPosition.getX() >= GameFrame.GAME_WIDTH || shootingIconPosition.getY() >= GameFrame.GAME_HEIGHT || shootingIconPosition.getY() <= 0 || shootingIconPosition.getX() <= 0) {
                collided = true;
            }
            Rectangle shootingIconRectangle = new Rectangle(shootingIconPosition.x + 100, shootingIconPosition.y + 100, shootingIcon.getWidth(), shootingIcon.getHeight());
            for (Wall wall: GameFrame.walls){
                if (wall.getBounds().intersects(shootingIconRectangle)){
                    if (wall.isSoft()){
                        wall.setDamage(wall.getDamage() + strength);
                        wall.changeWallImage();
                    }
                    collided = true;
                    break;
                }
            }
            if (shootingIconRectangle.intersects(GameFrame.getTank().getBounds()) && enemyShootingItem){
                GameFrame.getTank().health -= strength*5;
                collided = true;
            }

            for (EnemyTank enemyTank: GameFrame.enemies){
                if (enemyTank.getBounds().intersects(shootingIconRectangle) && !enemyShootingItem){
                    int enemyHealth = enemyTank.getHealth() - strength * 5;
                    enemyTank.setHealth(enemyHealth);
                    if (enemyTank.getHealth() <= 0){
                        GameState.playSound("enemydestroyed.wav");
                        GameFrame.enemies.remove(enemyTank);
                    }
                    collided = true;
                    break;
                }
            }
        }
    }

    /**
     * paint the shooting item on the frame
     * @param g2d graphics
     * @param state state of objects
     * @param image image of shooting item
     * @param enemy is enemy or not
     */
    protected void paintShootingElement(Graphics2D g2d ,GameState state, BufferedImage image, boolean enemy){
        shootingIconPosition.setLocation(shootingIconPosition.x, shootingIconPosition.y - GameFrame.minYView);
        if (!collided){
            if (state.shot && !enemy){
                paintElement(g2d, image);
            }
            else if (enemy){
                paintElement(g2d, image);
            }
        }
    }

    /**
     * paint the shooting item
     * @param g2d graphics
     * @param image image of shooting item
     */
    private void paintElement(Graphics2D g2d, BufferedImage image){
        int cx = image.getWidth() / 2;
        int cy = image.getHeight() / 2;
        AffineTransform oldAT = g2d.getTransform();
        g2d.translate(cx + shootingIconPosition.x + 50, cy + shootingIconPosition.y + 50);
        g2d.rotate(shootingAngleRad);
        g2d.drawImage(shootingIcon, 0 , 0 - GameFrame.minYView, null);
        g2d.setTransform(oldAT);
    }
}
