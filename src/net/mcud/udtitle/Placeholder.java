package net.mcud.udtitle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Placeholder extends PlaceholderExpansion {
	private final Title plugin;

	public Placeholder(Title plugin) {
		this.plugin = plugin;
	}

	public String getAuthor() {
		return "lazycat";
	}

	public String getIdentifier() {
		return "udtitle";
	}

	public String onRequest(OfflinePlayer player, String identifier) {
		if (identifier.toLowerCase().startsWith("title")) {
			if (identifier.toLowerCase().startsWith("title_")) {
				String playername = identifier.substring("title_".length());
				if (this.isMatchPlayerName(playername)) {
					return plugin.data.getPlayerTag(playername).replace("&", "§");
				}
			} else {
				if (player != null) {
					return plugin.data.getPlayerTag(player.getName()).replace("&", "§");
				}
			}
			return "";
		}
		return identifier;
	}

	public String getVersion() {
		return this.plugin.getDescription().getVersion();
	}

	public boolean persist() {
		return true;
	}

	public boolean canRegister() {
		return true;
	}

	public boolean isMatchPlayerName(String player) {
		Pattern p = Pattern.compile("[a-zA-Z0-9_]*{3,16}");
		Matcher m = p.matcher(player);
		return m.matches();
	}
}
