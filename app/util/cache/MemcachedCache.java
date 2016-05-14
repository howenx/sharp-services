/*
 *    Copyright 2012 the original author or authors.
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

import com.google.common.base.Throwables;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import play.Logger;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * The Memcached-based Cache implementation.
 *
 * @author Simone Tripodi
 */
public final class MemcachedCache implements Cache {

    private MemcachedClientWrapper MEMCACHED_CLIENT = new MemcachedClientWrapper();

    private ReadWriteLock readWriteLock = new DummyReadWriteLock();

    private String id;

    public MemcachedCache(final String id) {
        this.id = id;
    }


    public void clear() {
        MEMCACHED_CLIENT.removeGroup(this.id);
    }


    public String getId() {
        return this.id;
    }


    public Object getObject(Object key) {
        try {
            return MEMCACHED_CLIENT.getObject(key);
        } catch (Exception ex) {
            return null;
        }
    }


    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }


    public int getSize() {
        return Integer.MAX_VALUE;
    }


    public void putObject(Object key, Object value) {

        try {
            MEMCACHED_CLIENT.putObject(key, value, this.id);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            Logger.error("存入错误--->" + Throwables.getStackTraceAsString(ignored));
        }
    }


    public Object removeObject(Object key) {
        return MEMCACHED_CLIENT.removeObject(key);
    }


    @Override
    public boolean equals(Object o) {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cache)) {
            return false;
        }

        Cache otherCache = (Cache) o;
        return getId().equals(otherCache.getId());
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        return getId().hashCode();
    }

}
