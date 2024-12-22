package src.main.java.com.example.examplemod;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;

import java.util.Set;

public class Validate extends CommandBase {
    private final EGManager coords;

    public Validate(EGManager coords) {
        this.coords = coords;
    }

    @Override
    public String getCommandName() {
        return "eg:validate";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + this.getCommandName() + " <x> <y> <z>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        int x = parseInt(args[0]);
        int y = parseInt(args[1]);
        int z = parseInt(args[2]);

        BlockPos egCoords = new BlockPos(x, y, z);
        Set<BlockPos> savedCoords = coords.getSaved();

        if (savedCoords.contains(egCoords)) {
            sender.addChatMessage(new ChatComponentText("You registered this EG"));
        } else {
            sender.addChatMessage(new ChatComponentText("You haven't registered yet this EG"));
        }
    }
}
