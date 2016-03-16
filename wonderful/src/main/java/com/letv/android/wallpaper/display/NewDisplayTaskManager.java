
package com.letv.android.wallpaper.display;

import android.widget.ImageView;

import com.letv.android.wallpaper.cache.ByteArrayMemoryCache;
import com.letv.android.wallpaper.display.CacheTask.OnCompleteListener;
import com.letv.android.wallpaper.display.CacheTask.OnProgressListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NewDisplayTaskManager implements ICacheTaskManager, IDisplayTaskManager {
    private static final int CORE_POOL_SIZE = 6;
    private static final int MAXIMUM_POOL_SIZE = 12;
    private static final int KEEP_ALIVE_TIME = 9;
    private static final TimeUnit UNIT = TimeUnit.SECONDS;
    private static final int DISPLAY_TASK_QUEUE_CAPACITY = 15;
    private static final boolean DISPLAY_COMPLETE_RESULT = true;

    private static NewDisplayTaskManager sInstance;
    private ExecutorService mExecutorService;
    private LinkedBlockingQueue<Runnable> mWorkRunnableQueue;
    private volatile ConcurrentHashMap<CacheTask, Future<Boolean>> mDoingFutures;

    static {
        sInstance = new NewDisplayTaskManager();
    }

    public static NewDisplayTaskManager getInstance() {
        return sInstance;
    }

    private NewDisplayTaskManager() {
        initManager();
    }

    private void initManager() {
        if (mWorkRunnableQueue == null) {
            mWorkRunnableQueue = new LinkedBlockingQueue<Runnable>(DISPLAY_TASK_QUEUE_CAPACITY);
        }
        if (mExecutorService == null) {
            mExecutorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, UNIT, mWorkRunnableQueue);
        }
        if (mDoingFutures == null) {
            mDoingFutures = new ConcurrentHashMap<CacheTask, Future<Boolean>>(DISPLAY_TASK_QUEUE_CAPACITY);
        }
    }

    public void shutdown() {
        // cancel all displaying tasks
        cancelAllDoingTask();
        shutdownExecutorService();
        clearWorkRunnableQueue();
        clearDoingFutures();
        releaseMemoryCache();
    }

    private void clearDoingFutures() {
        // clear display futures
        if (mDoingFutures != null) {
            mDoingFutures.clear();
            mDoingFutures = null;
        }
    }

    private void clearWorkRunnableQueue() {
        // clear display tasks
        if (mWorkRunnableQueue != null) {
            mWorkRunnableQueue.clear();
            mWorkRunnableQueue = null;
        }
    }

    private void shutdownExecutorService() {
        // shut down executor service
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
            mExecutorService = null;
        }
    }

    private void releaseMemoryCache() {
        ByteArrayMemoryCache.getInstance().evictAll();
    }

    private void cancelAllDoingTask() {
        if (mDoingFutures != null) {
            // set isCancelled true
            final Collection<CacheTask> doingTasks = mDoingFutures.keySet();
            if (doingTasks != null) {
                for (CacheTask task : doingTasks) {
                    if (task != null) {
                        task.setCancelled(true);
                    }
                }
            }
            // cancel display
            final Collection<Future<Boolean>> doingFutures = mDoingFutures.values();
            if (doingFutures != null) {
                for (Future<Boolean> doingFuture : doingFutures) {
                    if (doingFuture != null && !doingFuture.isDone()) {
                        doingFuture.cancel(true);
                    }
                }
            }
        }
    }

    @Override
    public synchronized void display(ImageView imageView, String url, NewDisplayOptions displayOptions, OnCompleteListener onCompleteListener, OnProgressListener onProgressListener, long delay) {
        if (imageView != null && url != null && imageView.getContext() != null) {
            // check memory
            if (MemoryUtil.isMemoryLow(imageView.getContext())) {return;}
            // instantiate executorService
            initManager();
            // remove done futures
            removeDoneFutures();

            // instantiate displayTask
            if (displayOptions == null) {
                displayOptions = new NewDisplayOptions();
            }
            /*
            // set inBitmap
            final Object object = imageView.getTag(R.id.image_bitmap);
            if (object != null) {
                final Bitmap inBitmap = (Bitmap) object;
                if (inBitmap != null) {
                    final BitmapFactory.Options decodeOptions = displayOptions.getDecodeOptions();
                    if (decodeOptions != null) {
                        decodeOptions.inBitmap = inBitmap;
                    }
                }
            }
            */
            final NewDisplayTask displayTask = new NewDisplayTask(imageView, url, displayOptions, onCompleteListener, onProgressListener, delay);
            // check whether is displaying the same task
            final boolean isDisplayingSameImage = isDisplayingSameImage(displayTask);
            if (!isDisplayingSameImage) {
                // check whether is displaying the same imageView
                cancelDisplay(imageView);
                // display new task
                final NewDisplayRunnable displayRunnable = new NewDisplayRunnable(displayTask);
                // remove exceed tasks
                removeExceedWorkRunnable();
                // submit task
                final Future<Boolean> future = mExecutorService.submit(displayRunnable, DISPLAY_COMPLETE_RESULT);

                // remove done futures
                removeDoneFutures();
                // add displaying future
                addDoingFuture(displayTask, future);
            }
        }
    }

    @Override
    public synchronized void cancelDisplay(ImageView imageView) {
        // instantiate executorService
        initManager();
        final NewDisplayTask displayingTask = getDisplayingTask(imageView);
        if (displayingTask != null) {
            // set cancelled
            displayingTask.setCancelled(true);
            // cancel displaying task
            final Future<Boolean> displayingFuture = mDoingFutures.get(displayingTask);
            if (displayingFuture != null && !displayingFuture.isDone()) {
                displayingFuture.cancel(true);
            }
        }
    }

    @Override
    public synchronized void cache(String url, CacheOption cacheOption, OnCompleteListener onCompleteListener, OnProgressListener onProgressListener, long delay) {
        // check memory
        if (MemoryUtil.isRuntimeMemoryLow()) {return;}
        if (url != null) {
            // instantiate executorService
            initManager();
            // remove done futures
            removeDoneFutures();

            // instantiate cacheTask
            final CacheTask cacheTask = new CacheTask(url, cacheOption, onCompleteListener, onProgressListener, delay);
            // check whether is doing the same task
            final boolean isCaching = isCaching(cacheTask);
            if (!isCaching) {
                // cache new task
                final CacheRunnable cacheRunnable = new CacheRunnable(cacheTask);
                // remove exceed tasks
                removeExceedWorkRunnable();
                // submit task
                final Future<Boolean> future = mExecutorService.submit(cacheRunnable, DISPLAY_COMPLETE_RESULT);

                // remove done futures
                removeDoneFutures();
                // add doing future
                addDoingFuture(cacheTask, future);
            }
        }
    }

    @Override
    public synchronized void cancelCache(String url) {
        initManager();
        final CacheTask cachingTask = getCachingTask(url);
        if (cachingTask != null) {
            // set cancelled
            cachingTask.setCancelled(true);
            // cancel caching task
            final Future<Boolean> cachingFuture = mDoingFutures.get(cachingTask);
            if (cachingFuture != null && !cachingFuture.isDone()) {
                cachingFuture.cancel(true);
            }
        }
    }

    private CacheTask getCachingTask(String url) {
        if (url != null) {
            final Set<CacheTask> tasks = mDoingFutures.keySet();
            if (tasks != null && tasks.size() > 0) {
                for (CacheTask cacheTask : tasks) {
                    // urls equals
                    if (cacheTask != null && cacheTask.getUrl().equals(url)) {
                        // isDoing
                        final Future<Boolean> cachingFuture = mDoingFutures.get(cacheTask);
                        if (!cachingFuture.isDone()) {
                            return cacheTask;
                        } else {
                            mDoingFutures.remove(cacheTask);
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isCaching(CacheTask cacheTask) {
        if (cacheTask != null) {
            // contain task
            final boolean isCaching = mDoingFutures.containsKey(cacheTask);
            if (isCaching) {
                // future is doing
                final Future<Boolean> cachingFuture = mDoingFutures.get(cacheTask);
                final boolean isDoing = !cachingFuture.isDone();
                return isDoing;
            }
        }
        return false;
    }

    private void removeExceedWorkRunnable() {
        final int remainingCapacity = mWorkRunnableQueue.remainingCapacity();
        final int size = mWorkRunnableQueue.size();
        if (remainingCapacity == 0 && size == DISPLAY_TASK_QUEUE_CAPACITY) {
            final Runnable runnable = mWorkRunnableQueue.poll();
        }
    }

    private boolean isDisplayingSameImage(NewDisplayTask displayTask) {
        if (displayTask != null) {
            // contain task
            final boolean isDisplaying = mDoingFutures.containsKey(displayTask);
            if (isDisplaying) {
                // future is doing
                final Future<Boolean> displayingFuture = mDoingFutures.get(displayTask);
                final boolean isDoing = !displayingFuture.isDone();
                return isDoing;
            }
        }
        return false;
    }

    private NewDisplayTask getDisplayingTask(ImageView imageView) {
        if (imageView != null) {
            // contains imageView
            final Set<CacheTask> tasks = mDoingFutures.keySet();
            if (tasks != null && tasks.size() > 0) {
                for (CacheTask cacheTask : tasks) {
                    if (cacheTask instanceof NewDisplayTask) {
                        final NewDisplayTask displayingTask = (NewDisplayTask) cacheTask;
                        // urls not equals
                        if (displayingTask != null && displayingTask.getImageView() == imageView) {
                            // isDoing
                            final Future<Boolean> displayingFuture = mDoingFutures.get(displayingTask);
                            if (!displayingFuture.isDone()) {
                                return displayingTask;
                            } else {
                                mDoingFutures.remove(displayingTask);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void addDoingFuture(CacheTask cacheTask, Future<Boolean> future) {
        mDoingFutures.put(cacheTask, future);
    }

    private void removeDoneFutures() {
        final Set<Entry<CacheTask, Future<Boolean>>> entries = mDoingFutures.entrySet();
        if (entries != null && entries.size() != 0) {
            // get done future
            final ArrayList<CacheTask> doneFutures = new ArrayList<CacheTask>();
            for (Entry<CacheTask, Future<Boolean>> entry : entries) {
                final Future<Boolean> displayingFuture = entry.getValue();
                if (displayingFuture.isDone()) {
                    doneFutures.add(entry.getKey());
                }
            }
            // remove done futures
            for (CacheTask doneFuture : doneFutures) {
                mDoingFutures.remove(doneFuture);
            }
        }
        final int doingFuturesSize = mDoingFutures.entrySet().size();
    }
}
