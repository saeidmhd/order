package com.mahak.order.webService;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.common.ServiceTools;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;


// TODO: Auto-generated Javadoc

/**
 * The Class RequestSender.
 */
public abstract class RequestSender implements Runnable {

    /**
     * The Constant _TIME_OUT.
     */
    private int _TIME_OUT = 25000;

    /**
     * The Interface ResponseReceiver.
     */
    public interface ResponseReceiver {

        /**
         * Got response.
         *
         * @param sender  the sender
         * @param result  the result
         * @param cookies the cookies
         */
        void gotResponse(Object sender, StringBuffer result, StringBuffer cookies);
    }

    /**
     * The Use proxy.
     */
    protected boolean UseProxy = false;// TODO

    /**
     * The proxy_ ip address.
     */
    protected String proxy_IpAddress = "IPAddress";

    /**
     * The proxy_ port number.
     */
    protected int proxy_PortNumber = 90;

    /**
     * The proxy_ login.
     */
    protected String proxy_Login = "username:password";

    /**
     * The Constant _POST.
     */
    public final static int _POST = 0;

    /**
     * The Constant _GET.
     */
    public final static int _GET = 1;

    /**
     * The Constant _WEBSERVICE.
     */
    public final static int _WEBSERVICE = 2;

    /**
     * The send mode.
     */
    protected int sendMode = _POST;

    /**
     * The send data.
     */
    protected String sendData;

    /**
     * The param.
     */
    protected String param;

    protected HashMap<String, Object> mapParam;

    /**
     * The receiver.
     */
    protected ResponseReceiver receiver;

    /**
     * The url.
     */
    protected final String url;

    /**
     * The soap action.
     */
    protected final String soapAction; // for webservice calls

    /**
     * The response is image.
     */
    protected final boolean responseIsImage;

    /** The response image. */
//	protected Image responseImage;

    /**
     * The encode string.
     */
    protected boolean encodeString;

    // TODO private ExirWebServiceWaitPage waitPage;

    /**
     * The cookie data.
     */
    private String cookieData;

    /**
     * The context.
     */
    private Context context;

    /**
     * The thread.
     */
    private Thread thread;

    private String methodName;
    private PropertyInfo propertyInfo;

    /**
     * Check proxy.
     */
    protected abstract void checkProxy();

    /**
     * Instantiates a new request sender.
     *
     * @param context         the context
     * @param url             the url
     * @param param           the param
     * @param receiver        the receiver
     * @param sendMode        the send mode
     * @param soapAction      the soap action
     * @param responseIsImage the response is image
     */
    public RequestSender(Context context, String url, String param, ResponseReceiver receiver, int sendMode, String soapAction, boolean responseIsImage) {
        this(context, url, param, receiver, sendMode, soapAction, responseIsImage, "");
    }

    /**
     * Instantiates a new request sender.
     *
     * @param context         the context
     * @param url             the url
     * @param param           the param
     * @param receiver        the receiver
     * @param sendMode        the send mode
     * @param soapAction      the soap action
     * @param responseIsImage the response is image
     * @param cookieData      the cookie data
     */
    public RequestSender(Context context, String url, String param, ResponseReceiver receiver, int sendMode, String soapAction, boolean responseIsImage, String cookieData) {
        this.context = context;
        this.url = url;
        this.receiver = receiver;
        this.param = param;
        this.sendMode = sendMode;
        this.soapAction = soapAction;
        this.responseIsImage = responseIsImage;
        this.cookieData = cookieData;
        restSendData();
    }

    public RequestSender(Context context, String url, String param, ResponseReceiver receiver, int sendMode, String soapAction, String methodName, boolean responseIsImage, String cookieData) {
        this.context = context;
        this.url = url;
        this.receiver = receiver;
        this.param = param;
        this.sendMode = sendMode;
        this.soapAction = soapAction;
        this.responseIsImage = responseIsImage;
        this.cookieData = cookieData;
        this.methodName = methodName;
        restSendData();
    }

