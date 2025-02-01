package pl.imbestpumpkin.pumpkincommandutil.manager;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PumpkinCommandManager {
    private static JavaPlugin mainInstance;
    private static Map<String, BukkitCommand> customCommands;
    private Map<String, Command> knownCommands;

    private boolean logs = false;
    private Logger log;
    public PumpkinCommandManager(JavaPlugin main) {
        mainInstance = main;
        log = mainInstance.getLogger();
        customCommands = new HashMap<>();
        knownCommands = getCommandMap();
    }


    public void registerAll(boolean logs) {
        this.logs = logs;
        customCommands.forEach((name, cmd) -> {
            knownCommands.put(name, cmd);
            if (this.logs) {
                log.info("Registering command: " + name + " class name: " + cmd.getClass().getSimpleName());
            }
        });
    }

    private Map<String, Command> getCommandMap() {
        SimpleCommandMap commandMap = getSimpleCommandMap();

        Field knownCommandsField;
        Map<String, Command> knownCommands = null;
        try {
            knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            if (logs) {
                log.severe("Can't get CommandMap!");
            }
        }
        return knownCommands;
    }

    private SimpleCommandMap getSimpleCommandMap() {
        Field commandMapField;
        SimpleCommandMap commandMap = null;
        try {
            commandMapField = getCraftServer(mainInstance.getServer()).getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (SimpleCommandMap) commandMapField.get(mainInstance.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            if (logs) {
                log.severe("Can't get SimpleCommandMap!");
            }
        }
        return commandMap;
    }

    public void createCommand(String name, BukkitCommand command) {
        if (customCommands.get(name) != null)
            return;
        customCommands.put(name, command);
    }

    private Object getCraftServer(Server server) {
        if (server.getClass().getSimpleName().equals("CraftServer")) {
            return server;
        }
        if (logs) {
            log.severe("Can't get CraftServer class!");
        }
        return null;
    }
}
