package org.bukkit.fillr;

import org.bukkit.*;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader {
	private static String directory = Fillr.directory;
	private static String downloads = directory + File.separator + "downloads";
	private static String backup = "backup";

	/**
	 * Downloads the jar from a given url. If it is a compressed archive, it
	 * tries to get the .jars out of it
	 * 
	 * @param url
	 *            The url to download from
	 */
	public static void downloadJar(String url) throws Exception {
		int index = url.lastIndexOf('/');
		String name = url.substring(index + 1);

		File file = new File(directory, name);
		if (url.endsWith(".jar") && file.exists())
			backupFile(file);

		download(new URL(url), name, directory);
		file = new File("plugins", name);
		/*if (name.endsWith(".zip") || name.endsWith(".tar")
				|| name.endsWith(".rar") || name.endsWith(".7z")) {
			unzipPlugin(file);
			file.delete();
		}*/
	}

	/**
	 * Downloads the file for a given plugin (if it's updatr file exists);
	 * 
	 * @param name
	 *            The name of the plugin to download
	 * @param player
	 *            The player to send info to
	 */
	public void downloadFile(String name, Player player) throws Exception {
		File file = new File(directory, name + ".jar");
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
	 * Downloads the file to the Updatr/downloads directory
	 * 
	 * @param u
	 *            The url of the file to download
	 */
	private void downloadFile(URL u) throws Exception {
		String name = u.getFile();
		int index = name.lastIndexOf('/');
		name = name.substring(index + 1);
		download(u, name, downloads);
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
	private static void download(URL u, String name, String directory)
			throws Exception {
		InputStream inputStream = null;
		// try {
		inputStream = u.openStream();

		if (!new File(directory).exists())
			new File(directory).mkdir();

		File f = new File(directory, name);
		if (f.exists())
			f.delete();
		f.createNewFile();

		copyInputStream(inputStream, new BufferedOutputStream(
				new FileOutputStream(f)));

		try {
			if (inputStream != null)
				inputStream.close();
		} catch (IOException ioe) {
			System.out.println("[UPDATR]: Error closing inputStream");
		}
		// }
	}

	/**
	 * Decompresses a file! How nice.
	 * 
	 * @param f
	 *            the file to decompress
	 */
	private static void unzipPlugin(File f) {
		try {
			System.out.println("Extracting jars out of " + f.getName());
			//ExtractorUtil.extract(f, f.getAbsolutePath());
		} catch (Exception e) {
			System.out.println("[UPDATR]: Error decompressing " + f.getName());
		}
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
	public static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	/**
	 * Downloads the new updatr file
	 * 
	 * @param url
	 *            The url pointing to the updatr file
	 * @param name
	 *            The name of the plugin
	 */
	public static void downloadUpdatr(String url, String name) throws Exception {
		// try {
		download(new URL(url), name + ".updatr", Fillr.directory);
	}

	/**
	 * Moves the file to the backup folder.
	 * 
	 * @param file
	 *            The file to backup
	 */
	public static void backupFile(File file) {
		if (file.exists()) {
			System.out.println("Backing up old file: " + file.getName());
			if (!new File(backup).exists())
				new File(backup).mkdir();
			file.renameTo(new File(backup, file
					.getName() + ".bak"));
		}
	}

}