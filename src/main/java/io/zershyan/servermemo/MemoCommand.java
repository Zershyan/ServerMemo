package io.zershyan.servermemo;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.apache.logging.log4j.util.Strings;

import java.nio.file.*;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@EventBusSubscriber(modid = ServerMemo.MODID)
public class MemoCommand {
    @SubscribeEvent
    public static void memoCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(literal("memo").then(literal("backup").executes(MemoCommand::backupMemo))
                .then(argument("info", StringArgumentType.greedyString()).executes(MemoCommand::memo)));
    }

    private static int memo(CommandContext<CommandSourceStack> context) {
        try {
            String info = StringArgumentType.getString(context, "info").trim();
            if(Strings.isBlank(info)) context.getSource().sendSuccess(() -> Component.literal("无有效信息"), true);
            Path history = Paths.get("memo/memo.txt");
            if (!Files.exists(history)) {
                if(!Files.exists(history.getParent())) {
                    Files.createDirectories(history.getParent());
                }
                Files.createFile(history);
            } else info = System.lineSeparator() + info;
            Files.writeString(history, info, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            context.getSource().sendSuccess(() -> Component.literal("已记录"), true);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    private static int backupMemo(CommandContext<CommandSourceStack> context) {
        try {
            Path memo = Paths.get("memo/memo.txt");
            CommandSourceStack source = context.getSource();
            if (!Files.exists(memo)) {
                source.sendSuccess(() -> Component.literal("未存在已有记录"), true);
                return 1;
            }
            Path target;
            for (int i = 0;; i++) {
                Path path = Paths.get("memo/memo" + i + ".txt");
                if(!Files.exists(path)) {
                    target = path;
                    break;
                }
            }
            Files.move(memo, target, StandardCopyOption.REPLACE_EXISTING);
            source.sendSuccess(() -> Component.literal("已备份已有记录"), true);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
