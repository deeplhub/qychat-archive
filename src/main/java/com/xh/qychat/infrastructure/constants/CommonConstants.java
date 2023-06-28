package com.xh.qychat.infrastructure.constants;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface CommonConstants {
    /**
     * 可用处理器
     */
    Integer AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    /**
     * CPU密集型线程大小
     */
    Integer CPU_INTENSIVE_THREAD_SIZE = AVAILABLE_PROCESSORS + 1;

    /**
     * IO密集型线程大小
     */
    Integer IO_INTENSIVE_THREAD_SIZE = (int) (AVAILABLE_PROCESSORS / (1 - 0.7));

    String CHARSET_UTF8 = "UTF-8";

    int BATCH_SIZE = 1000;

    String RESOURCES_PATH = "/data/qychat_archive/";

    /**
     * 最大文件
     */
    int MAX_FILE_SIZE = 20 * 1024 * 1024;
}
