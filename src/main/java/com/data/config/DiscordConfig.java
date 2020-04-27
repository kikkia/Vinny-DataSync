package com.data.config;

import com.data.db.dao.ChannelDAO;
import com.data.db.dao.GuildDAO;
import com.data.db.dao.MembershipDAO;
import com.data.db.dao.ScheduledCommandDAO;
import com.data.tasks.AddFreshGuildDeferredTask;
import com.data.config.properties.DiscordProperties;
import com.data.tasks.LeaveGuildDeferredTask;
import com.mewna.catnip.Catnip;
import com.mewna.catnip.shard.DiscordEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class DiscordConfig {

    @Bean
    public Catnip catnip(DiscordProperties properties,
                         ExecutorService executor,
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
                .subscribe(guild -> new LeaveGuildDeferredTask(guild, membershipDAO, guildDAO, scheduledCommandDAO).run());

        catnip.observable(DiscordEvent.GUILD_MEMBER_ADD)
                .subscribe(member -> System.out.println(member.user().username()));

        catnip.observable(DiscordEvent.GUILD_MEMBER_REMOVE)
                .subscribe(member -> System.out.println(member.user().username()));

        // Connect to discord
        return catnip.connect();
    }

    @Bean
    public ExecutorService executor() {
        return Executors.newFixedThreadPool(10);
    }
}
