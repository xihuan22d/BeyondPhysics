package com.beyondphysics.ui.utils;


import android.content.Context;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public class SSLSocketTool {
    public static final int TYPE_ASSETS = 1;
    public static final int TYPE_FILE = 2;

    /**
     * 指定单证书认证,保证https高度安全,无法被中间人攻击和安装的拦包软件拦截
     * 不设置SSLSocketFactory,HttpsURLConnection会使用系统的证书库进行证书验证,不过这种验证方式依然存在被中间人攻击的可能,而且如果服务器使用了自定义证书而不被操作系统信任的话就会导致请求的失败
     * app其实和浏览器不同,app很多时候其实是可以设置使用指定证书的验证的,服务器也可生成自己的私有证书,这也有利于提高安全性
     */
    public static SSLSocketFactory getSocketFactoryByKeyStore(int type, String fileName, Context context) {
        SSLSocketFactory sslSocketFactory = null;
        if (fileName == null) {
            return sslSocketFactory;
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            InputStream caInputStream = null;
            if (type == TYPE_ASSETS && context != null) {
                caInputStream = new BufferedInputStream(context.getAssets().open(fileName));
            } else {
                caInputStream = new BufferedInputStream(new FileInputStream(fileName));
            }
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            try {
                keyStore.setCertificateEntry("ca_0", certificateFactory.generateCertificate(caInputStream));
            } finally {
                caInputStream.close();
            }
            String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    /**
     * 指定多证书验证,保证https高度安全,无法被中间人攻击和安装的拦包软件拦截
     */
    public static SSLSocketFactory getSocketFactoryByKeyStore(int type, List<String> fileNames, Context context) {
        SSLSocketFactory sslSocketFactory = null;
        if (fileNames == null) {
            return sslSocketFactory;
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            for (int i = 0; i < fileNames.size(); i++) {
                String fileName = fileNames.get(i);
                if (fileName != null) {
                    try {
                        InputStream caInputStream = null;
                        if (type == TYPE_ASSETS && context != null) {
                            caInputStream = new BufferedInputStream(context.getAssets().open(fileName));
                        } else {
                            caInputStream = new BufferedInputStream(new FileInputStream(fileName));
                        }
                        try {
                            String alias = "ca_" + String.valueOf(i);
                            keyStore.setCertificateEntry(alias, certificateFactory.generateCertificate(caInputStream));
                        } finally {
                            caInputStream.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

}