    public RequestSender(Context context, String url, String param, ResponseReceiver receiver, int sendMode, String soapAction, boolean responseIsImage, String cookieData, int timeout) {
        this.context = context;
        this.url = url;
        this.receiver = receiver;
        this.param = param;
        this.sendMode = sendMode;
        this.soapAction = soapAction;
        this.responseIsImage = responseIsImage;
        this.cookieData = cookieData;
        _TIME_OUT = timeout;
        restSendData();
    }

    public RequestSender(Context context, String url, String param, ResponseReceiver receiver, int sendMode, String soapAction, String methodName, boolean responseIsImage, String cookieData, int timeout) {
        this.context = context;
        this.url = url;
        this.receiver = receiver;
        this.param = param;
        this.sendMode = sendMode;
        this.soapAction = soapAction;
        this.responseIsImage = responseIsImage;
        this.cookieData = cookieData;
        _TIME_OUT = timeout;
        this.methodName = methodName;
        this.propertyInfo = propertyInfo;
        restSendData();
    }

    public RequestSender(Context context, String url, HashMap<String, Object> param, ResponseReceiver receiver, int sendMode, String soapAction, String methodName, boolean responseIsImage, String cookieData, int timeout) {
        this.context = context;
        this.url = url;
        this.receiver = receiver;
        this.mapParam = param;
        this.sendMode = sendMode;
        this.soapAction = soapAction;
        this.responseIsImage = responseIsImage;
        this.cookieData = cookieData;
        _TIME_OUT = timeout;
        this.methodName = methodName;
        this.propertyInfo = propertyInfo;
        restSendData();
    }

    /**
     * Rest send data.
     */
    private void restSendData() {
        sendData = param;
    }

    /**
     * Gets the response image.
     *
     * @return the response image
     */
//	public Image getResponseImage() {
//		return responseImage;
//	}

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        StringBuffer[] sb = null;
        try {
            if (sendMode == _POST) {
                if (sendData.length() == 0)
                    sb = PerformGETRequest(url, sendData);
                else
                    sb = PerformPOSTRequest(url, sendData);
            } else if (sendMode == _WEBSERVICE) {
                if (sendData != null && !sendData.equals(""))
                    sb = PerformWebServiceRequest_Soap(url, sendData);
                else
                    sb = PerformWebServiceRequest_Soap(url, mapParam);
            } else {
                sb = PerformGETRequest(url, sendData);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
            // ExirDebugger.println("er:" + e.getMessage());
        }
        if (thread == null)
            return;
        if (sb != null) {
            if (sb[0] != null) {
                String result = ServiceTools.correctFarsiText(sb[0].toString());
                sb[0] = new StringBuffer(result);
            }
            receiver.gotResponse(RequestSender.this, sb[0], sb[1]);
        } else {
            receiver.gotResponse(RequestSender.this, null, null);
        }

    }

