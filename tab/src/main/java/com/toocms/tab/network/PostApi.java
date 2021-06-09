package com.toocms.tab.network;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ObjectUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.wrapper.entity.Progress;

/**
 * POST请求方式
 * <p>
 * Author：Zero
 * Date：2020/9/30 15:20
 */
public class PostApi {

    private RxHttpFormParam param;

    private PostApi(RxHttpFormParam param) {
        this.param = param;
    }

    public static PostApi post(@NonNull String url) {
        return new PostApi(RxHttp.postForm(url));
    }

    public PostApi params(HttpParams params) {
        if (ObjectUtils.isNotEmpty(params)) {
            param.addAll(params.urlParamsMap);
            for (Map.Entry<String, File> entry : params.fileParamsMap.entrySet()) {
                param.addFile(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public PostApi params(Map<String, ?> params) {
        if (ObjectUtils.isNotEmpty(params)) {
            param.addAll(params);
        }
        return this;
    }

    public PostApi add(String key, Object value) {
        param.add(key, value);
        return this;
    }

    public PostApi addFile(String key, File value) {
        param.addFile(key, value);
        return this;
    }

    public PostApi addFile(String key, String filePath) {
        param.addFile(key, filePath);
        return this;
    }

    /**
     * 此次请求添加请求头部
     *
     * @param key
     * @param value
     * @return
     */
    public PostApi addHeader(String key, String value) {
        param.addHeader(key, value);
        return this;
    }

    public PostApi addHeader(String key, String value, boolean isAdd) {
        param.addHeader(key, value, isAdd);
        return this;
    }

    public PostApi addAllHeader(Map<String, String> headers) {
        param.addAllHeader(headers);
        return this;
    }

    public PostApi setHeader(String key, String value) {
        param.setHeader(key, value);
        return this;
    }

    public PostApi setAllHeader(Map<String, String> headers) {
        param.setAllHeader(headers);
        return this;
    }

    /**
     * 设置此次请求要是否添加公共参数
     *
     * @param enabled true - 添加，false - 不添加
     * @return
     */
    public PostApi setAssemblyEnabled(boolean enabled) {
        param.setAssemblyEnabled(enabled);
        return this;
    }

    /**
     * 上传
     *
     * @param progressConsumer 进度回调
     * @return
     */
    public PostApi asUpload(Consumer<Progress> progressConsumer) {
        param.upload(AndroidSchedulers.mainThread(), progressConsumer);
        return this;
    }

    /**
     * 结果解析成对象类型
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> TooCMSObservable<T> asTooCMSResponse(Class<T> cls) {
        return TooCMSObservable.create(param).asTooCMSResponse(cls);
    }

    /**
     * 结果解析成列表类型
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> TooCMSObservable<List<T>> asTooCMSResponseList(Class<T> cls) {
        return TooCMSObservable.create(param).asTooCMSResponseList(cls);
    }
}
