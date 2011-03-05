package org.bukkit.fillr;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.*;
import java.net.URL;

public class Downloader {
	private final static String DIRECTORY = Fillr.DIRECTORY;
	private final static String DOWNLOAD_DIR = DIRECTORY + File.separator + "downloads";
	private final static String BACKUP = DIRECTORY + File.separator + "backups";

	/**
	 * Downloads the jar from a given url. If it is a compressed archive, it
	 * tries to get the .jars out of it
	 * 
	 * @param url
	 *            The url to download from
	 */
	static void downloadJar(String url) throws Exception {
		int index = url.lastIndexOf('/');
		String name = url.substring(index + 1);

		File file = new File(DIRECTORY, name);
		if (url.endsWith(".jar") && file.exists()) {
			backupFile(file);
		}

		download(new URL(url), name, DIRECTORY);
		file = new File("plugins", name);
	}

	/**
	 * Downloads the file for a given plugin
	 * 
	 * @param name
	 *            The name of the plugin to download
	 * @param player
	 *            The player to send info to
	 */
	void downloadFile(String name, Player player) throws Exception {
		File file = new File(DIRECTORY, name + ".jar");
		if (file.exists()) {
			player.sendMessage("Downloading " + name + "'s file");
			PluginDescriptionFile pdfFile = Checker.getPDF(file);
			FillReader reader = Checker.needsUpdate(pdfFile);
			downloadFile(new URL(reader.getFile()));
			player.sendMessage("Finished download");
		} else {
			System.out.println("Can't find " + name);
		}
	}

	/**
	 * Downloads the file to the plugin/downloads directory
	 * 
	 * @param u
	 *            The url of the file to download
	 */
	private void downloadFile(URL u) throws Exception {
		String name = u.getFile();
		int index = name.lastIndexOf('/');
		name = name.substring(index + 1);
		download(u, name, DOWNLOAD_DIR);
	}

	/**
	 * Downloads the file to a given directory with a given name
	 * 
	 * @param u
	 *            The url of the file to download
	 * @param name
	 *            The name to give the file
	 * @param directory
	 *            The directory to put the file
	 */
	private static void download(URL u, String name, String directory) throws Exception {
		InputStream inputStream = null;
		// try {
		inputStream = u.openStream();

		if (!new File(directory).exists()) {
			new File(directory).mkdir();
		}

		File f = new File(directory, name);
		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();

		copyInputStream(inputStream, new BufferedOutputStream(new FileOutputStream(f)));

		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (IOException ioe) {
			System.out.println("[UPDATR]: Error closing inputStream");
		}
		// }
	}

	/**
	 * Copies an InputStream to an OutputStream!
	 * 
	 * @param in
	 *            InputStream
	 * @param out
	 *            OutputStream
	 * @throws IOException
	 */
	private static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.close();
	}

	/**
	 * Moves the file to the backup folder.
	 * 
	 * @param file
	 *            The file to backup
	 */
	private static void backupFile(File file) {
		if (file != null  && file.exists()) {
			System.out.println("Backing up old file: " + file.getName());
			if (!new File(BACKUP).exists()) {
				new File(BACKUP).mkdir();
			}
			file.renameTo(new File(BACKUP, file.getName() + ".bak"));
		}
	}
}
