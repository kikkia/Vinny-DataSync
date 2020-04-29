package com.data.config;

import com.data.db.dao.ChannelDAO;
import com.data.db.dao.GuildDAO;
import com.data.db.dao.MembershipDAO;
import com.data.db.dao.ScheduledCommandDAO;
import com.data.tasks.*;
import com.data.config.properties.DiscordProperties;
import com.mewna.catnip.Catnip;
import com.mewna.catnip.shard.DiscordEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
public class DiscordConfig {

    @Bean
    public Catnip catnip(DiscordProperties properties,
                         ScheduledExecutorService executor,
                         ChannelDAO channelDAO,
                         GuildDAO guildDAO,
                         MembershipDAO membershipDAO,
                         ScheduledCommandDAO scheduledCommandDAO) {
        Catnip catnip = Catnip.catnip(properties.getToken());

        // Register events
        catnip.observable(DiscordEvent.GUILD_CREATE)
                .subscribe(guild -> executor.submit(new AddFreshGuildDeferredTask(guild,
                        guildDAO,
                        membershipDAO,
                        channelDAO)));

        catnip.observable(DiscordEvent.GUILD_DELETE)
                .subscribe(guild -> executor.submit(new LeaveGuildDeferredTask(guild,
                        membershipDAO,
                        guildDAO,
                        scheduledCommandDAO)));

        catnip.observable(DiscordEvent.GUILD_MEMBER_ADD)
                .subscribe(member -> executor.submit(new AddGuildMemberDeferredTask(member, membershipDAO)));

        catnip.observable(DiscordEvent.GUILD_MEMBER_REMOVE)
                .subscribe(member -> executor.submit(new RemoveGuildMemberDeferredTask(member, membershipDAO)));

        catnip.observable(DiscordEvent.CHANNEL_CREATE)
                .subscribe(channel -> executor.submit(new AddChannelDeferredTask(channel, channelDAO)));

        catnip.observable(DiscordEvent.CHANNEL_DELETE)
                .subscribe(channel -> executor.submit(new RemoveChannelDeferredTask(channel, channelDAO)));

        // Connect to discord
        return catnip.connect();
    }

    @Bean
    public ScheduledExecutorService executor() {
        return Executors.newScheduledThreadPool(10);
    }
}
