package jp.sample.java.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Apache Http Client(4.3.3)を使用し、Basic認証で対象URLにアクセスします。
 */
public class HttpClient {
    String hostname = "www.example.com";
    int port = 80;
    String login = "loginname";
    String pass = "password";
    String url = "www.example.com/hogehoge/";

    public void HttpConnect() {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpHost targetHost = new HttpHost(hostname, port, "http");
        AuthCache authCache = getAuthCache(targetHost); // ユーザー認証に必須
        CredentialsProvider credsProvider = getCredentialProvider(login, pass);

        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

        HttpGet httpget = new HttpGet(url);
        // リクエスト発行
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpget, context);

            // 結果をストリームに出力
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            response.getEntity().writeTo(outputStream);
            System.out.println(outputStream.toString());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Basic認証のキャッシュ情報を返却する
     * 
     * @param targetHost
     * @return
     */
    private AuthCache getAuthCache(HttpHost targetHost) {
        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());
        return authCache;
    }

    /**
     * 認証情報(プロバイダ)を返却する
     * 
     * @param username
     * @param password
     * @return
     */
    private CredentialsProvider getCredentialProvider(String username,
            String password) {

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(AuthScope.ANY),
                new UsernamePasswordCredentials(username, password));

        return credsProvider;
    }

    /**
     * メイン関数
     * 
     * @param args
     */
    public static void main(String[] args) {
        new HttpClient().HttpConnect();
    }
}
