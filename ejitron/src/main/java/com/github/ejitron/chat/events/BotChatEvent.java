package com.github.ejitron.chat.events;

import com.github.ejitron.Bot;
import com.github.ejitron.chat.Chat;
import com.github.ejitron.sql.channels.Channel;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;

import java.util.Map;

public class BotChatEvent {
    @EventSubscriber
    public void onBotChat(IRCMessageEvent e) {
        if(!e.getMessage().isPresent() || e.getUser() == null) // Return since this is most likely back-end messages sent in chat
            return;

        String[] args = e.getMessage().get().split("\\s+");
        String user = e.getTags().get("display-name");
        String channel = e.getChannel().getName();

        if(!channel.equalsIgnoreCase("ejitron"))
            return;

        Chat chat = new Chat();

        if(!args[0].equalsIgnoreCase("!eji"))
            return;

        if(args[1].equalsIgnoreCase("join")) {
            Channel channels = new Channel();

            Map<String, Integer> registeredChannels = channels.getAddedChannels();

            if(Bot.twitchClient.getChat().isChannelJoined(user)) {
                chat.sendMessage(channel, "@" + user + " I'm already in your chat!");
                return;
            } else if(registeredChannels.containsKey(user) && !Bot.twitchClient.getChat().isChannelJoined(user)) {
                chat.sendMessage(channel, "@" + user + " I'm joining your channel now! Meet you over there!");
                Bot.twitchClient.getChat().joinChannel(user);
                return;
            } else if(!registeredChannels.containsKey(user)) {
                chat.sendMessage(channel, "@" + user + " You have not signed up to use me! Visit ejitron.tv to get started.");
                return;
            }
        }
    }
}
