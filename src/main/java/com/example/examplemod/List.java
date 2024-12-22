package src.main.java.com.example.examplemod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;

public class List extends CommandBase {
    private final EGManager coords;

    public List(EGManager coords) {
        this.coords = coords;
    }

    @Override
    public String getCommandName() {
        return "eg:list";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + this.getCommandName() + " <world> <type>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

        IChatComponent message = new ChatComponentText("");
        sender.addChatMessage(new ChatComponentText("-----------------------------------" +
                "" +
                "" +
                ""));

        if (args.length > 1) {
            if (Arrays.asList(coords.types()).contains(args[1])) {
                // EGManager egm = something.getFrom(args[0], args[1])
            } else sender.addChatMessage(new ChatComponentText("The easter egg type does not exist."));
        } else sender.addChatMessage(new ChatComponentText("You have found so far a total of  " + coords.getSaved().size() + " easter eggs."));
    }

    @Override
    public java.util.List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return getListOfStringsMatchingLastWord(args, egm.getDimensions(), egm.getTypes());
    }
}
