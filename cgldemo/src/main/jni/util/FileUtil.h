#ifndef __FileUtil_H__
#define __FileUtil_H__

#include "android/asset_manager.h"//������Ҫ���ͷ�ļ�
#include "android/asset_manager_jni.h"
#include <string>

using namespace std;

class FileUtil {
public:
    static AAssetManager *aam;

    static void setAAssetManager(AAssetManager *aamIn);

    static string loadShaderStr(string fname);
};

#endif
