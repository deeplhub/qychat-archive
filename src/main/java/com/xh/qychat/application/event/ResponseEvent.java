package com.xh.qychat.application.event;

import com.xh.qychat.infrastructure.common.enums.ResponseEnum;
import com.xh.qychat.infrastructure.common.model.Result;

/**
 * @author H.Yang
 * @date 2023/6/19
 */
public class ResponseEvent {
    public static Result reply(boolean isSuccess) {

        return isSuccess ? Result.operateSucceed() : Result.failed(ResponseEnum.INTERNAL_SERVER_FAIL);
    }

    public static Result reply(boolean isSuccess, ResponseEnum response) {

        return isSuccess ? Result.operateSucceed() : Result.failed(response);
    }
}
