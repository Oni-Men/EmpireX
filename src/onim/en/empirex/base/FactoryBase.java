package onim.en.empirex.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.Maps;

public abstract class FactoryBase<V extends Identifiable> {

  protected Map<String, V> pool;
  
  public FactoryBase() {
    pool = Maps.newHashMap();

    this.init();
  }

  protected abstract void init();

  public boolean has(String key) {
    return pool.containsKey(key);
  }

  public void set(V value) {
    pool.put(value.id(), value);
  }

  public V get(String key) {
    if (pool.containsKey(key)) {
      return pool.get(key);
    }

    V value = value(key);

    if (value == null) {
      return null;
    }

    pool.put(key, value);
    return value;
  }

  public V remove(String key) {
    return pool.remove(key);
  }

  protected abstract V value(String key);

  public void executeIfPresent(String key, Consumer<V> executor) {
    V v = get(key);

    if (v != null) {
      executor.accept(v);
    }
  }

  public Collection<V> all() {
    return new ArrayList<>(pool.values());
  }
}
