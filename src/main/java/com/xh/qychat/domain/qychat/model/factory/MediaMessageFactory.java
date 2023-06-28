package com.xh.qychat.domain.qychat.model.factory;

/**
 * @author H.Yang
 * @date 2023/6/28
 */
public class MediaMessageFactory {
    private static class Inner {
        private static final MediaMessageFactory instance = new MediaMessageFactory();
    }

    private MediaMessageFactory() {
    }

    public static MediaMessageFactory getSingleton() {
        return MediaMessageFactory.Inner.instance;
    }

    

}
