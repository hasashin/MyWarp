package de.imolli.mywarp;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.logging.Level;

public class UpdateChecker {

    private static Boolean updateAvailable;
    private static Boolean currentlyChecking;
    private static final Integer versionNumber = 12;
    private static Integer newVersionNumber;
    private static String newVersionString;

    //TODO: Make async and delayed

    public static void init() {

        updateAvailable = false;
        currentlyChecking = false;
        newVersionNumber = 0;
        newVersionString = "";

    }

    public static void checkForUpdate() {

        if (!MyWarp.getUpdateCheck()) {
            MyWarp.getPlugin().getLogger().log(Level.INFO, "Skipped update check, because it's disabled in the config.");
            return;
        }
        if (currentlyChecking) return;

        currentlyChecking = true;

        MyWarp.getPlugin().getLogger().log(Level.INFO, "Checking for updates...");

        try {
            //SSLContext sc = SSLContext.getInstance("SSL");
            //sc.init(null, getTrustManager(), new java.security.SecureRandom());
            //HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            //HttpsURLConnection.setDefaultHostnameVerifier(getHostname());

            URL url = new URL("http://imolli.de/uc/mywarp.php?key=Nepu=aDeP5use5r=ba3ar@k2c2AD-Cha");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream answerinputstream = connection.getInputStream();
            String[] answer = getTextFromInputStream(answerinputstream).split(":");

            newVersionNumber = Integer.parseInt(answer[0]);
            newVersionString = answer[1];

            if (versionNumber < newVersionNumber) {
                updateAvailable = true;

                Integer behind = newVersionNumber - versionNumber;

                MyWarp.getPlugin().getLogger().log(Level.WARNING, "A new update of MyWarp is available(v" + newVersionString + "). You are currently on " + MyWarp.getPlugin().getDescription().getVersion() + ", " + behind + " versions behind.");

            } else {
                updateAvailable = false;

                MyWarp.getPlugin().getLogger().log(Level.INFO, "No update for MyWarp was found.");
            }

        } catch (IOException e) {
            //e.printStackTrace();
            MyWarp.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while checking for a update.");
        }

        currentlyChecking = false;

    }

    public static void notifyPlayer(Player p) {
        if (updateAvailable && MyWarp.getUpdateNotify()) {
            if (p.isOp() || p.hasPermission("MyWarp.notify.update")) {

                Integer behind = newVersionNumber - versionNumber;

                Bukkit.getScheduler().runTaskLater(MyWarp.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        p.sendMessage(" ");
                        p.sendMessage(MyWarp.getPrefix() + "§7A new update of §aMyWarp §7is available(§ev" + newVersionString + "§7). You are currently on §e" + MyWarp.getPlugin().getDescription().getVersion() + "§7, §e" + behind + "§7 versions behind.");

                        TextComponent spacer = new TextComponent(MyWarp.getPrefix());

                        TextComponent component = new TextComponent("§6Download!");
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§eClick to open the §aMyWarp §edownload page!")));
                        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/mywarp.34974/"));

                        spacer.addExtra(component);

                        p.spigot().sendMessage(spacer);

                    }
                }, 20);
            }
        }
    }

    public static String getTextFromInputStream(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringbuiler = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringbuiler.append(line);
                stringbuiler.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringbuiler.toString().trim();
    }

    public static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };
        return trustAllCerts;
    }

    public static HostnameVerifier getHostname() {
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        return allHostsValid;
    }

    public static Boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public static Boolean isCurrentlyChecking() {
        return currentlyChecking;
    }
}
