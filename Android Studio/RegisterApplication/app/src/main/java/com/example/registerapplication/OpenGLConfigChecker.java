package com.example.registerapplication;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;



public class OpenGLConfigChecker {

    public static boolean checkEGLConfig(EGL10 egl, EGLDisplay display, int[] configAttribs) {
        int[] numConfigs = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        return egl.eglChooseConfig(display, configAttribs, configs, 1, numConfigs) && numConfigs[0] > 0;
    }

    public static void main(String[] args) {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] version = new int[2];
        egl.eglInitialize(display, version);

        int[] configAttribs = {
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 16,
                EGL10.EGL_RENDERABLE_TYPE, 4, // EGL_OPENGL_ES2_BIT
                EGL10.EGL_NONE
        };

        boolean isConfigSupported = checkEGLConfig(egl, display, configAttribs);
        if (isConfigSupported) {
            System.out.println("EGL 配置支持");
        } else {
            System.out.println("EGL 配置不支持");
        }

        egl.eglTerminate(display);
    }
}