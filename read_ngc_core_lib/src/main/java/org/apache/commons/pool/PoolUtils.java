/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.pool;

import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class consists exclusively of static methods that operate on or return ObjectPool or KeyedObjectPool related interfaces.
 * 
 * @author Sandy McArthur
 * @version $Revision: 1222670 $ $Date: 2011-12-23 08:18:25 -0500 (Fri, 23 Dec 2011) $
 * @since Pool 1.3
 */
public final class PoolUtils {

	/**
	 * Timer used to periodically check pools idle object count. Because a {@link Timer} creates a {@link Thread} this is lazily instantiated.
	 */
	private static Timer MIN_IDLE_TIMER; // @GuardedBy("this")

	/**
	 * PoolUtils instances should NOT be constructed in standard programming. Instead, the class should be used procedurally: PoolUtils.adapt(aPool);. This
	 * constructor is public to permit tools that require a JavaBean instance to operate.
	 */
	public PoolUtils() {
	}

	/**
	 * Should the supplied Throwable be re-thrown (eg if it is an instance of one of the Throwables that should never be swallowed). Used by the pool error
	 * handling for operations that throw exceptions that normally need to be ignored.
	 * 
	 * @param t
	 *            The Throwable to check
	 * @throws ThreadDeath
	 *             if that is passed in
	 * @throws VirtualMachineError
	 *             if that is passed in
	 * @since Pool 1.5.5
	 */
	public static void checkRethrow(Throwable t) {
		if (t instanceof ThreadDeath) {
			throw (ThreadDeath) t;
		}
		if (t instanceof VirtualMachineError) {
			throw (VirtualMachineError) t;
		}
		// All other instances of Throwable will be silently swallowed
	}

	/**
	 * Wraps an <code>ObjectPool</code> and dynamically checks the type of objects borrowed and returned to the pool. If an object is passed to the pool that
	 * isn't of type <code>type</code> a {@link ClassCastException} will be thrown.
	 * 
	 * @param <T>
	 *            the type of object
	 * @param pool
	 *            the pool to enforce type safety on
	 * @param type
	 *            the class type to enforce.
	 * @return an <code>ObjectPool</code> that will only allow objects of <code>type</code>
	 * @since Pool 1.3
	 */
	public static <T> ObjectPool<T> checkedPool(final ObjectPool<T> pool, final Class<T> type) {
		if (pool == null) {
			throw new IllegalArgumentException("pool must not be null.");
		}
		if (type == null) {
			throw new IllegalArgumentException("type must not be null.");
		}
		return new CheckedObjectPool<T>(pool, type);
	}

	/**
	 * Periodically check the idle object count for the pool. At most one idle object will be added per period. If there is an exception when calling
	 * {@link ObjectPool#addObject()} then no more checks will be performed.
	 * 
	 * @param <T>
	 *            the type of object
	 * @param pool
	 *            the pool to check periodically.
	 * @param minIdle
	 *            if the {@link ObjectPool#getNumIdle()} is less than this then add an idle object.
	 * @param period
	 *            the frequency to check the number of idle objects in a pool, see {@link Timer#schedule(TimerTask, long, long)}.
	 * @return the {@link TimerTask} that will periodically check the pools idle object count.
	 * @throws IllegalArgumentException
	 *             when <code>pool</code> is <code>null</code> or when <code>minIdle</code> is negative or when <code>period</code> isn't valid for
	 *             {@link Timer#schedule(TimerTask, long, long)}.
	 * @since Pool 1.3
	 */
	public static <T> TimerTask checkMinIdle(final ObjectPool<T> pool, final int minIdle, final long period) throws IllegalArgumentException {
		if (pool == null) {
			throw new IllegalArgumentException("keyedPool must not be null.");
		}
		if (minIdle < 0) {
			throw new IllegalArgumentException("minIdle must be non-negative.");
		}
		final TimerTask task = new ObjectPoolMinIdleTimerTask<T>(pool, minIdle);
		getMinIdleTimer().schedule(task, 0L, period);
		return task;
	}

	/**
	 * Call <code>addObject()</code> on <code>pool</code> <code>count</code> number of times.
	 * 
	 * @param <T>
	 *            the type of object
	 * @param pool
	 *            the pool to prefill.
	 * @param count
	 *            the number of idle objects to add.
	 * @throws Exception
	 *             when {@link ObjectPool#addObject()} fails.
	 * @throws IllegalArgumentException
	 *             when <code>pool</code> is <code>null</code>.
	 * @since Pool 1.3
	 */
	public static <T> void prefill(final ObjectPool<T> pool, final int count) throws Exception, IllegalArgumentException {
		if (pool == null) {
			throw new IllegalArgumentException("pool must not be null.");
		}
		for (int i = 0; i < count; i++) {
			pool.addObject();
		}
	}