    /**
     * Start.
     */
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stop.
     */
    public void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    /**
     * Perform get request.
     *
     * @param url      the url
     * @param sendData the send data
     * @return the string buffer[]
     */
    private StringBuffer[] PerformGETRequest(String url, String sendData) {
        HttpURLConnection http = null;
        InputStream is = null;
        StringBuffer result = new StringBuffer();

        // proxy
        Proxy proxy = null;
        if (UseProxy) {
            InetSocketAddress sa = new InetSocketAddress(proxy_IpAddress, proxy_PortNumber);
            proxy = new Proxy(Proxy.Type.HTTP, sa);
        }
        StringBuffer sbCooki = new StringBuffer();

        try {
            if (UseProxy) {
                http = (HttpURLConnection) (new URL(url + (sendData.length() > 0 ? ("?" + sendData) : ""))).openConnection(proxy);
            } else {
                http = (HttpURLConnection) (new URL(url + (sendData.length() > 0 ? ("?" + sendData) : ""))).openConnection();
            }
            http.setRequestMethod("GET");

            // proxy
            if (UseProxy) {
                String encodedLogin = Base64.encodeToString(proxy_Login.getBytes(), Base64.URL_SAFE);
                http.setRequestProperty("Proxy-Authorization", "Basic " + encodedLogin);
            }

            // http.setRequestProperty("User-Agent", "HttpMidlet/0.2");
            http.setReadTimeout(_TIME_OUT);
            http.setConnectTimeout(_TIME_OUT);
            int len = 0;

            if (cookieData != null && cookieData.length() > 0)
                http.setRequestProperty("Cookie", cookieData);

            if (responseIsImage) {
                len = http.getContentLength();
                if (len <= 0) {
                    return createImage(http);
                }
                is = new BufferedInputStream(http.getInputStream());
                DataInputStream dis = new DataInputStream(is);
                byte[] bts = new byte[len];
                dis.readFully(bts);
//				responseImage = Image.createImage(context, bts, 0, len);

                // ****************get Cookie ************
                addCookies(http, sbCooki);
                // ****************get Cookie ************

                if (http != null)
                    http.disconnect();
                return new StringBuffer[]{new StringBuffer("loaded"), sbCooki};
            } else {
                if (encodeString)
                    http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                else
                    http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                http.connect();
                is = new BufferedInputStream(http.getInputStream());
            }

            // ****************get Cookie ************
            addCookies(http, sbCooki);
            // ****************get Cookie ************

            result = readConnectionStream(is, len);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            result = null;
            e.printStackTrace();
        } finally {
            if (http != null)
                http.disconnect();
        }
        return new StringBuffer[]{result, sbCooki};
    }

    /*
     * private StringBuffer[] PerformPOSTRequestJson(String url, String
     * sendData) { HttpClient httpclient = new DefaultHttpClient(); HttpPost
     * httppost = new HttpPost(url); httpclient.setReadTimeout(_TIME_OUT);
     * httpclient.setConnectTimeout(_TIME_OUT);
     *
     * JSONObject json = new JSONObject(); try { json.put("username", "Hello");
     * json.put("password", "World"); StringEntity se = new
     * StringEntity(json.toString()); se.setContentEncoding(new
     * BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
     * httppost.setEntity(se); HttpResponse response =
     * httpclient.execute(httppost); Checking response
     *
     * if(response!=null) InputStream in = response.getEntity().getContent();
     * //Get the data in the entity
     *
     * } catch (Exception e) {
   FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().recordException(e); e.printStackTrace(); }
     *
     * }
     */

