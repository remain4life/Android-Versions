package org.remain4life.androidversions.helpers.rx;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;

/**
 * Modified class from Rx to work on Android with Room RxJava2,
 * needed to inject handlers to certain standard RxAndroid operations.
 */
public final class RxAndroidPlugins {

    private static volatile Function<Callable<Scheduler>, Scheduler> onInitMainThreadHandler;
    private static volatile Function<Scheduler, Scheduler> onMainThreadHandler;

    private static final Scheduler MAIN_THREAD = RxAndroidPlugins.initMainThreadScheduler(
            () -> new HandlerScheduler(new Handler(Looper.getMainLooper())));

    private static void setInitMainThreadSchedulerHandler(Function<Callable<Scheduler>, Scheduler> handler) {
        onInitMainThreadHandler = handler;
    }

    private static Scheduler initMainThreadScheduler(Callable<Scheduler> scheduler) {
        if (scheduler == null) {
            throw new NullPointerException("scheduler == null");
        }
        Function<Callable<Scheduler>, Scheduler> f = onInitMainThreadHandler;
        if (f == null) {
            return callRequireNonNull(scheduler);
        }
        return applyRequireNonNull(f, scheduler);
    }

    private static void setMainThreadSchedulerHandler(Function<Scheduler, Scheduler> handler) {
        onMainThreadHandler = handler;
    }

    public static Scheduler onMainThreadScheduler() {
        return onMainThreadScheduler(MAIN_THREAD);
    }

    public static Scheduler onMainThreadScheduler(Scheduler scheduler) {
        if (scheduler == null) {
            throw new NullPointerException("scheduler == null");
        }
        Function<Scheduler, Scheduler> f = onMainThreadHandler;
        if (f == null) {
            return scheduler;
        }
        return apply(f, scheduler);
    }

    /**
     * Returns the current hook function.
     * @return the hook function, may be null
     */
    public static Function<Callable<Scheduler>, Scheduler> getInitMainThreadSchedulerHandler() {
        return onInitMainThreadHandler;
    }

    /**
     * Returns the current hook function.
     * @return the hook function, may be null
     */
    public static Function<Scheduler, Scheduler> getOnMainThreadSchedulerHandler() {
        return onMainThreadHandler;
    }

    /**
     * Removes all handlers and resets the default behavior.
     */
    public static void reset() {
        setInitMainThreadSchedulerHandler(null);
        setMainThreadSchedulerHandler(null);
    }

    private static Scheduler callRequireNonNull(Callable<Scheduler> s) {
        try {
            Scheduler scheduler = s.call();
            if (scheduler == null) {
                throw new NullPointerException("Scheduler Callable returned null");
            }
            return scheduler;
        } catch (Throwable ex) {
            throw Exceptions.propagate(ex);
        }
    }

    private static Scheduler applyRequireNonNull(Function<Callable<Scheduler>, Scheduler> f, Callable<Scheduler> s) {
        Scheduler scheduler = apply(f,s);
        if (scheduler == null) {
            throw new NullPointerException("Scheduler Callable returned null");
        }
        return scheduler;
    }

    private static <T, R> R apply(Function<T, R> f, T t) {
        try {
            return f.apply(t);
        } catch (Throwable ex) {
            throw Exceptions.propagate(ex);
        }
    }

    private RxAndroidPlugins() {
        throw new AssertionError("No instances.");
    }
}