	/**
	 * Returns a synchronized (thread-safe) ObjectPool backed by the specified ObjectPool.
	 * 
	 * <p>
	 * <b>Note:</b> This should not be used on pool implementations that already provide proper synchronization such as the pools provided in the Commons Pool
	 * library. Wrapping a pool that {@link #wait() waits} for poolable objects to be returned before allowing another one to be borrowed with another layer of
	 * synchronization will cause liveliness issues or a deadlock.
	 * </p>
	 * 
	 * @param <T>
	 *            the type of object
	 * @param pool
	 *            the ObjectPool to be "wrapped" in a synchronized ObjectPool.
	 * @return a synchronized view of the specified ObjectPool.
	 * @since Pool 1.3
	 */
	public static <T> ObjectPool<T> synchronizedPool(final ObjectPool<T> pool) {
		if (pool == null) {
			throw new IllegalArgumentException("pool must not be null.");
		}
		/*
		 * assert !(pool instanceof GenericObjectPool) : "GenericObjectPool is already thread-safe"; assert !(pool instanceof SoftReferenceObjectPool) :
		 * "SoftReferenceObjectPool is already thread-safe"; assert !(pool instanceof StackObjectPool) : "StackObjectPool is already thread-safe"; assert
		 * !"org.apache.commons.pool.composite.CompositeObjectPool".equals(pool.getClass().getName()) : "CompositeObjectPools are already thread-safe";
		 */
		return new SynchronizedObjectPool<T>(pool);
	}

	/**
	 * Returns a pool that adaptively decreases it's size when idle objects are no longer needed. This is intended as an always thread-safe alternative to using
	 * an idle object evictor provided by many pool implementations. This is also an effective way to shrink FIFO ordered pools that experience load spikes.
	 * 
	 * @param <T>
	 *            the type of object
	 * @param pool
	 *            the ObjectPool to be decorated so it shrinks it's idle count when possible.
	 * @return a pool that adaptively decreases it's size when idle objects are no longer needed.
	 * @see #erodingPool(ObjectPool, float)
	 * @since Pool 1.4
	 */
	public static <T> ObjectPool<T> erodingPool(final ObjectPool<T> pool) {
		return erodingPool(pool, 1f);
	}

	/**
	 * Returns a pool that adaptively decreases it's size when idle objects are no longer needed. This is intended as an always thread-safe alternative to using
	 * an idle object evictor provided by many pool implementations. This is also an effective way to shrink FIFO ordered pools that experience load spikes.
	 * 
	 * <p>
	 * The factor parameter provides a mechanism to tweak the rate at which the pool tries to shrink it's size. Values between 0 and 1 cause the pool to try to
	 * shrink it's size more often. Values greater than 1 cause the pool to less frequently try to shrink it's size.
	 * </p>
	 * 
	 * @param <T>
	 *            the type of object
	 * @param pool
	 *            the ObjectPool to be decorated so it shrinks it's idle count when possible.
	 * @param factor
	 *            a positive value to scale the rate at which the pool tries to reduce it's size. If 0 &lt; factor &lt; 1 then the pool shrinks more
	 *            aggressively. If 1 &lt; factor then the pool shrinks less aggressively.
	 * @return a pool that adaptively decreases it's size when idle objects are no longer needed.
	 * @see #erodingPool(ObjectPool)
	 * @since Pool 1.4
	 */
	public static <T> ObjectPool<T> erodingPool(final ObjectPool<T> pool, final float factor) {
		if (pool == null) {
			throw new IllegalArgumentException("pool must not be null.");
		}
		if (factor <= 0f) {
			throw new IllegalArgumentException("factor must be positive.");
		}
		return new ErodingObjectPool<T>(pool, factor);
	}

	/**
	 * Get the <code>Timer</code> for checking keyedPool's idle count. Lazily create the {@link Timer} as needed.
	 * 
	 * @return the {@link Timer} for checking keyedPool's idle count.
	 * @since Pool 1.3
	 */
	private static synchronized Timer getMinIdleTimer() {
		if (MIN_IDLE_TIMER == null) {
			MIN_IDLE_TIMER = new Timer(true);
		}
		return MIN_IDLE_TIMER;
	}

