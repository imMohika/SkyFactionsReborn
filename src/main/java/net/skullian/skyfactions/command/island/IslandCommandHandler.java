package net.skullian.skyfactions.command.island;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.command.CommandHandler;
import net.skullian.skyfactions.command.CommandTemplate;
import net.skullian.skyfactions.command.island.cmds.*;
import net.skullian.skyfactions.util.CooldownManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.meta.SimpleCommandMeta;
import org.incendo.cloud.paper.PaperCommandManager;

import java.util.HashMap;
import java.util.Map;

public class IslandCommandHandler implements CommandHandler {

    PaperCommandManager<CommandSourceStack> manager;
    AnnotationParser<CommandSourceStack> parser;
    Map<String, CommandTemplate> subcommands = new HashMap<>();

    public IslandCommandHandler() {
        this.manager = PaperCommandManager.builder()
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildOnEnable(SkyFactionsReborn.getInstance());
        this.manager.registerCommandPostProcessor(new CooldownManager.CooldownPostprocessor<>());

        this.parser = new AnnotationParser<>(
                manager,
                CommandSourceStack.class,
                params -> SimpleCommandMeta.empty()
        );

        registerSubCommands(this.parser);
    }

    @Override
    public CommandHandler getHandler() {
        return this;
    }

    @Override
    public PaperCommandManager<CommandSourceStack> getManager() {
        return this.manager;
    }

    @Override
    public AnnotationParser<CommandSourceStack> getParser() {
        return this.parser;
    }

    @Override
    public Map<String, CommandTemplate> getSubCommands() {
        return this.subcommands;
    }

    @Override
    public void registerSubCommands(AnnotationParser<CommandSourceStack> parser) {
        register(new IslandCreateCommand());
        register(new IslandDeleteCommand());
        register(new IslandHelpCommand(this));
        register(new IslandTeleportCommand());
        register(new IslandTrustCommand());
        register(new IslandUntrustCommand());
        register(new IslandVisitCommand(this));
    }

    @Override
    public void register(CommandTemplate template) {
        parser.parse(template);
        subcommands.put(template.getName(), template);
    }

}
