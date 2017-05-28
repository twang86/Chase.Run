package run.quest.twang.questrun.monsters;

import run.quest.twang.questrun.R;

/**
 * Created by Tim on 5/21/2017.
 */

public enum MonsterTypes {
    ZOMBIE ("Zombie",
            //1800000
            1800000, 10, 1.5, .5, R.drawable.animation_zombie, R.raw.sound_zombie,
            "A standard zombie. These guys are super slow and can be outrun with a brisk walk. But they have lots of stamina and will chase you for a while"),
    FASTZOMBIE ("Fast Zombie",
            1200000, 10, 2.5, .5, R.drawable.animation_fast_zombie, R.raw.sound_zombie,
            "Slightly faster version of the zombie. He's a little faster for some reason"),
    INFECTED ("Infected",
            600000, 15, 3, .5, R.drawable.animation_infected, R.raw.sound_zombie,
            "The zombies for 28 days later. They are pretty fast and can chase you down pretty well. Be careful!"),
    CYBORG ("Cyborg",
            3600000, 150, 6, .6, R.drawable.animation_cyborg, R.raw.sound_cyborg,
            "Half man half machine, this guy is super fast and can chase you for quite a while!"),
    T_REX ("T-Rex",
            900000, 100, 10, 1, R.drawable.animation_t_rex, R.raw.sound_t_rex,
            "Holy crap it's a T-Rex! If you aren't a high level, you probably can't outrun this guy!");

    private String mName;
    private double mStamina;//how long this monster can run for in milliseconds
    private int mHealth;
    private double mSpeed;//meters per second
    private int mAnimation;
    private int mSound;
    private double mSize;
    private String mDescription;

    MonsterTypes(String name, double stamina, int health, double speed, double size, int animation, int sound, String description) {
        mName = name;
        mStamina = stamina;
        mHealth = health;
        mSpeed = speed;
        mSize = size;
        mAnimation = animation;
        mSound = sound;
        mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }

    public double getSize() {
        return mSize;
    }

    public int getSound() {
        return mSound;
    }

    public String getName() {
        return mName;
    }

    public double getStamina() {
        return mStamina;
    }

    public int getHealth() {
        return mHealth;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public int getAnimation() {
        return mAnimation;
    }
}
