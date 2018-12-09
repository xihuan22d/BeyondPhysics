package com.my.utils;

import android.content.Context;

import com.beyondphysics.ui.utils.SSLSocketTool;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.security.auth.x500.X500Principal;

/**
 * 与服务器有关的静态变量名统一使用小写,方便与服务器接口对应
 */
public class HttpConnectTool {
    public static final boolean USEHTTPS = true;
    public static final String COREHOSTNAME = "server.coolwallpaper.cn";
    public static final String FILEROOTURL = "http://" + HttpConnectTool.COREHOSTNAME + ":4126/coolWallpaper";

    //一页的数据个数
    public static final int pageSize = 25;
    //获取评论的一页数据个数
    public static final int commentPageSize = 12;
    //获取评论的子评论个数
    public static final int findChildCommentCount = 3;
    //开启加密
    public static final boolean openEncryption = false;
    //字段默认值
    public static final String defaultStringValue = "error";
    //比如email是个唯一的字段,但是注册时候是手机方式注册,可以先以unSetUniqueStringValue_1514624770629的方式维护其唯一性,再以待修改
    public static final String unSetUniqueStringValue = "unSetUniqueStringValue_";
    //需要被替换的分隔符
    public static final String featureNeedReplaceSplitSign = "，";
    //特征分隔符
    public static final String featureSplitSign = ",";


    // 反馈
    public static final String theApplication_feedback = "/theApplication_feedback";
    // 上传壁纸
    public static final String uploadActivity_uploadWallpaper = "/uploadActivity_uploadWallpaper";
    // 根据创建时间获得数据
    public static final String mainActivity_home_getWallpaperByCreateTime = "/mainActivity_home_getWallpaperByCreateTime";
    // 根据排序获得数据
    public static final String mainActivity_home_getWallpaperBySort = "/mainActivity_home_getWallpaperBySort";
    // 获得壁纸类型
    public static final String mainActivity_type_getWallpaperType = "/mainActivity_type_getWallpaperType";
    // 获得壁纸详情
    public static final String wallpaperDetailsActivity_getWallpaperDetails = "/wallpaperDetailsActivity_getWallpaperDetails";
    // 根据创建时间获取壁纸详情界面的评论
    public static final String wallpaperDetailsActivity_getWallpaperCommentByCreateTime = "/wallpaperDetailsActivity_getWallpaperCommentByCreateTime";
    // 根据排序获取壁纸详情界面的评论
    public static final String hotCommentActivity_getWallpaperCommentBySort = "/hotCommentActivity_getWallpaperCommentBySort";
    // 获得壁纸评论的子评论
    public static final String childCommentActivity_getWallpaperChildCommentByCreateTime = "/childCommentActivity_getWallpaperChildCommentByCreateTime";
    // 评论
    public static final String wallpaperDetailsActivity_commentWallpaper = "/wallpaperDetailsActivity_commentWallpaper";
    // 赞
    public static final String wallpaperDetailsActivity_doPraise = "/wallpaperDetailsActivity_doPraise";
    // 收藏
    public static final String wallpaperDetailsActivity_doCollection = "/wallpaperDetailsActivity_doCollection";
    // 关注
    public static final String wallpaperDetailsActivity_doFollow = "/wallpaperDetailsActivity_doFollow";


    public static SSLSocketFactory sslSocketFactory;

    public static String getServerRootUrl() {
        String result = "http://" + COREHOSTNAME + ":22001";
        if (USEHTTPS) {
            result = "https://" + COREHOSTNAME + ":22002";
        }
        return result;
    }

    public static void addReferer(Map<String, String> headerParams) {
        if (headerParams != null) {
            headerParams.put("Referer", "coolwallpaper.cn");
        }
    }

    public static synchronized SSLSocketFactory getInstanceSslSocketFactory(Context context) {
        if (sslSocketFactory == null) {
            List<SSLSocketTool.CrtItem> crtItems = new ArrayList<SSLSocketTool.CrtItem>();
            crtItems.add(new SSLSocketTool.CrtItem(SSLSocketTool.TYPE_ASSETS, "core.crt"));
            sslSocketFactory = SSLSocketTool.getSocketFactoryByKeyStore(crtItems, context);
        }
        return sslSocketFactory;
    }

    public static void setHostnameVerifier(HttpsURLConnection httpsURLConnection) {
        httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                try {
                    String peerHost = session.getPeerHost();
                    X509Certificate[] x509Certificates = (X509Certificate[]) session
                            .getPeerCertificates();
                    for (X509Certificate x509Certificate : x509Certificates) {
                        X500Principal x500Principal = x509Certificate
                                .getSubjectX500Principal();
                        String name = x500Principal.getName();
                        String[] split = name.split(",");
                        for (String str : split) {
                            if (str.startsWith("CN")) {
                                if (peerHost.equals(hostname) && str.contains(COREHOSTNAME)) {
                                    return true;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

}
