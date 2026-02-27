#include <jni.h>
#include <string>
#include <vector>
#include "secrets.h"

std::string performXor(const uint8_t* data, size_t len) {
    std::string out;
    out.reserve(len);
    size_t xor_len = strlen(XOR_KEY);

    for (size_t i = 0; i < len; i++) {
        out.push_back((char)(data[i] ^ XOR_KEY[i % xor_len]));
    }
    return out;
}

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_flowintent_network_util_NativeConfig_getGroqApiKey(JNIEnv* env, jobject thiz) {
    std::string decrypted = performXor(GROQ_API_KEY_ENC, GROQ_API_KEY_LEN);
    jstring result = env->NewStringUTF(decrypted.c_str());

    std::fill(decrypted.begin(), decrypted.end(), 0);

    return result;
}

JNIEXPORT jstring JNICALL
Java_com_flowintent_network_util_NativeConfig_getBaseUrl(JNIEnv* env, jobject thiz) {
    std::string decrypted = performXor(BASE_URL_ENC, BASE_URL_LEN);
    return env->NewStringUTF(decrypted.c_str());
}

}