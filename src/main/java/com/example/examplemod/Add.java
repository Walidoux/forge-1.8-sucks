package src.main.java.com.example.examplemod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandException;

public class Add extends CommandBase {
    @Override
    public String getCommandName() {
        return "eg:add";
    }

    @Override
    public String getCommandUsage() {
        return "/" + this.getCommandName();
    }

    @Override
    public boolean canCommandSenderUseCommand() {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        // TODO
    }
}