    /**
     * Perform post request.
     *
     * @param url      the url
     * @param sendData the send data
     * @return the string buffer[]
     * @throws Exception the exception
     */
    private StringBuffer[] PerformPOSTRequest(String url, String sendData) throws Exception {
        StringBuffer response = null;
        InputStream istrm = null;
        HttpURLConnection http = null;
        Proxy proxy = null;
        if (UseProxy) {
            InetSocketAddress sa = new InetSocketAddress(proxy_IpAddress, proxy_PortNumber);
            proxy = new Proxy(Proxy.Type.HTTP, sa);
            http = (HttpURLConnection) new URL(url).openConnection(proxy);
        } else
            http = (HttpURLConnection) new URL(url).openConnection();

        http.setRequestMethod("POST");
        // proxy
        if (UseProxy) {

            String encodedLogin = Base64.encodeToString(proxy_Login.getBytes(), Base64.URL_SAFE);
            http.setRequestProperty("Proxy-Authorization", "Basic " + encodedLogin);
        }

        if (cookieData != null && cookieData.length() > 0)
            http.setRequestProperty("Cookie", cookieData);
        http.setRequestProperty("User-Agent", "HttpMidlet/0.2");
        http.setReadTimeout(_TIME_OUT);
        http.setConnectTimeout(_TIME_OUT);
        StringBuffer sbCooki = new StringBuffer();

        if (responseIsImage) {
            http.setRequestProperty("VerifyTransfer", "image/png");
            int len = http.getContentLength();
            if (len <= 0) {
                return createImage(http);
            }
            InputStream is = http.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            byte[] bts = new byte[len];
            dis.readFully(bts);
//			responseImage = Image.createImage(context, bts, 0, len);

            // ****************get Cookie ************
            addCookies(http, sbCooki);
            // ****************get Cookie ************

            if (http != null)
                http.disconnect();
            return new StringBuffer[]{new StringBuffer("loaded"), sbCooki};
        } else {
            if (encodeString)
                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            else
                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        }
        byte[] buf = sendData.getBytes("UTF-8");
        http.setDoInput(true);
        http.setDoOutput(true);

        if (sendData.length() > 0) {
            http.setRequestProperty("Content-length", "" + buf.length);
            OutputStream out = http.getOutputStream();
            out.write(buf);
            out.flush();
        }

        int rCode = http.getResponseCode();
        if (rCode == HttpURLConnection.HTTP_OK) {
            // ****************get Cookie ************
            addCookies(http, sbCooki);
            // ****************get Cookie ************

            int len = http.getContentLength();
            istrm = http.getInputStream();
            if (istrm == null) {
                // ExirDebugger.println("Cannot open HTTP InputStream, aborting");
            }
            if (len != -1) {
                if (encodeString) {
                    DataInputStream dis = new DataInputStream(istrm);
                    int c1 = dis.readUnsignedByte();
                    int c2 = dis.readUnsignedByte();
                    int c3 = dis.readUnsignedByte();
                    int c4 = dis.readUnsignedByte();
                    c1 = c2 * 256 + c1;
                    c2 = c4 * 256 + c3;
                    int c = c2 * 65536 + c1;
                    StringBuffer buffer = new StringBuffer();
                    boolean b = false;
                    for (int i = 0; i < c; i++) {
                        c1 = dis.readUnsignedByte();
                        c2 = dis.readUnsignedByte();
                        if (b)
                            c1++;
                        else
                            c1--;
                        b = !b;
                        buffer.append((char) ((c2 * 256 + c1)));
                        setProgrss(i, c);
                    }
                    response = buffer;
                } else {
                    Reader r = new InputStreamReader(istrm, "UTF-8");
                    StringBuffer result = new StringBuffer();
                    int c1 = 0;
                    while (true) {
                        c1 = r.read();
                        if (c1 == -1) {
                            break;
                        }
                        result.append((char) c1);
                    }
                    response = result;
                }
            } else {
                Reader r = new InputStreamReader(istrm, "UTF-8");
                final char[] buffer = new char[404800];
                StringBuffer result = new StringBuffer();
                int c1 = 0;
                while (true) {
//					c1 = r.read();
                    c1 = r.read(buffer, 0, buffer.length);
                    if (c1 == -1) {
                        break;
                    }
//					result.append((char) c1);
                    result.append(buffer, 0, c1);
                }
                response = result;
            }
        } else {
            response = new StringBuffer("invalid");
            Log.e("RS:", http.getResponseMessage());
        }
        if (http != null)
            http.disconnect();
        return new StringBuffer[]{response, sbCooki};
    }

    /**
     * Adds the cookies.
     *
     * @param http    the http
     * @param sbCooki the sb cooki
     */
    private void addCookies(HttpURLConnection http, StringBuffer sbCooki) {
        String headerName = "";
        for (int i = 1; (headerName = http.getHeaderFieldKey(i)) != null; i++) {

            if (headerName.equals("Set-Cookie")) {
                String cookieValue = http.getHeaderField(i);
                sbCooki.append(cookieValue);
            }
        }
    }

    /**
     * Creates the image.
     *
     * @param http the http
     * @return the string buffer[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private StringBuffer[] createImage(HttpURLConnection http) throws IOException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] chunk = new byte[128];
            int bytesRead;
            InputStream stream = http.getInputStream();
            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }
            stream.close();
//			responseImage = Image.createImage(context, outputStream.toByteArray(), 0, outputStream.size());
            outputStream.close();

            // responseImage = Image.createImage(context, http.getInputStream()
            // .toString());
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            throw new IOException(">>>> EXIR : Can not read image");
        }
        StringBuffer sbCooki = new StringBuffer();
        // ****************get Cookie ************
        addCookies(http, sbCooki);
        // ****************get Cookie ************

        if (http != null)
            http.disconnect();
        return new StringBuffer[]{new StringBuffer("loaded"), null};
    }


    public final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";


    /**
     * Perform web service request.
     *
     * @param url      the url
     * @param sendData the send data
     * @return the string buffer[]
     */
    private StringBuffer[] PerformWebServiceRequest_Soap(String url, String sendData) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, methodName);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("Input");
        pi.setValue(sendData);
        request.addProperty(pi);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(url);
