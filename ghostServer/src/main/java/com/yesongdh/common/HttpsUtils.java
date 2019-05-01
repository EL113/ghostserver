package com.yesongdh.common;

import java.io.IOException;
import java.io.InterruptedIOException;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpsUtils {
    public static final int TIME_OUT = 10 * 1000;
    public static final Object SYNC_LOCK = new Object();
//    public static final int MAX_TOTAL_CONNECTIONS = 200;//连接池最大连接数
    public static final int MAX_PER_ROUTE_CONNECTIONS   = 200;//每个路由最大连接数
//    public static final int MAX_ROUTE = 10;//路由最大数量
    
    private static CloseableHttpClient httpClient = null;
    
    private static List<String> routes = new ArrayList<String>();

    static {
        routes.add("https://api.weixin.qq.com/sns/oauth2/access_token");
    }
    
    /**
     * 设置http连接池路由
     * @param routes
     */
    public static void setRoutes(List<String> routes) {
        if (routes == null) { return; }
        HttpsUtils.routes = routes;
    }
    
    /**
     * 获取HttpClient对象
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (SYNC_LOCK) {
                if (httpClient == null) {
                    int maxTotal = MAX_PER_ROUTE_CONNECTIONS * routes.size();
                    int maxPerRoute = MAX_PER_ROUTE_CONNECTIONS;
                    int maxRoute = routes.size();
                    httpClient = createHttpClient(maxTotal, maxPerRoute, maxRoute);
                }
            }
        }
        return httpClient;
    }

    /**
     * 创建HttpClient对象
     * @param maxTotal
     * @param maxPerRoute
     * @param maxRoute
     * @return
     */
    private static CloseableHttpClient createHttpClient(int maxTotal,
            int maxPerRoute, int maxRoute) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
            .getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
            .getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
            .<ConnectionSocketFactory> create().register("http", plainsf)
            .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        
        //自行设置, 将目标主机的最大连接数增加
        for (int i = 0; i < routes.size(); i++) {
            HttpHost httpHost = new HttpHost(routes.get(i), 443);
            cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);// 将目标主机的最大连接数增加
        }
        
        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                    int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext
                        .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler).build();

        return httpClient;
    }

    /**
     * POST请求URL获取内容
     * @param url
     * @param text
     * @return
     */
    public static String post(String url, String text) {
        return post(url, null, text);
    }
    
    /**
     * POST请求URL获取内容
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, Map<String, String> params) {
        return post(url, null, params);
    }
    
    /**
     * POST请求URL获取内容
     * @param url
     * @param headers
     * @param text
     * @return
     */
    public static String post(String url, Map<String, String> headers, String text) {
        
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "text/xml");
        httpPost.setEntity(new StringEntity(text, Consts.UTF_8));
        
        return doSend(httpPost, headers);
    }
    
    /**
     * POST请求URL获取内容
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String post(String url, Map<String, String> headers, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(url);
        
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        
        return doSend(httpPost, headers);
        
    }

    /**
     * GET请求URL获取内容
     * @param url
     * @return
     */
    public static String get(String url) {
        return get(url, null);
    }
    
    /**
     * GET请求URL获取内容
     * @param url
     * @param headers
     * @return
     */
    public static String get(String url, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);
        return doSend(httpGet, headers);
    }
    
    /**
     * 通用请求方式
     * @param httpRequestBase 请求实例
     * @param headers 请求头
     * @return
     */
    private static String doSend(HttpRequestBase httpRequestBase, Map<String, String> headers) {
        String result = "";
        
        httpRequestBase.setHeader("Accept-Charset", Consts.UTF_8.toString());
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(TIME_OUT)
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT).build();
        httpRequestBase.setConfig(requestConfig);
        
        if (headers != null) {
            for(String key: headers.keySet()) {
                httpRequestBase.setHeader(key, headers.get(key));
            }
        }
        
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(httpRequestBase,
                    HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, Consts.UTF_8);
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            System.out.println("请求错误, 错误信息: " + e.getMessage());
//            e.printStackTrace();
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
              System.out.println("响应流关闭错误, 错误信息: " + e.getMessage());
//              e.printStackTrace();
            }
        }
        return result;
    }

}
