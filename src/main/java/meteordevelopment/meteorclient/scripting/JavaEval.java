/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.scripting;

import bsh.Interpreter;

public class JavaEval {
    private static final Interpreter INTERPRETER = new Interpreter();

    private JavaEval() {
    }

    public static Object run(String code) {
        try {
            return INTERPRETER.eval(code);
        } catch (Exception e) {
            throw new RuntimeException("Java eval error: " + e.getMessage(), e);
        }
    }
}
