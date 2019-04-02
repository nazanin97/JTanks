/**
 * This class represents a cannon weapon
 * @version 1
 * @author Mana Atarod & Nazanin Akhtarian
 */
class Cannon extends Weapon {
    Cannon(int type) {
        gap = 15;
        weaponImage = (type == 1 ? weapon1Image : weapon1ImageEnemy);
        shootingIcon = shootingIcon1;
        speed = 6;
        strength = 2;
    }
}
