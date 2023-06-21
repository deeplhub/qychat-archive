package com.xh.qychat.domain.qychat.model;

import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/21
 */
@Data
@NoArgsConstructor
public class MessageContent {

    private List<ChatDataModel> dataModelList;

    public MessageContent(List<ChatDataModel> dataModelList) {
        this.dataModelList = dataModelList;
    }

}
