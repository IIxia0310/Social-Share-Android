package com.example.registerapplication;//package com.example.registerapplication;
//import android.opengl.EGL14;
//
//import javax.microedition.khronos.egl.EGL10;
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.egl.EGLContext;
//import javax.microedition.khronos.egl.EGLDisplay;
//import javax.microedition.khronos.egl.EGLSurface;
//
//public class EGLHelper {
//    private EGL10 egl;
//    private EGLDisplay display;
//    private EGLConfig config;
//    private EGLContext context;
//    private EGLSurface surface;
//
//    public EGLHelper() {
//        egl = (EGL10) javax.microedition.khronos.egl.EGLContext.getEGL();
//        display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
//
//        int[] version = new int[2];
//        egl.eglInitialize(display, version);
//
//        int[] configSpec = {
//                EGL10.EGL_RED_SIZE, 8,
//                EGL10.EGL_GREEN_SIZE, 8,
//                EGL10.EGL_BLUE_SIZE, 8,
//                EGL10.EGL_ALPHA_SIZE, 8,
//                EGL10.EGL_DEPTH_SIZE, 16,
//                EGL10.EGL_RENDERABLE_TYPE, 4, // EGL_OPENGL_ES2_BIT
//                EGL10.EGL_NONE
//        };
//
//        int[] numConfigs = new int[1];
//        EGLConfig[] configs = new EGLConfig[1];
//        egl.eglChooseConfig(display, configSpec, configs, 1, numConfigs);
//        config = configs[0];
//
//        int[] contextAttribs = {
//                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
//                EGL10.EGL_NONE
//        };
//        context = egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, contextAttribs);
//    }
//
//    public void createSurface(Object nativeWindow) {
//        int[] surfaceAttribs = {
//                EGL10.EGL_NONE
//        };
//        surface = egl.eglCreateWindowSurface(display, config, nativeWindow, surfaceAttribs);
//        egl.eglMakeCurrent(display, surface, surface, context);
//    }
//
//    public void swapBuffers() {
//        egl.eglSwapBuffers(display, surface);
//    }
//
//    public void release() {
//        egl.eglMakeCurrent(display, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
//        egl.eglDestroySurface(display, surface);
//        egl.eglDestroyContext(display, context);
//        egl.eglTerminate(display);
//    }
//}