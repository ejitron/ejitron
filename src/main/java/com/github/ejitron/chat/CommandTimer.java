package com.github.ejitron.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CommandTimer {
	public static List<CommandCooldown> cooldownList = new ArrayList<CommandCooldown>();
	
	public static void startCooldown() {
		cooldownTimer();
	}
	
	public static void addToCooldown(String channel, String command) {
		cooldownList.add(new CommandCooldown(channel, command, 7));
	}
	
	public static boolean isInCooldown(String channel, String command) {
		for(int i = 0; i < cooldownList.size(); i++) {
			String curChannel = cooldownList.get(i).getChannel();
			String curCommand = cooldownList.get(i).getCommand();
			
			if(curChannel.equalsIgnoreCase(channel) && curCommand.equalsIgnoreCase(command))
				return true;
		}
		
		return false;
	}
	
	private static void cooldownTimer() {
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if(cooldownList.size() > 1)
					return;
				
				for(int i = 0; i < cooldownList.size(); i++) {
					String channel = cooldownList.get(i).getChannel();
					String command = cooldownList.get(i).getCommand();
					int cooldown = cooldownList.get(i).getCooldown();
					
					if(cooldown > 1) {
						cooldown--;
						cooldownList.set(i, new CommandCooldown(channel, command, cooldown));
					} else if(cooldown == 1) {
						cooldownList.remove(i);
					}
				}
			}
		}, 1000, 1000);
	}
}
