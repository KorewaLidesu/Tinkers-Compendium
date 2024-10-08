package lance5057.tDefense;

import lance5057.tDefense.util.ArmorTagUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TCCommands extends CommandBase implements ICommand {
    private final List aliases;
    private final List commands;

    public TCCommands() {
        aliases = new ArrayList();

        aliases.add("TinkersDefense");
        aliases.add("TDefense");
        aliases.add("TD");

        commands = new ArrayList();

        commands.add("reloadRenderers");
        commands.add("toggleTransparency");
        commands.add("toggleDebugMode");
        commands.add("visor");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    // @Override
    // public int compareTo(Object arg0)
    // {
    // return 0;
    // }

    @Override
    public String getName() {
        return "TinkersDefense";
    }

    @Override
    public String getUsage(ICommandSender p_71518_1_) {
        return "TinkersDefense <text>";
    }

    // @Override
    // public List getCommandAliases()
    // {
    // return aliases;
    // }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] commandIn) {
        final World world = sender.getEntityWorld();
        if (world.isRemote) {
            // if(p_71515_2_[0].equals("reloadRenderers"))
            // {
            // p_71515_1_.addChatMessage(new TextComponentString(
            // "[TDefense] - Reloading All Renderers..."));
            // reloadModels();
            // }
            // else if(p_71515_2_[0].equals("toggleTransparency"))
            // {
            // TinkersDefense.config.transparency = !TinkersDefense.config.transparency;
            // if(TinkersDefense.config.transparency)
            // {
            // p_71515_1_.addChatMessage(new TextComponentString(
            // "[TDefense] - Transparency on."));
            // }
            // else
            // {
            // p_71515_1_.addChatMessage(new TextComponentString(
            // "[TDefense] - Transparency off."));
            // }
            //
            // }
            if (commandIn[0].equals("toggleDebugMode")) {
                TCConfig.debug = !TCConfig.debug;
                if (TCConfig.debug) {
                    sender.sendMessage(new TextComponentString("[TDefense] - Debug Mode on."));
                } else {
                    sender.sendMessage(new TextComponentString("[TDefense] - Debug Mode off."));
                }

            } else if (commandIn[0].equals("reloadRenderers")) {
                TinkersCompendium.proxy.reloadRenderers();
            } else if (commandIn[0].equals("visor")) {
                if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
                    EntityPlayer p = (EntityPlayer) sender.getCommandSenderEntity();
                    for (ItemStack s : p.getArmorInventoryList()) {
                        if (s != null && s.getItem() != Items.AIR) {
                            ArmorTagUtil.setVisor(s, !ArmorTagUtil.getVisor(s));
                            ArmorTagUtil.setVisorTime(s, 0.0f);
                            s.serializeNBT();
                            if (TCConfig.debug) {
                                if (ArmorTagUtil.getVisor(s))
                                    sender.sendMessage(new TextComponentString("[TDefense] - Visor closed."));
                                else
                                    sender.sendMessage(new TextComponentString("[TDefense] - Visor opened."));
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(new TextComponentString("�c[TDefense] - Invalid Command"));
            }
        }
    }

    // @Override
    // public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
    // {
    // return true;
    // }
    //
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          @Nullable BlockPos pos) {
        return commands;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    // public void reloadModels()
    // {
    // TinkersDefense.proxy.registerRenderers();
    // }
}
