package com.xh.qychat.infrastructure.integration.qychat.adapter;

import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import com.xh.qychat.infrastructure.integration.qychat.model.CustomerModel;
import com.xh.qychat.infrastructure.integration.qychat.model.MemberModel;

import java.util.List;
import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface QyChatAdapter {

    /**
     * 获取会话内容
     * https://developer.work.weixin.qq.com/document/path/91774
     *
     * @param seq 消息序号，获取消息记录开始的seq值
     * @return
     */
    List<ChatDataModel> listChatData(Long seq);

    /**
     * 下载会话记录文件
     *
     * @param sdkfileid
     * @param fileName
     * @return
     */
    void download(String sdkfileid, String fileName);

    /**
     * 获取客户群列表
     * <p>
     * 该接口用于获取配置过客户群管理的客户群列表。
     * <p>
     * https://developer.work.weixin.qq.com/document/path/92120
     *
     * @return
     */
    Set<String> listRoomId();

    /**
     * 获取群详情
     * <p>
     * 通过客户群ID，获取详情。包括群名、群成员列表、群成员入群时间、入群方式。（客户群是由具有客户群使用权限的成员创建的外部群）
     * <p>
     * 需注意的是，如果发生群信息变动，会立即收到群变更事件，但是部分信息是异步处理，可能需要等一段时间调此接口才能得到最新结果
     * <p>
     * https://developer.work.weixin.qq.com/document/path/92122
     *
     * @param roomid 群ID
     * @return
     */
    ChatRoomModel getChatRoomDetail(String roomid);

    /**
     * 获取成员详情（内部联系人）
     * <p>
     * https://developer.work.weixin.qq.com/document/path/90196
     *
     * @param userId
     * @return
     */
    MemberModel getMemberDetail(String userId);

    /**
     * 获取客户详情（外部联系人）
     *
     * <p>
     * https://developer.work.weixin.qq.com/document/path/92114
     *
     * @param userId
     * @return
     */
    CustomerModel getCustomerDetail(String userId);


    /**
     * 获取人员详情
     * <p>
     * 整合成员详情（内部联系人）和客户详情（外部联系人）两个接口的数据
     *
     * @param userId
     * @return
     */
    default MemberModel getPersonnelDetail(String userId) {
        // 获取客户详情（外部联系人）
        if (userId.startsWith("wb") || userId.startsWith("wo") || userId.startsWith("wm")) {
            MemberModel memberModel = new MemberModel();

            memberModel.setCustomerModel(this.getCustomerDetail(userId));

            return memberModel;
        }

        // 获取成员详情（内部联系人）
        return this.getMemberDetail(userId);
    }
}
