package net.galaxa.qs;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import net.galaxa.qs.cmds.SignCommand;

public class SignEvent implements Listener
{
	@EventHandler
	public void onCreate(SignChangeEvent e)
	{
		Sign sign = (Sign) e.getBlock().getState();

		Player p = e.getPlayer();

		if(SignCommand.signC.containsKey(p.getName()))
		{
			Location loc = sign.getLocation();

			ArrayList<String> list = (ArrayList<String>) Main.getPlugin().getConfig().getStringList("SignLocations");

			for(int i = 0; i <= 1; i++)
			{
				if(i == 1)
				{
					String world = "" + loc.getWorld().getName();
					String xC = " " + loc.getBlockX();
					String yC = " " + loc.getBlockY();
					String zC = " " + loc.getBlockZ() + " ";

					list.add(world + xC + yC + zC + SignCommand.signC.get(p.getName()));

					Main.getPlugin().getConfig().set("SignLocations", list);
					Main.getPlugin().saveConfig();

					SignCommand.signC.remove(e.getPlayer().getName());

					Main.setupSigns();
				}
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{

		if(e.getBlock().getType() == Material.WALL_SIGN)
		{
			ArrayList<String> list = (ArrayList<String>) Main.getPlugin().getConfig().getStringList("SignLocations");

			Location loc = e.getBlock().getLocation();

			String xC = "" + loc.getBlockX();
			String yC = " " + loc.getBlockY();
			String zC = " " + loc.getBlockZ();

			String locS = xC + yC + zC;

			for(String s : list)
			{
				if(s.contains(locS))
				{
					Player p = e.getPlayer();
					
					if(SignCommand.signR.contains(p.getName()))
					{
						SignCommand.signR.remove(p.getName());
						
						list.remove(s);

						Main.getPlugin().getConfig().set("SignLocations", list);
						Main.getPlugin().saveConfig();

						Main.setupSigns();
						
						p.sendMessage(ChatColor.GREEN + "Successfully removed bungee sign!");
						
						break;
					}
					else 
					{
						e.setCancelled(true);
						break;
					}
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

