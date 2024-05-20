package com.chm0104;

import com.chm0104.DisabledSSL.DisableCertificateValidation;
import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableCasClient
public class App 
{
    public static void main( String[] args )
    {
        try {
            // 创建一个忽略 SSL 证书验证的设置
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            // 发起客户端请求，此时忽略 SSL 证书验证
            // 这里放置你实际的业务代码，例如在这里向服务端发起 HTTPS 请求
            SpringApplication.run(App.class, args);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