	/**
	 * An object pool that performs type checking on objects passed to pool methods.
	 * 
	 */
	private static class CheckedObjectPool<T> implements ObjectPool<T> {
		/**
		 * Type of objects allowed in the pool. This should be a subtype of the return type of the underlying pool's associated object factory.
		 */
		private final Class<T> type;

		/** Underlying object pool */
		private final ObjectPool<T> pool;

		/**
		 * Create a CheckedObjectPool accepting objects of the given type using the given pool.
		 * 
		 * @param pool
		 *            underlying object pool
		 * @param type
		 *            expected pooled object type
		 * @throws IllegalArgumentException
		 *             if either parameter is null
		 */
		CheckedObjectPool(final ObjectPool<T> pool, final Class<T> type) {
			if (pool == null) {
				throw new IllegalArgumentException("pool must not be null.");
			}
			if (type == null) {
				throw new IllegalArgumentException("type must not be null.");
			}
			this.pool = pool;
			this.type = type;
		}

		/**
		 * Borrow an object from the pool, checking its type.
		 * 
		 * @return a type-checked object from the pool
		 * @throws ClassCastException
		 *             if the object returned by the pool is not of the expected type
		 */
		public T borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
			final T obj = pool.borrowObject();
			if (type.isInstance(obj)) {
				return obj;
			} else {
				throw new ClassCastException("Borrowed object is not of type: " + type.getName() + " was: " + obj);
			}
		}

		/**
		 * Return an object to the pool, verifying that it is of the correct type.
		 * 
		 * @param obj
		 *            object to return
		 * @throws ClassCastException
		 *             if obj is not of the expected type
		 */
		public void returnObject(final T obj) {
			if (type.isInstance(obj)) {
				try {
					pool.returnObject(obj);
				} catch (Exception e) {
					// swallowed as of Pool 2
				}
			} else {
				throw new ClassCastException("Returned object is not of type: " + type.getName() + " was: " + obj);
			}
		}

		/**
		 * Invalidates an object from the pool, verifying that it is of the expected type.
		 * 
		 * @param obj
		 *            object to invalidate
		 * @throws ClassCastException
		 *             if obj is not of the expected type
		 */
		public void invalidateObject(final T obj) {
			if (type.isInstance(obj)) {
				try {
					pool.invalidateObject(obj);
				} catch (Exception e) {
					// swallowed as of Pool 2
				}
			} else {
				throw new ClassCastException("Invalidated object is not of type: " + type.getName() + " was: " + obj);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
			pool.addObject();
		}

		/**
		 * {@inheritDoc}
		 */
		public int getNumIdle() throws UnsupportedOperationException {
			return pool.getNumIdle();
		}

		/**
		 * {@inheritDoc}
		 */
		public int getNumActive() throws UnsupportedOperationException {
			return pool.getNumActive();
		}

		/**
		 * {@inheritDoc}
		 */
		public void clear() throws Exception, UnsupportedOperationException {
			pool.clear();
		}

		/**
		 * {@inheritDoc}
		 */
		public void close() {
			try {
				pool.close();
			} catch (Exception e) {
				// swallowed as of Pool 2
			}
		}

		/**
		 * Sets the object factory associated with the pool
		 * 
		 * @param factory
		 *            object factory
		 * @deprecated to be removed in version 2.0
		 */
		@Deprecated
		public void setFactory(final PoolableObjectFactory<T> factory) throws IllegalStateException, UnsupportedOperationException {
			pool.setFactory(factory);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer();
			sb.append("CheckedObjectPool");
			sb.append("{type=").append(type);
			sb.append(", pool=").append(pool);
			sb.append('}');
			return sb.toString();
		}
	}

	/**
	 * Timer task that adds objects to the pool until the number of idle instances reaches the configured minIdle. Note that this is not the same as the pool's
	 * minIdle setting.
	 * 
	 */
	private static class ObjectPoolMinIdleTimerTask<T> extends TimerTask {

		/** Minimum number of idle instances. Not the same as pool.getMinIdle(). */
		private final int minIdle;

		/** Object pool */
		private final ObjectPool<T> pool;

