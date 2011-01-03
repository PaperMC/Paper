package org.bukkit.fillr;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Used to filter out non-updatr files
 */
public class PluginFilter implements FilenameFilter {

	public boolean accept(File file, String name) {
		if(name.endsWith(".jar"))
			return true;
		else
			return false;
	}

}
