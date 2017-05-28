package run.quest.twang.questrun.monsters;

/**
 * Created by Tim on 5/19/2017.
 */

public class Monster {
    private String mName;
    private double mStamina;//how long this monster can run for
    private int mHealth;
    private double mSpeed;//meters per second
    private double mPosition;
    private MonsterTypes mType;

    public Monster(MonsterTypes type, double position){
        mName = type.getName();
        mStamina = type.getStamina();
        mHealth = type.getHealth();
        mSpeed = type.getSpeed();
        mPosition = position;
        mType = type;
    }

    public void move(double howMuch){
        if (mStamina > 0 && mHealth > 0) {
            mPosition += howMuch;
        }
    }

    public void getTired(double howMuch){
        mStamina-=howMuch;
        if(mStamina < 0){
            mStamina = 0;
        }
    }

    public MonsterTypes getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getStamina() {
        return mStamina;
    }

    public void setStamina(int stamina) {
        mStamina = stamina;
    }

    public int getHealth() {
        return mHealth;
    }

    public void setHealth(int health) {
        mHealth = health;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(double speed) {
        mSpeed = speed;
    }

    public double getPosition() {
        return mPosition;
    }

    public void setPosition(double position) {
        mPosition = position;
    }
}
