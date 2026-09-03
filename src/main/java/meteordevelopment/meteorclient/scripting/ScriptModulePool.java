/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.scripting;

import meteordevelopment.meteorclient.systems.modules.Category;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Modules.moduleInstances is a Map<Class<? extends Module>, Module> keyed by class, on the
 * assumption every module has its own dedicated class (true for all of Meteor's built-in
 * modules). Every Potion addon module is a plain ScriptModule though, so without this pool,
 * registering a second script module (from any addon, any language, at the same time)
 * silently overwrites the first one's map entry: it keeps ticking, but Modules.get(name)/
 * getAll()/favorites/per-module config would stop seeing it. Each slot below is a trivial
 * subclass that exists purely to give its instance a distinct Class key. 32 concurrent
 * script modules should be more than enough for real addon use; beyond that, slots wrap
 * around and the original collision this class exists to avoid can reappear.
 */
final class ScriptModulePool {
    private static final AtomicInteger NEXT = new AtomicInteger();

    private ScriptModulePool() {
    }

    static ScriptModule create(Category category, String name, Runnable func) {
        int slot = Math.floorMod(NEXT.getAndIncrement(), 32);

        return switch (slot) {
            case 0 -> new Slot0(category, name, func);
            case 1 -> new Slot1(category, name, func);
            case 2 -> new Slot2(category, name, func);
            case 3 -> new Slot3(category, name, func);
            case 4 -> new Slot4(category, name, func);
            case 5 -> new Slot5(category, name, func);
            case 6 -> new Slot6(category, name, func);
            case 7 -> new Slot7(category, name, func);
            case 8 -> new Slot8(category, name, func);
            case 9 -> new Slot9(category, name, func);
            case 10 -> new Slot10(category, name, func);
            case 11 -> new Slot11(category, name, func);
            case 12 -> new Slot12(category, name, func);
            case 13 -> new Slot13(category, name, func);
            case 14 -> new Slot14(category, name, func);
            case 15 -> new Slot15(category, name, func);
            case 16 -> new Slot16(category, name, func);
            case 17 -> new Slot17(category, name, func);
            case 18 -> new Slot18(category, name, func);
            case 19 -> new Slot19(category, name, func);
            case 20 -> new Slot20(category, name, func);
            case 21 -> new Slot21(category, name, func);
            case 22 -> new Slot22(category, name, func);
            case 23 -> new Slot23(category, name, func);
            case 24 -> new Slot24(category, name, func);
            case 25 -> new Slot25(category, name, func);
            case 26 -> new Slot26(category, name, func);
            case 27 -> new Slot27(category, name, func);
            case 28 -> new Slot28(category, name, func);
            case 29 -> new Slot29(category, name, func);
            case 30 -> new Slot30(category, name, func);
            default -> new Slot31(category, name, func);
        };
    }

    private static final class Slot0 extends ScriptModule {
        Slot0(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot1 extends ScriptModule {
        Slot1(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot2 extends ScriptModule {
        Slot2(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot3 extends ScriptModule {
        Slot3(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot4 extends ScriptModule {
        Slot4(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot5 extends ScriptModule {
        Slot5(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot6 extends ScriptModule {
        Slot6(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot7 extends ScriptModule {
        Slot7(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot8 extends ScriptModule {
        Slot8(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot9 extends ScriptModule {
        Slot9(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot10 extends ScriptModule {
        Slot10(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot11 extends ScriptModule {
        Slot11(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot12 extends ScriptModule {
        Slot12(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot13 extends ScriptModule {
        Slot13(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot14 extends ScriptModule {
        Slot14(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot15 extends ScriptModule {
        Slot15(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot16 extends ScriptModule {
        Slot16(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot17 extends ScriptModule {
        Slot17(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot18 extends ScriptModule {
        Slot18(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot19 extends ScriptModule {
        Slot19(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot20 extends ScriptModule {
        Slot20(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot21 extends ScriptModule {
        Slot21(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot22 extends ScriptModule {
        Slot22(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot23 extends ScriptModule {
        Slot23(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot24 extends ScriptModule {
        Slot24(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot25 extends ScriptModule {
        Slot25(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot26 extends ScriptModule {
        Slot26(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot27 extends ScriptModule {
        Slot27(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot28 extends ScriptModule {
        Slot28(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot29 extends ScriptModule {
        Slot29(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot30 extends ScriptModule {
        Slot30(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

    private static final class Slot31 extends ScriptModule {
        Slot31(Category category, String name, Runnable func) {
            super(category, name, func);
        }
    }

}
