package com.ja3son.libdemo.util;


import android.content.res.Resources;
import android.opengl.GLES30;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ShaderManager {
    public static final String SHADER_SCRIPT_1 = "vertex.sh";
    public static final String SHADER_SCRIPT_2 = "frag.sh";
    public static final String SHADER_SCRIPT_3 = "vertexlight.sh";
    public static final String SHADER_SCRIPT_4 = "fraglight.sh";

    private ShaderManager() {
    }

    private static String[][] shaderName =
            {
                    {"vertex.sh", "frag.sh"},
                    {"vertexlight.sh", "fraglight.sh"},


            };

    private final static int shaderCount = shaderName.length;
    private final static int[] program = new int[shaderCount];
    private final static String[] mVertexShader = new String[shaderCount];
    private final static String[] mFragmentShader = new String[shaderCount];


    public static int getShaderProgram(int i) {
        return program[i];
    }

    public static void loadShaderScriptAndCompiled(Resources r) {
        loadCodeFromFile(r);
        compileShader();
    }

    public static void loadShaderScriptAndCompiled(Resources r, String[][] script) {
        shaderName = script;
        loadCodeFromFile(r);
        compileShader();
    }

    private static void loadCodeFromFile(Resources r) {
        for (int i = 0; i < shaderCount; i++) {
            mVertexShader[i] = loadFromAssetsFile(shaderName[i][0], r);
            mFragmentShader[i] = loadFromAssetsFile(shaderName[i][1], r);
        }
    }


    private static void compileShader() {
        for (int i = 0; i < shaderCount; i++) {
            program[i] = createProgram(mVertexShader[i], mFragmentShader[i]);
        }
    }

    public static int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        int pixelShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }
        int program = GLES30.glCreateProgram();
        if (program != 0) {
            GLES30.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            GLES30.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            GLES30.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES30.GL_TRUE) {
                Log.e("ES30_ERROR", "Could not link program: ");
                Log.e("ES30_ERROR", GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }


    private static int loadShader(int shaderType, String source) {
        int shader = GLES30.glCreateShader(shaderType);
        if (shader != 0) {
            GLES30.glShaderSource(shader, source);
            GLES30.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e("ES30_ERROR", "Could not compile shader " + shaderType + ":");
                Log.e("ES30_ERROR", GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }


    private static void checkGlError(String op) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e("ES30_ERROR", op + ": glError " + error);

            throw new RuntimeException(op + ": glError " + error);
        }
    }


    public static String loadFromAssetsFile(String fname, Resources r) {
        String result = null;
        try {
            InputStream in = r.getAssets().open(fname);
            int ch = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch = in.read()) != -1) {
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();
            baos.close();
            in.close();
            result = new String(buff, "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getInternalScript(String name) {
        try {
            InputStream in = ClassLoader.getSystemResourceAsStream("com/fuxp/openwater/resource/" + name);
            int ch = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch = in.read()) != -1) {
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();
            baos.close();
            in.close();
            String result = new String(buff, "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
