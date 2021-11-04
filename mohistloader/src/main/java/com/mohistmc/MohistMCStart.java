package com.mohistmc;

import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.libraries.CustomLibraries;
import com.mohistmc.libraries.DefaultLibraries;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.util.FileUtils;
import com.mohistmc.util.JarTool;
import com.mohistmc.util.OSUtils;
import com.mohistmc.util.i18n.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.mohistmc.util.EulaUtil.hasAcceptedEULA;
import static com.mohistmc.util.EulaUtil.writeInfos;
import static com.mohistmc.util.InstallUtils.startInstallation;

public class MohistMCStart {

    public static String getMohistVersion() {
        return FileUtils.readVersionFromInputStream(MohistMCStart.class.getClassLoader().getResourceAsStream("versions/mohist.txt")).replaceAll(" ", "");
    }
    public static String getMinecraftVersion() {
        return FileUtils.readVersionFromInputStream(MohistMCStart.class.getClassLoader().getResourceAsStream("versions/minecraft.txt")).replaceAll(" ", "");
    }
    public static String getForgeVersion() {
        return FileUtils.readVersionFromInputStream(MohistMCStart.class.getClassLoader().getResourceAsStream("versions/forge.txt")).replaceAll(" ", "");
    }
    public static String getMCPVersion() {
        return FileUtils.readVersionFromInputStream(MohistMCStart.class.getClassLoader().getResourceAsStream("versions/mcp.txt")).replaceAll(" ", "");
    }

    public static void main(String[] args) throws Exception {
        MohistConfigUtil.copyMohistConfig();

        if (MohistConfigUtil.bMohist("show_logo", "true"))
            System.out.println("\n" + "\n" +
                    " __    __   ______   __  __   __   ______   ______  \n" +
                    "/\\ \"-./  \\ /\\  __ \\ /\\ \\_\\ \\ /\\ \\ /\\  ___\\ /\\__  _\\ \n" +
                    "\\ \\ \\-./\\ \\\\ \\ \\/\\ \\\\ \\  __ \\\\ \\ \\\\ \\___  \\\\/_/\\ \\/ \n" +
                    " \\ \\_\\ \\ \\_\\\\ \\_____\\\\ \\_\\ \\_\\\\ \\_\\\\/\\_____\\  \\ \\_\\ \n" +
                    "  \\/_/  \\/_/ \\/_____/ \\/_/\\/_/ \\/_/ \\/_____/   \\/_/ \n" +
                    "                                                    \n" + "\n" +
                    "                                      "
                    + i18n.get("mohist.launch.welcomemessage"));
        if (MohistConfigUtil.bMohist("check_libraries", "true")) {
            DefaultLibraries.run();
            startInstallation();
        }
        CustomLibraries.loadCustomLibs();
        //new JarLoader().loadJar(InstallUtils.extra);

        if (MohistConfigUtil.bMohist("check_update", "true")) UpdateUtils.versionCheck();
        if (!hasAcceptedEULA()) {
            System.out.println(i18n.get("eula"));
            while (!"true".equals(new Scanner(System.in).next())) ;
            writeInfos();
        }

    }
}
