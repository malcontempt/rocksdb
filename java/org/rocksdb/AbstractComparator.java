// Copyright (c) 2014, Facebook, Inc.  All rights reserved.
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree. An additional grant
// of patent rights can be found in the PATENTS file in the same directory.

package org.rocksdb;

/**
 * Comparators are used by RocksDB to determine
 * the ordering of keys.
 *
 * This class is package private, implementers
 * should extend either of the public abstract classes:
 *   @see org.rocksdb.Comparator
 *   @see org.rocksdb.DirectComparator
 */
abstract class AbstractComparator<T extends AbstractSlice> extends RocksObject {

  public abstract String name();

  /**
   * Three-way key comparison
   *
   *  @param a Slice access to first key
   *  @param b Slice access to second key
   *
   *  @return Should return either:
   *    1) < 0 if "a" < "b"
   *    2) == 0 if "a" == "b"
   *    3) > 0 if "a" > "b"
   */
  public abstract int compare(final T a, final T b);

  /**
   * Used to reduce the space requirements
   * for internal data structures like index blocks.
   *
   * If start < limit, you may return a new start which is a
   * shorter string in [start, limit).
   *
   * Simple comparator implementations may return null if they
   * wish to use start unchanged. i.e., an implementation of
   * this method that does nothing is correct.
   *
   * @return a shorter start, or null
   */
  public String findShortestSeparator(final String start, final T limit) {
      return null;
  }

  /**
   * Used to reduce the space requirements
   * for internal data structures like index blocks.
   *
   * You may return a new short key (key1) where
   * key1 >= key.
   *
   * Simple comparator implementations may return null if they
   * wish to leave the key unchanged. i.e., an implementation of
   * this method that does nothing is correct.
   *
   * @return a shorter key, or null
   */
  public String findShortSuccessor(final String key) {
      return null;
  }

  /**
   * Deletes underlying C++ comparator pointer.
   *
   * Note that this function should be called only after all
   * RocksDB instances referencing the comparator are closed.
   * Otherwise an undefined behavior will occur.
   */
  @Override protected void disposeInternal() {
    assert(isInitialized());
    disposeInternal(nativeHandle_);
  }

  private native void disposeInternal(long handle);
}
