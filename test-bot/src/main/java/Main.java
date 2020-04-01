import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main extends ListenerAdapter {
    public static void main(String[] args)
            throws LoginException, RateLimitedException, InterruptedException
    {
        String token = "Njk0MzAzNjgxMTk3MTEzMzc0.XoP2iw.CGzM98K_DaDI7aC71YmIn1t1_LE";

        JDA test = JDABuilder.createDefault(token).build();
        test.addEventListener(new Main());
    }

    @Override
    public void onMessageReceived (MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
        {
            return;
        }
        String str = event.getMessage().getContentDisplay();

        System.out.println("We received a message from " +
                event.getAuthor().getName() + ": " +
                str);
        for (int i = 0; i <  str.length() - 3; i ++) {
            String strA = event.getMessage().getContentDisplay().substring(i, i + 4);
            String strB = "";
            if (i > 5)
            {
                strB = event.getMessage().getContentDisplay().substring(i - 6, i + 4);
            }
            if (strA.equalsIgnoreCase("bars") && !strB.equalsIgnoreCase("small ")) {

                String id = event.getAuthor().getId();

                List<Role> save = event.getMember().getRoles();
                System.out.println(save);


                Role timeout = event.getGuild().getRolesByName("timeout", true).get(0);
                Role tab = event.getGuild().getRolesByName("Tab", true).get(0);
                System.out.println(event.getGuild().getRolesByName("timeout", true));

                String nickname = event.getMember().getNickname();
                event.getMember().modifyNickname("shame").queue();

                if (save.contains(tab)){
                    return;
                }
                for (int a = 0; a < save.size(); a++) {
                    event.getGuild().removeRoleFromMember(id, save.get(a)).queue();
                }
                event.getGuild().addRoleToMember(id, timeout).queue();
                System.out.println("added!");

                event.getGuild().removeRoleFromMember(id, timeout).queueAfter(30, TimeUnit.SECONDS);
                System.out.println("removed!");

                for (int a = 0; a < save.size(); a++) {
                    event.getGuild().addRoleToMember(id, save.get(a)).queue();
                }

                event.getMember().modifyNickname(nickname).queue();
                return;
            }
        }

        if (str.contains("!room")) {
            int start = Integer.parseInt(str.substring(0,2));
            int end = Integer.parseInt(str.substring(2,4));

            for (int i = start; i < end + 1; i ++) {
                ChannelAction<Category> action1 = event.getGuild().createCategory("Room " + i);
                action1.queue();
            }
        }

        if (str.contains("!channels"))
        {
            int start = Integer.parseInt(str.substring(0,2));
            int end = Integer.parseInt(str.substring(2,4));

            for (int i = start; i < end + 1; i ++) {
                Category parent = event.getGuild().getCategoriesByName("Room " + i, true).get(0);

                ChannelAction<TextChannel> action2 = event.getGuild().createTextChannel("room" + i + "docs").setParent(parent);
                action2.queue();
                ChannelAction<VoiceChannel> action3 = event.getGuild().createVoiceChannel("Room " + i).setParent(parent);
                action3.queue();
            }
        }
    }

    @Override
    public final void onMessageReactionAdd(MessageReactionAddEvent event) {
        System.out.println("Bot activitated");
        String id = event.getMessageId();

        if (event.getUser().isBot())
        {
            System.out.println("bot identified");
            event.getChannel().retrieveMessageById(id).complete().clearReactions().queueAfter(1, TimeUnit.SECONDS);
        }

    }
}