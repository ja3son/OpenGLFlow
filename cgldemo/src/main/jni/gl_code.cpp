#include <jni.h>
#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>
#include <cstdio>
#include <cstdlib>
#include <cmath>
#include "util/MatrixState.h"
#include "util/FileUtil.h"
#include "Triangle.h"
#include <android/log.h>

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "native-activity", __VA_ARGS__))

Triangle *t;

bool setupGraphics(int w, int h) {
    glViewport(0, 0, w, h);
    float ratio = (float) w / h;
    MatrixState::setProjectFrustum(-ratio, ratio, -1, 1, 1, 10);
    MatrixState::setCamera(0, 0, 3, 0, 0, 0, 0, 1, 0);
    MatrixState::setInitStack();
    glClearColor(0.5f, 0.5f, 0.5f, 1);
    t = new Triangle();
    return true;
}

void renderFrame() {
    glClear(GL_COLOR_BUFFER_BIT);
    t->drawSelf();
    MatrixState::rotate(1, 1, 0, 0);
}

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_ja3son_cgldemo_GL2JNILib_init
        (JNIEnv *, jclass, jint width, jint height) {
    setupGraphics(width, height);
}

JNIEXPORT void JNICALL Java_com_ja3son_cgldemo_GL2JNILib_step
        (JNIEnv *, jclass) {
    renderFrame();
}

JNIEXPORT void JNICALL Java_com_ja3son_cgldemo_GL2JNILib_nativeSetAssetManager
        (JNIEnv *env, jclass cls, jobject assetManager) {
    AAssetManager *aamIn = AAssetManager_fromJava(env, assetManager);
    FileUtil::setAAssetManager(aamIn);
}

#ifdef __cplusplus
}
#endif


