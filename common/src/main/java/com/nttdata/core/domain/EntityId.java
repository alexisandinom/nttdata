package com.nttdata.core.domain;


import java.util.Objects;

public abstract class EntityId<T> {
    protected final T value;

    protected EntityId(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityId<?> entityId = (EntityId<?>) o;
        return Objects.equals(value, entityId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
