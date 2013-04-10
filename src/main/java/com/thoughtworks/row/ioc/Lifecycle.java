package com.thoughtworks.row.ioc;

public enum Lifecycle {
    Singleton {
        @Override
        public ComponentProvider getProvider(ComponentProvider provider) {
            return new CacheComponentProvider(provider);
        }
    }, Transient;

    public ComponentProvider getProvider(ComponentProvider provider) {
        return provider;
    }
}
