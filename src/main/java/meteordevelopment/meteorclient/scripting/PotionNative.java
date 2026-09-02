/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.scripting;

/**
 * Plain static, JNI-friendly entry points for native (C/C++) Potion addons to call into the game.
 * Every method here takes/returns only primitives and Strings (no boxed types, no functional
 * interfaces), so they're easy to call from C via JNIEnv->CallStatic*Method without extra
 * unboxing work. Values that need the player/world to exist (position, health, etc.) return a
 * zero/empty default when unavailable instead of null, since null objects are awkward from C.
 *
 * Class/method names below are exactly what appears in JNI lookups, e.g. from C:
 *   jclass cls = (*env)->FindClass(env, "meteordevelopment/meteorclient/scripting/PotionNative");
 *   jmethodID mid = (*env)->GetStaticMethodID(env, cls, "chat", "(Ljava/lang/String;)V");
 *
 * See the "native-addon" companion jar for how a shared library gets loaded in the first place
 * (via its "JNI_OnLoad" entry point) — {@link NativeRuntime}.
 */
public final class PotionNative {
    private static final PotionBridge BRIDGE = new PotionBridge();

    private PotionNative() {
    }

    public static void chat(String message) {
        BRIDGE.chat(message);
    }

    public static void error(String message) {
        BRIDGE.error(message);
    }

    public static void log(String message) {
        BRIDGE.log(message);
    }

    public static String ip() {
        return orEmpty(BRIDGE.ip());
    }

    public static boolean inSingleplayer() {
        return BRIDGE.inSingleplayer();
    }

    public static String username() {
        return orEmpty(BRIDGE.username());
    }

    public static float health() {
        Float v = BRIDGE.health();
        return v == null ? 0f : v;
    }

    public static float maxHealth() {
        Float v = BRIDGE.maxHealth();
        return v == null ? 0f : v;
    }

    public static int hunger() {
        Integer v = BRIDGE.hunger();
        return v == null ? 0 : v;
    }

    public static double x() {
        Double v = BRIDGE.x();
        return v == null ? 0d : v;
    }

    public static double y() {
        Double v = BRIDGE.y();
        return v == null ? 0d : v;
    }

    public static double z() {
        Double v = BRIDGE.z();
        return v == null ? 0d : v;
    }

    public static float yaw() {
        Float v = BRIDGE.yaw();
        return v == null ? 0f : v;
    }

    public static float pitch() {
        Float v = BRIDGE.pitch();
        return v == null ? 0f : v;
    }

    public static String gamemode() {
        return orEmpty(BRIDGE.gamemode());
    }

    public static String dimension() {
        return orEmpty(BRIDGE.dimension());
    }

    public static int ping() {
        Integer v = BRIDGE.ping();
        return v == null ? -1 : v;
    }

    public static int fps() {
        return BRIDGE.fps();
    }

    public static double tps() {
        return BRIDGE.tps();
    }

    public static boolean isSneaking() {
        Boolean v = BRIDGE.isSneaking();
        return v != null && v;
    }

    public static boolean isSprinting() {
        Boolean v = BRIDGE.isSprinting();
        return v != null && v;
    }

    public static boolean isOnGround() {
        Boolean v = BRIDGE.isOnGround();
        return v != null && v;
    }

    public static boolean isSubmergedInWater() {
        Boolean v = BRIDGE.isSubmergedInWater();
        return v != null && v;
    }

    public static int experienceLevel() {
        Integer v = BRIDGE.experienceLevel();
        return v == null ? 0 : v;
    }

    public static String heldItem() {
        return orEmpty(BRIDGE.heldItem());
    }

    public static long worldTime() {
        Long v = BRIDGE.worldTime();
        return v == null ? 0L : v;
    }

    public static boolean isRaining() {
        Boolean v = BRIDGE.isRaining();
        return v != null && v;
    }

    public static boolean isThundering() {
        Boolean v = BRIDGE.isThundering();
        return v != null && v;
    }

    public static String difficulty() {
        return orEmpty(BRIDGE.difficulty());
    }

    public static boolean isModuleActive(String name) {
        return BRIDGE.isModuleActive(name);
    }

    public static void toggleModule(String name) {
        BRIDGE.toggleModule(name);
    }

    public static void enableModule(String name) {
        BRIDGE.enableModule(name);
    }

    public static void disableModule(String name) {
        BRIDGE.disableModule(name);
    }

    private static String orEmpty(String s) {
        return s == null ? "" : s;
    }
}
