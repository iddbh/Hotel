package com.sym.hotel.Service.imp;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sym.hotel.mapper.MessageMapper;
import com.sym.hotel.pojo.Message;
import com.sym.hotel.pojo.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageMapper messageMapper;

//    @Scheduled(cron = "*/15 * * * * *")
    public List<Message> getMessage(int sendId) {
        List<Message> messages = messageMapper.selectList(new MPJLambdaWrapper<Message>()
                .selectAll(Message.class).eq(Message::getSendId,sendId)
                .orderByAsc(Message::getTime));    //执行Mapper中的方法
        return messages;
    }
    public void addMessage(int sendId, String content, Date time){
        Message message=new Message();
        message.setSendId(sendId);
        message.setMessage(content);
        message.setTime(time);
        messageMapper.insert(message);
    }

}
