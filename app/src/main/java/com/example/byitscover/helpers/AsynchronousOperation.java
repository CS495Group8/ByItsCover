package com.example.byitscover.helpers;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class can be used to asynchronously execute an operation returning a value of type V.
 * The operation may be in one of three states: cancelled, completed, or ongoing.
 * If the operation is completed, the completion function is executed (if not null)
 * on the thread which created the instance of the class via the message loop of the thread.
 *
 * If a thread calls the constructor without an associated message loop, IllegalThreadStateException
 * is thrown.
 * Note that the UI thread has a message loop.
 *
 * The user of this class must ensure that the operation will finish.
 * If the operation always finishes on its own, then there is no need to do anything.
 * Otherwise the user must ensure that the operation is completed or cancelled before losing
 * all references to an instance.
 * This is necessary to avoid leaking resources such as threads and memory.
 *
 * @author Jack
 * @version 1.0
 */

public class AsynchronousOperation<V> {
    private final ExecutorService executor;
    private final Future<V> future;

    public AsynchronousOperation(Callable<V> operation, Runnable completion) {
        final Looper currentThreadLooper = Looper.myLooper();

        if (currentThreadLooper == null)
            throw new IllegalThreadStateException("The current thread does not have a message loop");

        final Handler handler = new Handler(currentThreadLooper);

        // Execute operation, and then notify thread of completion
        executor = Executors.newSingleThreadExecutor();

        future = executor.submit(operation);

        if (completion != null)
           executor.submit(new Runnable() {
               @Override
               public void run() {
                   if (!future.isCancelled())
                       handler.post(completion);
               }
           });

    }

    /**
     * If the operation has completed, the result of the operation is returned or an exception is propagated.
     * When the operation is ongoing, the current thread will be blocked until the operation is complete or cancelled.
     * Attempting to get the result of a cancelled operation throws an exception.
     *
     * @return the result of the operation
     * @throws CancellationException if operation was cancelled
     * @throws InterruptedException if interruption occurs while waiting
     * @throws ExecutionException if operation finished by throwing an exception, the exception is
     * propagated.
     */
    public V get() throws CancellationException, InterruptedException, ExecutionException {
        return future.get();
    }

    /**
     * Gets the status of the operation
     * @return whether the operation completed or was cancelled
     */
    public boolean isDone() {
        return future.isDone();
    }

    /**
     * Determines if the operation was cancelled
     * @return whether the operation was cancelled
     */
    public boolean isCancelled() {
        return future.isCancelled();
    }

    /**
     * Cancels an ongoing operation.
     * If the operation has already completed or been cancelled, then false is returned.
     * @return whether the cancellation was successful
     */
    public boolean cancel() {
        return future.cancel(true);
    }
}
