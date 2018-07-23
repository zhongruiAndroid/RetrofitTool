package com.github.retrofitutil;

import android.util.Log;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/1/11.
 */
public abstract class RetrofitCallBack<T> implements Callback {
    Type mType;
    public RetrofitCallBack(){
        mType = getSuperclassTypeParameter(getClass());
    }
    static Type getSuperclassTypeParameter(Class<?> subclass){
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class){
        ///throw new RuntimeException("Missing type parameter.");
            Log.e("Type", "Missing type parameter.");
            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
    protected abstract void onSuccess(T response);
    protected abstract void onError(Throwable t);
    @Override
    public void onResponse(Call call, Response response) {
        if(mType==String.class||mType==null){
            onSuccess((T)response.body().toString());
        }else{
            onSuccess((T)response.body());
        }
    }
    @Override
    public void onFailure(Call call, Throwable t) {
        onError(t);
    }
}
