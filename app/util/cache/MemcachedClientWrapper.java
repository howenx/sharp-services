/*
 *    Copyright 2012-2014 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package util.cache;

import org.apache.ibatis.cache.CacheException;
import util.StringUtils;
import play.Logger;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

import static util.cache.MemcachedConfiguration.*;

/**
 * @author Simone Tripodi
 */
public class MemcachedClientWrapper {


    /**
     * Converts the MyBatis object key in the proper string representation.
     *
     * @param key the MyBatis object key.
     * @return the proper string representation.
     */
    private String toKeyString(final Object key) {
        return StringUtils.sha1Hex(key.toString());
    }

    /**
     * @param key
     * @return
     */
    public Object getObject(Object key) {
        String keyString = toKeyString(key);

        return retrieve(keyString);
    }

    /**
     * Return the stored group in Memcached identified by the specified key.
     *
     * @param groupKey the group key.
     * @return the group if was previously stored, null otherwise.
     */
    @SuppressWarnings("unchecked")
    private Set<String> getGroup(String groupKey) {

        Optional<Object> groupOption = Optional.ofNullable(retrieve(groupKey));
        if (groupOption.isPresent()) {
            return (Set<String>) groupOption.get();
        } else {
            return null;
        }
    }

    /**
     * @param keyString
     * @return
     * @throws Exception
     */
    private Object retrieve(final String keyString) {
        Object retrieved = null;

        try {
            if (usingAsyncGet) {
                Future<Object> future;
                if (compressionEnabled) {
                    Optional<Future<Object>> keyOption = Optional.ofNullable(memcachedClient.asyncGet(keyString, new CompressorTranscoder()));
                    if (keyOption.isPresent()) {
                        future = keyOption.get();
                    } else return null;
                } else {
                    Optional<Future<Object>> keyOption = Optional.ofNullable(memcachedClient.asyncGet(keyString));
                    if (keyOption.isPresent()) {
                        future = keyOption.get();
                    } else return null;
                }

                try {
                    retrieved = future.get(timeout, timeUnit);
                } catch (Exception e) {
                    future.cancel(false);
                    throw new CacheException(e);
                }
            } else {
                if (compressionEnabled) {
                    Optional<Object> keyOption = Optional.ofNullable(memcachedClient.get(keyString, new CompressorTranscoder()));
                    if (keyOption.isPresent()) {
                        retrieved = keyOption.get();
                    } else return null;
                } else {
                    Optional<Object> keyOption = Optional.ofNullable(memcachedClient.get(keyString));
                    if (keyOption.isPresent()) {
                        retrieved = keyOption.get();
                    } else return null;
                }
            }
            return retrieved;
        } catch (Exception ex) {
            return null;
        }
    }

    public void putObject(Object key, Object value, String id) {

        String keyString = toKeyString(key);
        String groupKey = toKeyString(id);

        storeInMemcached(keyString, value);

        Set<String> group = new HashSet<>();
        Optional<Set<String>> groupSet = Optional.ofNullable(getGroup(groupKey));
        if (groupSet.isPresent()) {
            group.addAll(groupSet.get());
        }
        group.add(keyString);

        storeInMemcached(groupKey, group);
    }

    /**
     * Stores an object identified by a key in Memcached.
     *
     * @param keyString the object key
     * @param value     the object has to be stored.
     */
    private void storeInMemcached(String keyString, Object value) {

        if (value != null
                && !Serializable.class.isAssignableFrom(value.getClass())) {
            throw new CacheException("Object of type '"
                    + value.getClass().getName()
                    + "' that's non-serializable is not supported by Memcached");
        }

        if (compressionEnabled) {
            memcachedClient.set(keyString, expiration, value, new CompressorTranscoder());
        } else {
            memcachedClient.set(keyString, expiration, value);
        }
    }

    public Object removeObject(Object key) {
        String keyString = toKeyString(key);

        Object result = getObject(key);
        if (result != null) {
            memcachedClient.delete(keyString);
        }
        return result;
    }

    public void removeGroup(String id) {
        String groupKey = toKeyString(id);

        Set<String> group = getGroup(groupKey);

        if (group == null) {
            Logger.error("No need to flush cached entries for group '"
                    + id
                    + "' because is empty");
            return;
        }

        Logger.error("Flushing keys: " + group);


        for (String key : group) {
            memcachedClient.delete(key);
        }

        Logger.error("Flushing group: " + groupKey);


        memcachedClient.delete(groupKey);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        memcachedClient.shutdown(timeout, timeUnit);
    }

}
