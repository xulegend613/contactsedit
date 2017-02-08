package com.simon.util;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

/**
 * Created by test on 2017/2/7.
 */
public class BackgroundThread {
    //用作IO线程，异步读写文件
    private static Handler sIOHandler = null;
    private static HandlerThread sIOHandlerThread;

    private static void ensureIOThread(){
        if(null == sIOHandlerThread || !sIOHandlerThread.isAlive()){
            sIOHandlerThread = new HandlerThread("IOThread");
            sIOHandlerThread.start();
            sIOHandler = new Handler(sIOHandlerThread.getLooper());
        }
        if(sIOHandler == null){
            sIOHandler = new Handler(sIOHandlerThread.getLooper());
        }
    }

    static class RunnableWrapper implements Runnable{

        private Runnable runnable;
        public RunnableWrapper(Runnable r){
            this.runnable = r;
        }

        @Override
        public void run() {
            if(runnable != null){
                long startTime = System.currentTimeMillis();

                runnable.run();
                long endTime = System.currentTimeMillis();

                if(endTime - startTime >= 200){

                }
            }
        }
    }

    public static void postOnIOThread(Runnable runnable){
        synchronized (BackgroundThread.class) {
            ensureIOThread();
            sIOHandler.post(new RunnableWrapper(runnable));
        }
    }

    public static void postOnIOThreadDelay(final Runnable runnable, long delayMillis){
        synchronized (BackgroundThread.class) {
            ensureIOThread();
            sIOHandler.postDelayed(new RunnableWrapper(runnable), delayMillis);
        }
    }


    public static void revokeOnIOThread(final Runnable runnable){
        synchronized (BackgroundThread.class) {
            ensureIOThread();
            sIOHandler.removeCallbacks(runnable);
        }
    }

    public static void runOnIOThread(Runnable r) {
        synchronized (BackgroundThread.class) {
            ensureIOThread();
            RunnableWrapper runnableWrapper = new RunnableWrapper(r);
            if (isOnIOThread()) {
                runnableWrapper.run();
            } else {
                sIOHandler.post(runnableWrapper);
            }
        }
    }

    public static <T> T runOnIOThreadBlocking(Callable<T> c) {
        FutureTask<T> task = new FutureTask<T>(c);
        runOnIOThread(task);
        try {
            return task.get();
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isOnIOThread() {
        synchronized (BackgroundThread.class) {
            ensureIOThread();
            return sIOHandler.getLooper() == Looper.myLooper();
        }
    }

    public static <T> void executeAsyncTask(final AsyncTask<T, ?, ?> task,
                                            final T... params) {
        ThreadHelper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
                    }
                    else {
                        task.execute(params);
                    }
                } catch (Throwable e) {
                }
            }
        });
    }

    private BackgroundThread() {
    }
}