		/**
		 * Create a new ObjectPoolMinIdleTimerTask for the given pool with the given minIdle setting.
		 * 
		 * @param pool
		 *            object pool
		 * @param minIdle
		 *            number of idle instances to maintain
		 * @throws IllegalArgumentException
		 *             if the pool is null
		 */
		ObjectPoolMinIdleTimerTask(final ObjectPool<T> pool, final int minIdle) throws IllegalArgumentException {
			if (pool == null) {
				throw new IllegalArgumentException("pool must not be null.");
			}
			this.pool = pool;
			this.minIdle = minIdle;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			boolean success = false;
			try {
				if (pool.getNumIdle() < minIdle) {
					pool.addObject();
				}
				success = true;

			} catch (Exception e) {
				cancel();

			} finally {
				// detect other types of Throwable and cancel this Timer
				if (!success) {
					cancel();
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer();
			sb.append("ObjectPoolMinIdleTimerTask");
			sb.append("{minIdle=").append(minIdle);
			sb.append(", pool=").append(pool);
			sb.append('}');
			return sb.toString();
		}
	}

	/**
	 * A synchronized (thread-safe) ObjectPool backed by the specified ObjectPool.
	 * 
	 * <p>
	 * <b>Note:</b> This should not be used on pool implementations that already provide proper synchronization such as the pools provided in the Commons Pool
	 * library. Wrapping a pool that {@link #wait() waits} for poolable objects to be returned before allowing another one to be borrowed with another layer of
	 * synchronization will cause liveliness issues or a deadlock.
	 * </p>
	 */
	private static class SynchronizedObjectPool<T> implements ObjectPool<T> {

		/** Object whose monitor is used to synchronize methods on the wrapped pool. */
		private final Object lock;

		/** the underlying object pool */
		private final ObjectPool<T> pool;

		/**
		 * Create a new SynchronizedObjectPool wrapping the given pool.
		 * 
		 * @param pool
		 *            the ObjectPool to be "wrapped" in a synchronized ObjectPool.
		 * @throws IllegalArgumentException
		 *             if the pool is null
		 */
		SynchronizedObjectPool(final ObjectPool<T> pool) throws IllegalArgumentException {
			if (pool == null) {
				throw new IllegalArgumentException("pool must not be null.");
			}
			this.pool = pool;
			lock = new Object();
		}

		/**
		 * {@inheritDoc}
		 */
		public T borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
			synchronized (lock) {
				return pool.borrowObject();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void returnObject(final T obj) {
			synchronized (lock) {
				try {
					pool.returnObject(obj);
				} catch (Exception e) {
					// swallowed as of Pool 2
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void invalidateObject(final T obj) {
			synchronized (lock) {
				try {
					pool.invalidateObject(obj);
				} catch (Exception e) {
					// swallowed as of Pool 2
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
			synchronized (lock) {
				pool.addObject();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public int getNumIdle() throws UnsupportedOperationException {
			synchronized (lock) {
				return pool.getNumIdle();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public int getNumActive() throws UnsupportedOperationException {
			synchronized (lock) {
				return pool.getNumActive();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void clear() throws Exception, UnsupportedOperationException {
			synchronized (lock) {
				pool.clear();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void close() {
			try {
				synchronized (lock) {
					pool.close();
				}
			} catch (Exception e) {
				// swallowed as of Pool 2
			}
		}

		/**
		 * Sets the factory used by the pool.
		 * 
		 * @param factory
		 *            new PoolableObjectFactory
		 * @deprecated to be removed in pool 2.0
		 */
		@Deprecated
		public void setFactory(final PoolableObjectFactory<T> factory) throws IllegalStateException, UnsupportedOperationException {
			synchronized (lock) {
				pool.setFactory(factory);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer();
			sb.append("SynchronizedObjectPool");
			sb.append("{pool=").append(pool);
			sb.append('}');
			return sb.toString();
		}
	}


	/**
	 * Encapsulate the logic for when the next poolable object should be discarded. Each time update is called, the next time to shrink is recomputed, based on
	 * the float factor, number of idle instances in the pool and high water mark. Float factor is assumed to be between 0 and 1. Values closer to 1 cause less
	 * frequent erosion events. Erosion event timing also depends on numIdle. When this value is relatively high (close to previously established high water
	 * mark), erosion occurs more frequently.
	 */
	private static class ErodingFactor {
		/** Determines frequency of "erosion" events */
		private final float factor;

		/** Time of next shrink event */
		private transient volatile long nextShrink;

		/** High water mark - largest numIdle encountered */
		private transient volatile int idleHighWaterMark;

		/**
		 * Create a new ErodingFactor with the given erosion factor.
		 * 
		 * @param factor
		 *            erosion factor
		 */
		public ErodingFactor(final float factor) {
			this.factor = factor;
			nextShrink = System.currentTimeMillis() + (long) (900000 * factor); // now + 15 min * factor
			idleHighWaterMark = 1;
		}

		/**
		 * Updates internal state based on numIdle and the current time.
		 * 
		 * @param numIdle
		 *            number of idle elements in the pool
		 */
		@SuppressWarnings("unused")
		public void update(final int numIdle) {
			update(System.currentTimeMillis(), numIdle);
		}

		/**
		 * Updates internal state using the supplied time and numIdle.
		 * 
		 * @param now
		 *            current time
		 * @param numIdle
		 *            number of idle elements in the pool
		 */
		public void update(final long now, final int numIdle) {
			final int idle = Math.max(0, numIdle);
			idleHighWaterMark = Math.max(idle, idleHighWaterMark);
			final float maxInterval = 15f;
			final float minutes = maxInterval + ((1f - maxInterval) / idleHighWaterMark) * idle;
			nextShrink = now + (long) (minutes * 60000f * factor);
		}

		/**
		 * Returns the time of the next erosion event.
		 * 
		 * @return next shrink time
		 */
		public long getNextShrink() {
			return nextShrink;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "ErodingFactor{" + "factor=" + factor + ", idleHighWaterMark=" + idleHighWaterMark + '}';
		}
	}

	/**
	 * Decorates an object pool, adding "eroding" behavior. Based on the configured {@link #factor erosion factor}, objects returning to the pool may be
	 * invalidated instead of being added to idle capacity.
	 * 
	 */
	private static class ErodingObjectPool<T> implements ObjectPool<T> {
		/** Underlying object pool */
		private final ObjectPool<T> pool;

		/** Erosion factor */
		private final ErodingFactor factor;

		/**
		 * Create an ErodingObjectPool wrapping the given pool using the specified erosion factor.
		 * 
		 * @param pool
		 *            underlying pool
		 * @param factor
		 *            erosion factor - determines the frequency of erosion events
		 * @see #factor
		 */
		public ErodingObjectPool(final ObjectPool<T> pool, final float factor) {
			this.pool = pool;
			this.factor = new ErodingFactor(factor);
		}

		/**
		 * {@inheritDoc}
		 */
		public T borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
			return pool.borrowObject();
		}

		/**
		 * Returns obj to the pool, unless erosion is triggered, in which case obj is invalidated. Erosion is triggered when there are idle instances in the
		 * pool and more than the {@link #factor erosion factor}-determined time has elapsed since the last returnObject activation.
		 * 
		 * @param obj
		 *            object to return or invalidate
		 * @see #factor
		 */
		public void returnObject(final T obj) {
			boolean discard = false;
			final long now = System.currentTimeMillis();
			synchronized (pool) {
				if (factor.getNextShrink() < now) { // XXX: Pool 3: move test out of sync block
					final int numIdle = pool.getNumIdle();
					if (numIdle > 0) {
						discard = true;
					}

					factor.update(now, numIdle);
				}
			}
			try {
				if (discard) {
					pool.invalidateObject(obj);
				} else {
					pool.returnObject(obj);
				}
			} catch (Exception e) {
				// swallowed
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void invalidateObject(final T obj) {
			try {
				pool.invalidateObject(obj);
			} catch (Exception e) {
				// swallowed
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
			pool.addObject();
		}

		/**
		 * {@inheritDoc}
		 */
		public int getNumIdle() throws UnsupportedOperationException {
			return pool.getNumIdle();
		}

		/**
		 * {@inheritDoc}
		 */
		public int getNumActive() throws UnsupportedOperationException {
			return pool.getNumActive();
		}

		/**
		 * {@inheritDoc}
		 */
		public void clear() throws Exception, UnsupportedOperationException {
			pool.clear();
		}

		/**
		 * {@inheritDoc}
		 */
		public void close() {
			try {
				pool.close();
			} catch (Exception e) {
				// swallowed
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @deprecated to be removed in pool 2.0
		 */
		@Deprecated
		public void setFactory(final PoolableObjectFactory<T> factory) throws IllegalStateException, UnsupportedOperationException {
			pool.setFactory(factory);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "ErodingObjectPool{" + "factor=" + factor + ", pool=" + pool + '}';
		}
	}

}