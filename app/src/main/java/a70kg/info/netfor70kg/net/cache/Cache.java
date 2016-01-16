package a70kg.info.netfor70kg.net.cache;

/**
 * Created by Mr_Wrong on 16/1/16.
 */
public interface Cache<K, V> {

    public V get(K key);

    public void put(K key, V value);

    public void remove(K key);

}