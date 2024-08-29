package com.spring_project.one_group_chat.Controller;

import com.spring_project.one_group_chat.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // Mapped as /app/chat.sendMessage
    @MessageMapping("/chat.sendMessage")
    // Все, кто подписан на канал /topic/public получат сообщение
    @SendTo("/topic/public")
    // Так как @Payload, то автоматически будет отправлено в /topic/public
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    /**
     * Когда клиент отправляет сообщение на путь /chat.addUser, сервер обрабатывает это сообщение,
     * извлекает из него имя пользователя и сохраняет его в сессии.
     * После этого сообщение (или другая информация, представленная объектом ChatMessage) рассылается всем клиентам,
     * подписанным на топик /topic/public.
     * @param chatMessage Этот параметр помечен аннотацией @Payload, что означает,
     *                    что содержимое WebSocket-сообщения будет преобразовано в объект типа ChatMessage и
     *                    передано в метод в качестве аргумента.
     * @param headerAccessor Этот параметр позволяет получить доступ к заголовкам WebSocket-сообщения.
     *                       В данном случае он используется для доступа к атрибутам сессии.
     * @return Метод возвращает объект ChatMessage, который затем будет отправлен всем подписчикам канала /topic/public благодаря аннотации @SendTo.
     */
    // Mapped as /app/chat.addUser
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;

    }
}
