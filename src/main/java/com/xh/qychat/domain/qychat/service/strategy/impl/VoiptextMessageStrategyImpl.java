package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.core.date.DateUtil;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.domain.qychat.service.strategy.dto.ChatDataMessageDTO;

/**
 * 音视频通话
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public class VoiptextMessageStrategyImpl implements MessageStrategy {

    @Override
    public String process(ChatDataMessageDTO chatDataDto) {

        return this.getVoipText(chatDataDto.getInvitetype(), chatDataDto.getCallduration());
    }

    private String getVoipText(Integer invitetype, Integer callduration) {
        String text = "";
        switch (invitetype) {
            case 1:
                text = "单人视频通话时长 ";
                break;
            case 2:
                text = "单人语音通话时长 ";
                break;
            case 3:
                text = "多人视频通话时长 ";
                break;
            case 4:
                text = "多人语音通话时长 ";
                break;
            default:
                break;
        }

        return text + this.getDuration(callduration);
    }

    private String getDuration(Integer callduration) {
        if (callduration == null) return "";

        long millisecond = callduration * 1000;
        return DateUtil.formatBetween(millisecond);
    }
}
