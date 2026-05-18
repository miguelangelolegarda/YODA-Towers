package com.yodatowers.entities.projectiles.visuals;

public class ProjectileStyle {
    private final float scale;
    private final float glowIntensity;
    private final float glowScale;
    private final boolean trailEnabled;
    private final boolean muzzleFlashEnabled;
    private final boolean impactEffectEnabled;

    public ProjectileStyle(
        float scale,
        float glowIntensity,
        float glowScale,
        boolean trailEnabled,
        boolean muzzleFlashEnabled,
        boolean impactEffectEnabled
    ) {
        this.scale = scale;
        this.glowIntensity = glowIntensity;
        this.glowScale = glowScale;
        this.trailEnabled = trailEnabled;
        this.muzzleFlashEnabled = muzzleFlashEnabled;
        this.impactEffectEnabled = impactEffectEnabled;
    }

    public static ProjectileStyle laser() {
        // TODO: Route rarity, elemental, crit, and status-effect overlays through this style object.
        return new ProjectileStyle(1f, 0.35f, 1.7f, false, false, false);
    }

    public ProjectileStyle withScale(float scale) {
        return new ProjectileStyle(scale, glowIntensity, glowScale, trailEnabled, muzzleFlashEnabled, impactEffectEnabled);
    }

    public ProjectileStyle withGlow(float intensity, float scale) {
        return new ProjectileStyle(this.scale, intensity, scale, trailEnabled, muzzleFlashEnabled, impactEffectEnabled);
    }

    public ProjectileStyle withTrail(boolean enabled) {
        return new ProjectileStyle(scale, glowIntensity, glowScale, enabled, muzzleFlashEnabled, impactEffectEnabled);
    }

    public ProjectileStyle withMuzzleFlash(boolean enabled) {
        return new ProjectileStyle(scale, glowIntensity, glowScale, trailEnabled, enabled, impactEffectEnabled);
    }

    public ProjectileStyle withImpactEffect(boolean enabled) {
        return new ProjectileStyle(scale, glowIntensity, glowScale, trailEnabled, muzzleFlashEnabled, enabled);
    }

    public float getScale() {
        return scale;
    }

    public float getGlowIntensity() {
        return glowIntensity;
    }

    public float getGlowScale() {
        return glowScale;
    }

    public boolean isTrailEnabled() {
        return trailEnabled;
    }

    public boolean isMuzzleFlashEnabled() {
        return muzzleFlashEnabled;
    }

    public boolean isImpactEffectEnabled() {
        return impactEffectEnabled;
    }
}
