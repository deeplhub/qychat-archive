package com.xh.qychat.domain.qychat.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 微信群详情 Mapper 接口
 * </p>
 *
 * @author H.Yang
 * @since 2023-04-19
 */
@Mapper
public interface ChatRoomMapper extends BaseMapper<ChatRoomEntity> {

    List<ChatRoomEntity> listByChatId(@Param("chatIds") Set<String> chatIds);


    /**
     * 查询所有群
     * <p>
     * 根据消息时间排序
     *
     * @return
     */
    @Select("SELECT a.chat_id, a.name, a.owner FROM qychat_chat_room a " +
            "INNER JOIN ( SELECT a.roomid, MAX(a.msgtime) AS latest_msgtime FROM qychat_message_content a GROUP BY a.roomid ) b ON b.roomid=a.chat_id " +
            "ORDER BY b.latest_msgtime DESC")
    List<ChatRoomEntity> listAll();
}
