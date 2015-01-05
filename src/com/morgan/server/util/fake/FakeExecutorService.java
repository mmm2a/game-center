package com.morgan.server.util.fake;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;

/**
 * Fake implementation of an {@link ExecutorService} used for testing.  All jobs submitted to
 * this service are executed immediately.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class FakeExecutorService implements ExecutorService {

  @Override public void execute(Runnable command) {
    command.run();
  }

  @Override public void shutdown() {
    throw new UnsupportedOperationException();
  }

  @Override public List<Runnable> shutdownNow() {
    throw new UnsupportedOperationException();
  }

  @Override public boolean isShutdown() {
    return false;
  }

  @Override public boolean isTerminated() {
    return false;
  }

  @Override public boolean awaitTermination(
      long timeout, TimeUnit unit) throws InterruptedException {
    throw new UnsupportedOperationException();
  }

  @Override public <T> Future<T> submit(Callable<T> task) {
    try {
      return Futures.immediateFuture(task.call());
    } catch (Exception e) {
      return Futures.immediateFailedFuture(e);
    }
  }

  @Override public <T> Future<T> submit(Runnable task, T result) {
    try {
      task.run();
      return Futures.immediateFuture(result);
    } catch (Exception e) {
      return Futures.immediateFailedFuture(e);
    }
  }

  @Override public Future<?> submit(Runnable task) {
    try {
      task.run();
      return Futures.immediateFuture(null);
    } catch (Exception e) {
      return Futures.immediateFailedFuture(e);
    }
  }

  @Override public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
      throws InterruptedException {
    ImmutableList.Builder<Future<T>> builder = ImmutableList.builder();
    for (Callable<T> callable : tasks) {
      builder.add(submit(callable));
    }
    return builder.build();
  }

  @Override public <T> List<Future<T>> invokeAll(
      Collection<? extends Callable<T>> tasks,
      long timeout,
      TimeUnit unit) throws InterruptedException {
    return invokeAll(tasks);
  }

  @Override public <T> T invokeAny(
      Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
    throw new UnsupportedOperationException();
  }

  @Override public <T> T invokeAny(
      Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
          throws InterruptedException, ExecutionException, TimeoutException {
    throw new UnsupportedOperationException();
  }
}
