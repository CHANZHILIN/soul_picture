#include <jni.h>
#include <string>

#include "gif_lib.h"
#include <android/bitmap.h>

#include <android/log.h>

#define  LOG_TAG    "CHEN"
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  argb(a, r, g, b) ( ((a) & 0xff) << 24 ) | ( ((b) & 0xff) << 16 ) | ( ((g) & 0xff) << 8 ) | ((r) & 0xff)
typedef struct GifBean {
    // 当前播放的帧
    int current_frame;
    // 总的帧数
    int total_frame;
    // 每一帧的间隔时间 指针数组
    int *delays;
}GifBean;


extern "C"
JNIEXPORT jint JNICALL
Java_com_soul_1picture_main_gif_GifHandler_getWidth(JNIEnv *env, jobject thiz,
                                                    jlong long_gif_handler) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(long_gif_handler);
    return gifFileType->SWidth;
}
extern "C" {
void drawFrameDs(GifFileType *pType, GifBean *gifBean, AndroidBitmapInfo info, void *pixels) {
    SavedImage savedImage = pType->SavedImages[gifBean->current_frame];
    // 当前帧的图像信息
    GifImageDesc imageInfo = savedImage.ImageDesc;
    // 拿到图像数组的首地址
    int *px = (int *) pixels;
    // 图像颜色表
    ColorMapObject *colorMap = imageInfo.ColorMap;
    if (colorMap == nullptr) {
        colorMap = pType->SColorMap;
    }
    // y 方向偏移量
    px = (int *) ((char *) px + info.stride * imageInfo.Top);
    // 像素点的位置
    int pointPixel;
    GifByteType gifByteType;//压缩数据
    //    每一行的首地址
    int *line;
    for (int y = imageInfo.Top; y < imageInfo.Top + imageInfo.Height; ++y) {
        line = px;
        for (int x = imageInfo.Left; x < imageInfo.Left + imageInfo.Width; ++x) {
            pointPixel = (y - imageInfo.Top) * imageInfo.Width + (x - imageInfo.Left);
            // 通过 LWZ 压缩算法拿到当前数组的值
            gifByteType = savedImage.RasterBits[pointPixel];
            GifColorType gifColorType = colorMap->Colors[gifByteType];
            // 将 color type 转换成 argb 的值
            line[x] = argb(255, gifColorType.Red, gifColorType.Green, gifColorType.Blue);
        }
        // 更新到下一行
        px = (int *) ((char *) px + info.stride);
    }
}
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_soul_1picture_main_gif_GifHandler_getHeight(JNIEnv *env, jobject thiz,
                                                     jlong long_gif_handler) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(long_gif_handler);
    return gifFileType->SHeight;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_soul_1picture_main_gif_GifHandler_loadGif(JNIEnv *env, jobject thiz, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);
    int errorCode = 0;
    GifFileType *gifFileType = DGifOpenFileName(path, &errorCode);
    DGifSlurp(gifFileType);
    GifBean *gifBean = static_cast<GifBean *>(malloc(sizeof(GifBean)));
    memset(gifBean, 0, sizeof(GifBean));

    gifBean->delays = (int *) malloc(sizeof(int) * gifFileType->ImageCount);  //为数组开辟空间
    memset(gifBean->delays, 0, sizeof(int) * gifFileType->ImageCount);

    gifBean->current_frame = 0;
    gifBean->total_frame = gifFileType->ImageCount;

    // 图形拓展块
    ExtensionBlock *extensionBlock;

    for (int i = 0; i < gifFileType->ImageCount; ++i) {
        // 取出 GIF 中的每一帧
        SavedImage frame = gifFileType->SavedImages[i];
        // 拿到每一帧的拓展块
        for (int j = 0; j < frame.ExtensionBlockCount; ++j) {
            if (frame.ExtensionBlocks[j].Function == GRAPHICS_EXT_FUNC_CODE) {
                extensionBlock = &(frame.ExtensionBlocks[j]);
                break;
            }
            if (extensionBlock) {
                // 拿到图形控制拓展块和当前帧的延时时间
                // 因为它的 Bytes 是小端字节序 延时单位是 10 ms
                // Bytes[0] 是保留字段
                // Bytes[1] 是低 8 位
                // Bytes[2] 表示高 8 位
                int frame_delay = (extensionBlock->Bytes[2] << 8 | extensionBlock->Bytes[1]) * 10;//得到毫秒单位的延迟时间
//                LOGE("jni  %d   ",extensionBlock->Bytes[1]);
//                LOGE("jni  %d   ",extensionBlock->Bytes[2]);
                gifBean->delays[i] = frame_delay;
            }
        }
    }


    gifFileType->UserData = gifBean;

    env->ReleaseStringUTFChars(path_, path);
    return reinterpret_cast<jlong>(gifFileType);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_soul_1picture_main_gif_GifHandler_updateFrame(JNIEnv *env, jobject thiz, jobject bitmap,
                                                       jlong long_gif_handler) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(long_gif_handler);
    GifBean *gifBean =(GifBean *)(gifFileType->UserData);
    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env, bitmap, &info);
    void *pixels;
    AndroidBitmap_lockPixels(env, bitmap, &pixels);
    drawFrameDs(gifFileType, gifBean, info, pixels);
    gifBean->current_frame += 1;
    if (gifBean->current_frame > gifBean->total_frame - 1) {
        gifBean->current_frame = 0;
    }
    AndroidBitmap_unlockPixels(env, bitmap);
    return gifBean->delays[gifBean->current_frame];
}