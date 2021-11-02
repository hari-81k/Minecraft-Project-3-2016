package net.galaxa.qs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.galaxa.qs.cmds.SignCommand;

public class Main extends JavaPlugin implements PluginMessageListener, Listener
{
	public static Plugin p;

	public static ServerListPing17 c;

	public static ArrayList<Sign> sign2 = new ArrayList<Sign>();
	public static HashMap<Location, String> sign22 = new HashMap<Location, String>();


	public void onEnable()
	{
		p = this;

		c = new ServerListPing17();

		getServer().getPluginManager().registerEvents(new SignEvent(), this);
		getServer().getPluginManager().registerEvents(this, this);

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

		getCommand("qsign").setExecutor(new SignCommand());

		saveDefaultConfig();


		setupSigns();

		refresh();
	}

	public static void setupSigns()
	{

		for(String s : getPlugin().getConfig().getStringList("SignLocations"))
		{
			String[] s2 = s.split(" ");

			Location loc = new Location(Bukkit.getWorld(s2[0]), Integer.parseInt(s2[1]), Integer.parseInt(s2[2]), Integer.parseInt(s2[3]));

			if(Bukkit.getServer().getWorld(s2[0]).getBlockAt(loc).getState() instanceof Sign)
			{
				Sign sign1 = (Sign) Bukkit.getServer().getWorld(s2[0]).getBlockAt(loc).getState();
				sign2.add(sign1);
				sign22.put(loc, s2[4]);
			}
		}
	}


	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		subchannel.equals("SomeSubChannel");
	}

	public static void sendPlayerToServer(Player p, String serverName)
	{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try
		{
			out.writeUTF("Connect");
			out.writeUTF(serverName);
			p.sendPluginMessage(getPlugin(), "BungeeCord", b.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<Sign, Integer> queueP = new HashMap<Sign, Integer>();

	public static void refresh()
	{
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(getPlugin(), new Runnable()
		{
			@SuppressWarnings("deprecation")
			public void run()
			{
				if(Bukkit.getOnlinePlayers().length == 0);
				else {
					for (Sign sign : sign2)
					{
						String line0 = sign22.get(sign.getLocation());
						
						for(int i = 1; i <= 4; i++)
						{
							if(getPlugin().getConfig().contains(line0 + ".SignLayout." + i))
							{
								String line = (getPlugin().getConfig().getString(line0 + ".SignLayout." + i)).replace("&", "§");

								if(line.contains("%queuedPlayers%"))
								{
									int currentP = 0;
									
									for(String s : getPlugin().getConfig().getStringList(line0 + ".Servers"))
									{
										String[] server = s.split(" ");

										String[] ip = server[1].split(":");
										
										int port = Integer.parseInt(ip[1]);

										c.setAddress(new InetSocketAddress(ip[0], port));

										try
										{
											String serverMotd = c.fetchData().getDescription();

											if(serverMotd.contains(line0 + ".JoinableMOTD"))
											{
												int online = c.fetchData().getPlayers().getOnline();

												currentP = currentP + online;
											} else;
										} catch (ConnectException e) {
											System.out.println(line0 + " is offline!");
										} catch (UnknownHostException e) {
											System.out.println("Can not ping " + line0);
										} catch (IOException e) {
											System.out.println("Can not ping " + line0);
										}
									}
									sign.setLine(i - 1, line.replace("%queuedPlayers%", currentP + ""));
								}
								else
								{
									sign.setLine(i - 1, line);
									sign.update();
								}
							} else;
						}
					}
				}
			}
		}, 130L, 16000L);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent ev)
	{
		Player p = ev.getPlayer();

		if(ev.getAction() == Action.RIGHT_CLICK_BLOCK) 
		{
			if(ev.getClickedBlock().getType() == Material.SIGN || ev.getClickedBlock().getType() == Material.SIGN_POST || ev.getClickedBlock().getType() == Material.WALL_SIGN) 
			{	
				Sign sign = (Sign) ev.getClickedBlock().getState();

				String line0 = sign22.get(sign.getLocation());

				String joinableMotd = getConfig().getString(line0 + ".JoinableMOTD");

				for(String s : getConfig().getStringList(line0 + ".Servers"))
				{
					String[] servers = s.split(" ");

					String[] ip = servers[1].split(":");

					String ipN = ip[0];
					int port = Integer.parseInt(ip[1]);

					c.setAddress(new InetSocketAddress(ipN, port));

					try
					{
						String serverMotd = c.fetchData().getDescription();
						int cP = c.fetchData().getPlayers().getOnline();
						int mP = c.fetchData().getPlayers().getMax();

						if(serverMotd.contains(joinableMotd))
						{
							if(cP >= mP);
							else
							{
								sendPlayerToServer(p, servers[0]);
								break;
							}
						} else;

					} catch (ConnectException e) {
						System.out.println(line0 + " is offline!");
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


















	public static Plugin getPlugin() {
		return p;
	}


















































































































}
