//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.retrofitutil.logger;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

class LoggerPrinter implements Printer {
    private static final int JSON_INDENT = 2;
    private final ThreadLocal<String> localTag = new ThreadLocal();
    private final List<LogAdapter> logAdapters = new ArrayList();

    LoggerPrinter() {
    }

    public Printer t(String tag) {
        if(tag != null) {
            this.localTag.set(tag);
        }

        return this;
    }

    public void d(String message, Object... args) {
        this.log(3, (Throwable)null, message, (Object[])args);
    }

    public void d(Object object) {
        this.log(3, (Throwable)null, Utils.toString(object), (Object[])(new Object[0]));
    }

    public void e(String message, Object... args) {
        this.e((Throwable)null, message, args);
    }

    public void e(Throwable throwable, String message, Object... args) {
        this.log(6, (Throwable)throwable, message, (Object[])args);
    }

    public void w(String message, Object... args) {
        this.log(5, (Throwable)null, message, (Object[])args);
    }

    public void i(String message, Object... args) {
        this.log(4, (Throwable)null, message, (Object[])args);
    }

    public void v(String message, Object... args) {
        this.log(2, (Throwable)null, message, (Object[])args);
    }

    public void wtf(String message, Object... args) {
        this.log(7, (Throwable)null, message, (Object[])args);
    }

    public void json(String json) {
        if(Utils.isEmpty(json)) {
            this.d("Empty/Null json content");
        } else {
            try {
                json = json.trim();
                String message;
                if(json.startsWith("{")) {
                    JSONObject e1 = new JSONObject(json);
                    message = e1.toString(2);
                    this.d(message);
                    return;
                }

                if(json.startsWith("[")) {
                    JSONArray e = new JSONArray(json);
                    message = e.toString(2);
                    this.d(message);
                    return;
                }

                this.e("Invalid Json", new Object[0]);
            } catch (JSONException var4) {
                this.e("Invalid Json", new Object[0]);
            }

        }
    }

    public void xml(String xml) {
        if(Utils.isEmpty(xml)) {
            this.d("Empty/Null xml content");
        } else {
            try {
                StreamSource e = new StreamSource(new StringReader(xml));
                StreamResult xmlOutput = new StreamResult(new StringWriter());
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty("indent", "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(e, xmlOutput);
                this.d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
            } catch (TransformerException var5) {
                this.e("Invalid xml", new Object[0]);
            }

        }
    }

    public synchronized void log(int priority, String tag, String message, Throwable throwable) {
        if(throwable != null && message != null) {
            message = message + " : " + Utils.getStackTraceString(throwable);
        }

        if(throwable != null && message == null) {
            message = Utils.getStackTraceString(throwable);
        }

        if(Utils.isEmpty(message)) {
            message = "Empty/NULL log message";
        }

        Iterator var5 = this.logAdapters.iterator();

        while(var5.hasNext()) {
            LogAdapter adapter = (LogAdapter)var5.next();
            if(adapter.isLoggable(priority, tag)) {
                adapter.log(priority, tag, message);
            }
        }

    }

    public void clearLogAdapters() {
        this.logAdapters.clear();
    }

    public void addAdapter(LogAdapter adapter) {
        this.logAdapters.add(adapter);
    }

    private synchronized void log(int priority, Throwable throwable, String msg, Object... args) {
        String tag = this.getTag();
        String message = this.createMessage(msg, args);
        this.log(priority, tag, message, throwable);
    }

    private String getTag() {
        String tag = (String)this.localTag.get();
        if(tag != null) {
            this.localTag.remove();
            return tag;
        } else {
            return null;
        }
    }

    private String createMessage(String message, Object... args) {
        return args != null && args.length != 0?String.format(message, args):message;
    }
}