//        httpTransport.setTimeout(this._TIME_OUT);
        String response = null;
        try {
            httpTransport.call(WSDL_TARGET_NAMESPACE + methodName, envelope);
            response = envelope.getResponse().toString();
        } catch (Exception exception) {
            FirebaseCrashlytics.getInstance().recordException(exception);
            FirebaseCrashlytics.getInstance().recordException(exception);
            response = "invalid";
        }
        return new StringBuffer[]{new StringBuffer(response), null};
    }

    private StringBuffer[] PerformWebServiceRequest_Soap(String url, HashMap<String, Object> sendData) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, methodName);

        for (String key : sendData.keySet()) {
            request.addProperty(key, sendData.get(key));
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(url);
//        httpTransport.setTimeout(this._TIME_OUT);
        String response = null;
        try {
            httpTransport.call(WSDL_TARGET_NAMESPACE + methodName, envelope);
            response = envelope.getResponse().toString();
        } catch (Exception exception) {
            FirebaseCrashlytics.getInstance().recordException(exception);
            exception.printStackTrace();
            response = "invalid";
        }
        return new StringBuffer[]{new StringBuffer(response), null};
    }

    private StringBuffer[] PerformWebServiceRequest(String url, String sendData) throws Exception {
        StringBuffer response = null;
        InputStream istrm = null;
        HttpURLConnection http = null;

        // proxy
        Proxy proxy = null;
        if (UseProxy) {
            InetSocketAddress sa = new InetSocketAddress(proxy_IpAddress, proxy_PortNumber);
            proxy = new Proxy(Proxy.Type.HTTP, sa);
        }

        if (UseProxy)
            http = (HttpURLConnection) new URL(url).openConnection(proxy);
        else
            http = (HttpURLConnection) new URL(url).openConnection();
        byte[] buf = sendData.getBytes("UTF-8");
        http.setRequestMethod("POST");
        http.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
        http.setRequestProperty("SOAPAction", soapAction);
        http.setRequestProperty("Content-length", "" + buf.length);
        http.setRequestProperty("User-Agent", "HttpMidlet/0.2");

        // proxy
        if (UseProxy) {

            String encodedLogin = Base64.encodeToString(proxy_Login.getBytes(), Base64.URL_SAFE);
            http.setRequestProperty("Proxy-Authorization", "Basic " + encodedLogin);
        }

        http.setReadTimeout(_TIME_OUT);
        http.setConnectTimeout(_TIME_OUT);
        // -------------------------------------------------------------------------------
        try {
            http.setDoInput(true);
            http.setDoOutput(true);
            if (sendData.length() > 0) {
                OutputStream out = http.getOutputStream();
                out.write(buf);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        // -------------------------------------------------------------------------------
        try {
            int rCode = http.getResponseCode();
            InputStream erCode = http.getErrorStream();
            if (rCode == HttpURLConnection.HTTP_OK) {
                int len = http.getContentLength();
                istrm = http.getInputStream();
                if (istrm == null) {
                    // ExirDebugger.println("Cannot open HTTP InputStream, aborting");
                }
                if (len != -1) {
                    Reader r = new InputStreamReader(istrm, "UTF-8");
                    // response = new StringBuffer();
                    StringWriter out1 = new StringWriter();
                    char[] buf2 = new char[2048];
                    int n;
                    int count = 0;

                    while ((n = r.read(buf2)) >= 0) {
                        out1.write(buf2, 0, n);
                        count += n;
                        setProgrss(count, len);
                    }
                    out1.close();
                    response = new StringBuffer(out1.toString());
                } else {
                    // Reader r = new InputStreamReader(istrm, "UTF-8");
                    // StringWriter out1 = new StringWriter();
                    // char[] buf2 = new char[2048];
                    // int n;
                    // int count = 0;
                    // while ((n = r.read(buf2)) >= 0) {
                    // out1.write(buf2, 0, n);
                    // count += n;
                    // }
                    // out1.close();
                    // response = new StringBuffer(out1.toString());
                    Reader r = new InputStreamReader(istrm, "UTF-8");
                    StringBuffer result = new StringBuffer();
                    int c1 = 0;
                    while (true) {
                        c1 = r.read();
                        if (c1 == -1) {
                            break;
                        }
                        result.append((char) c1);
                    }
                    response = result;

                }
            } else {
                response = new StringBuffer("invalid");
                Log.e("RS:", http.getResponseMessage());
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        if (http != null)
            http.disconnect();
        return new StringBuffer[]{response, null};
    }

    /**
     * Sets the progrss.
     *
     * @param total        the total
     * @param lenghtOfFile the lenght of file
     */
    private void setProgrss(int total, int lenghtOfFile) {
        // TODO wait
        // if (waitPage != null && lenghtOfFile > 0) {
        // int progress = (int) ((total * waitPage._MAX) / lenghtOfFile);
        // waitPage.setState(progress);
        // }
    }

    /**
     * Read connection stream.
     *
     * @param is  the is
     * @param len the len
     * @return the string buffer
     * @throws IOException                  Signals that an I/O exception has occurred.
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    private StringBuffer readConnectionStream(InputStream is, int len) throws IOException, UnsupportedEncodingException {
        StringBuffer result = new StringBuffer();
        if (encodeString) {
            DataInputStream dis = new DataInputStream(is);
            int c1 = dis.readUnsignedByte();
            int c2 = dis.readUnsignedByte();
            int c3 = dis.readUnsignedByte();
            int c4 = dis.readUnsignedByte();
            c1 = c2 * 256 + c1;
            c2 = c4 * 256 + c3;
            int c = c2 * 65536 + c1;
            StringBuffer buffer = new StringBuffer();
            boolean b = false;
            for (int i = 0; i < c; i++) {
                c1 = dis.readUnsignedByte();
                c2 = dis.readUnsignedByte();
                if (b)
                    c1++;
                else
                    c1--;
                b = !b;
                buffer.append((char) ((c2 * 256 + c1)));
                setProgrss(i, c);
            }
            result = buffer;
        } else {
            Reader r = new InputStreamReader(is, "UTF-8");

            StringWriter out1 = new StringWriter();
            char[] buf = new char[1024];
            int n;
            int count = 0;
            while ((n = r.read(buf)) >= 0) {
                out1.write(buf, 0, n);
                count += n;
                setProgrss(count, len);
            }
            out1.close();
            result = new StringBuffer(out1.toString());

        }
        return result;
    }

    /**
     * Prepare web service input xml.
     *
     * @param p          the p
     * @param methodName the method name
     * @return the string
     */
    public static String prepareWebServiceInputXML(Vector<String> p, String methodName) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/' xmlns:SOAP-ENC='http://schemas.xmlsoap.org/soap/encoding/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema'><SOAP-ENV:Body>");
        sb.append("<" + methodName + " xmlns=\"http://tempuri.org/\">");
        sb.append(prepareWebServiceParams(p));
        sb.append("</" + methodName + ">");
        sb.append("</SOAP-ENV:Body></SOAP-ENV:Envelope>");
        return sb.toString();
    }

    /**
     * Prepare web service params.
     *
     * @param p the p
     * @return the string
     */
    private static String prepareWebServiceParams(Vector<String> p) {
        if (p == null)
            return "";
        StringBuffer sb = new StringBuffer();
        int count = p.size();
        for (int i = 0; i < count; i += 2) {
            String paramName = p.elementAt(i);
            String paramValue = p.elementAt(i + 1);
            sb.append("<" + paramName + ">" + paramValue + "</" + paramName + ">");
        }
        return sb.toString();
    }
}
