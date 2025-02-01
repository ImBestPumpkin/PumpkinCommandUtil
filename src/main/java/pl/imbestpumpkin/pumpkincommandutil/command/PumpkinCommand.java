package pl.imbestpumpkin.pumpkincommandutil.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import pl.imbestpumpkin.pumpkincommandutil.manager.PumpkinCommandManager;

public abstract class PumpkinCommand extends BukkitCommand {

    public PumpkinCommand(String name, PumpkinCommandManager manager) {
        super(name);
        manager.createCommand(name, this);
    }

    public abstract boolean execute(CommandSender sender, String s, String[] args);
}
