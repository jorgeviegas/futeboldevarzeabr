package br.com.sharkweb.fbv.converters;

/**
 * Created by i848340 on 3/22/16.
 */
public abstract class AbstractConverter<T, V> {
    public AbstractConverter(Class<T> fromClass, Class<V> toClass) {
        throw new RuntimeException("Ero!");
    }

    public abstract V convert(T var1);

}