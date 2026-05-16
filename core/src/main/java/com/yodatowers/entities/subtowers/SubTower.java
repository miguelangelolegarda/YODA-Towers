package com.yodatowers.entities.subtowers;

import com.badlogic.gdx.math.Vector2;
import com.yodatowers.entities.towers.Tower;
import com.yodatowers.entities.towers.YodaTower;

public abstract class SubTower extends Tower {
    protected final String name;
    protected final YodaTower owner;

    public SubTower(String name, YodaTower owner, float attackCooldown, float range) {
        super(attackCooldown, range);
        this.name = name;
        this.owner = owner;
    }

    @Override
    protected Vector2 getAttackOrigin() {
        return owner.getCenter();
    }

    public String getName() {
        return name;
    }
}
