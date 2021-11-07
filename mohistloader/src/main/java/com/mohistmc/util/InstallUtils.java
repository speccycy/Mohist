package com.mohistmc.util;

import com.mohistmc.MohistMCStart;
import com.mohistmc.util.i18n.i18n;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;

public class InstallUtils {
    private static final PrintStream origin = System.out;
    public static String mohistVer = MohistMCStart.getMohistVersion();
    public static String forgeVer = MohistMCStart.getForgeVersion();
    public static String mcpVer = MohistMCStart.getMCPVersion();
    public static String mcVer = MohistMCStart.getMinecraftVersion();
    public static String libPath = JarTool.getJarDir() + "/libraries/";

    public static String forgeStart = libPath + "net/minecraftforge/forge/" + mcVer + "-" + forgeVer + "/forge-" + mcVer + "-" + forgeVer;
    public static File universalJar = new File(forgeStart + "-universal.jar");
    public static File serverJar = new File(forgeStart + "-server.jar");


    public static File fmlloader = new File(libPath + "net/minecraftforge/fmlloader/" + mcVer + "-" + forgeVer + "/fmlloader-" + mcVer + "-" + forgeVer + ".jar");
    public static File fmlcore = new File(libPath + "net/minecraftforge/fmlcore/" + mcVer + "-" + forgeVer + "/fmlcore-" + mcVer + "-" + forgeVer + ".jar");
    public static File javafmllanguage = new File(libPath + "net/minecraftforge/javafmllanguage/" + mcVer + "-" + forgeVer + "/javafmllanguage-" + mcVer + "-" + forgeVer + ".jar");
    public static File mclanguage = new File(libPath + "net/minecraftforge/mclanguage/" + mcVer + "-" + forgeVer + "/mclanguage-" + mcVer + "-" + forgeVer + ".jar");
    public static File mohistloader = new File(libPath + "net/minecraftforge/mohistloader/" + mcVer + "-" + mohistVer + "/mohistloader-" + mcVer + "-" + mohistVer + ".jar");

    public static File lzma = new File(libPath + "com/mohistmc/installation/data/server.lzma");
    public static File installInfo = new File(libPath + "com/mohistmc/installation/installInfo");

    public static String otherStart = libPath + "net/minecraft/server/" + mcVer + "-" + mcpVer + "/server-" + mcVer + "-" + mcpVer;
    public static File mojmap = new File(otherStart + "-mappings.txt");
    public static File extra = new File(otherStart + "-extra.jar");
    public static File slim = new File(otherStart + "-slim.jar");
    public static File srg = new File(otherStart + "-srg.jar");

    public static String mcpStart = libPath + "de/oceanlabs/mcp/mcp_config/" + mcVer + "-" + mcpVer + "/mcp_config-" + mcVer + "-" + mcpVer;
    public static File mcpZip = new File(mcpStart + ".zip");
    public static File mcpTxt = new File(mcpStart + "-mappings.txt");
    public static File mergedMapping = new File(mcpStart + "-mappings-merged.txt");

    public static void startInstallation() throws Exception {
        System.out.println(i18n.get("installation.start"));
        copyFileFromJar(lzma, "data/server.lzma");
        copyFileFromJar(universalJar, "data/forge-" + mcVer + "-" + forgeVer + "-universal.jar");
        copyFileFromJar(fmlloader, "data/fmlloader-" + mcVer + "-" + forgeVer + ".jar");
        copyFileFromJar(fmlcore, "data/fmlcore-" + mcVer + "-" + forgeVer + ".jar");
        copyFileFromJar(javafmllanguage, "data/javafmllanguage-" + mcVer + "-" + forgeVer + ".jar");
        copyFileFromJar(mclanguage, "data/mclanguage-" + mcVer + "-" + forgeVer + ".jar");
        copyFileFromJar(mohistloader, "data/mohistloader-" + mcVer + "-" + mohistVer + ".jar");

        if(mohistVer == null || mcpVer == null) {
            System.out.println("[Mohist] There is an error with the installation, the forge / mcp version is not set.");
            System.exit(0);
        }

        if(mcpZip.exists()) {
            if(!mcpTxt.exists()) {

                // MAKE THE MAPPINGS TXT FILE

                System.out.println(i18n.get("installation.mcp"));
                mute();
                run("net.minecraftforge.installertools.ConsoleTool", new ArrayList<>(Arrays.asList("--task", "MCP_DATA", "--input", mcpZip.getAbsolutePath(), "--output", mcpTxt.getAbsolutePath(), "--key", "mappings")), stringToUrl(new ArrayList<>(Arrays.asList(libPath + "net/minecraftforge/installertools/1.2.7/installertools-1.2.7.jar", libPath + "net/md-5/SpecialSource/1.10.0/SpecialSource-1.10.0.jar", libPath + "net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar", libPath + "com/google/code/gson/gson/2.8.7/gson-2.8.7.jar", libPath + "de/siegmar/fastcsv/2.0.0/fastcsv-2.0.0.jar", libPath + "net/minecraftforge/srgutils/0.4.3/srgutils-0.4.3.jar", libPath + "org/ow2/asm/asm-commons/9.1/asm-commons-9.1.jar", libPath + "com/google/guava/guava/20.0/guava-20.0.jar", libPath + "com/opencsv/opencsv/4.4/opencsv-4.4.jar", libPath + "org/ow2/asm/asm-analysis/9.1/asm-analysis-9.1.jar", libPath + "org/ow2/asm/asm-tree/9.1/asm-tree-9.1.jar", libPath + "org/ow2/asm/asm/9.1/asm-9.1.jar", libPath + "org/apache/commons/commons-text/1.3/commons-text-1.3.jar", libPath + "org/apache/commons/commons-lang3/3.8.1/commons-lang3-3.8.1.jar", libPath + "commons-beanutils/commons-beanutils/1.9.3/commons-beanutils-1.9.3.jar", libPath + "org/apache/commons/commons-collections4/4.2/commons-collections4-4.2.jar", libPath + "commons-logging/commons-logging/1.2/commons-logging-1.2.jar", libPath + "commons-collections/commons-collections/3.2.2/commons-collections-3.2.2.jar"))));
                unmute();
            }
        } else {
            System.out.println(i18n.get("installation.mcpfilemissing"));
            System.exit(0);
        }

        if(isCorrupted(extra)) extra.delete();
        if(isCorrupted(slim)) slim.delete();
        if(isCorrupted(srg)) srg.delete();

        if(!mojmap.exists()) {
            System.out.println(i18n.get("installation.mojmap"));
            mute();
            run("net.minecraftforge.installertools.ConsoleTool", new ArrayList<>(Arrays.asList("--task", "DOWNLOAD_MOJMAPS", "--version", mcVer, "--side", "server", "--output", mojmap.getAbsolutePath())), stringToUrl(new ArrayList<>(Arrays.asList(libPath + "net/minecraftforge/installertools/1.2.7/installertools-1.2.7.jar", libPath + "net/md-5/SpecialSource/1.10.0/SpecialSource-1.10.0.jar", libPath + "net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar", libPath + "com/google/code/gson/gson/2.8.7/gson-2.8.7.jar", libPath + "de/siegmar/fastcsv/2.0.0/fastcsv-2.0.0.jar", libPath + "net/minecraftforge/srgutils/0.4.3/srgutils-0.4.3.jar", libPath + "org/ow2/asm/asm-commons/9.1/asm-commons-9.1.jar", libPath + "com/google/guava/guava/20.0/guava-20.0.jar", libPath + "com/opencsv/opencsv/4.4/opencsv-4.4.jar", libPath + "org/ow2/asm/asm-analysis/9.1/asm-analysis-9.1.jar", libPath + "org/ow2/asm/asm-tree/9.1/asm-tree-9.1.jar", libPath + "org/ow2/asm/asm/9.1/asm-9.1.jar", libPath + "org/apache/commons/commons-text/1.3/commons-text-1.3.jar", libPath + "org/apache/commons/commons-lang3/3.8.1/commons-lang3-3.8.1.jar", libPath + "commons-beanutils/commons-beanutils/1.9.3/commons-beanutils-1.9.3.jar", libPath + "org/apache/commons/commons-collections4/4.2/commons-collections4-4.2.jar", libPath + "commons-logging/commons-logging/1.2/commons-logging-1.2.jar", libPath + "commons-collections/commons-collections/3.2.2/commons-collections-3.2.2.jar"))));
            unmute();
        }

        if (!mergedMapping.exists()) {
            System.out.println(i18n.get("installation.mergedmapping"));
            mute();
            run("net.minecraftforge.installertools.ConsoleTool", new ArrayList<>(Arrays.asList("--task", "MERGE_MAPPING", "--left", mcpTxt.getAbsolutePath(), "--right", mojmap.getAbsolutePath(), "--output", mergedMapping.getAbsolutePath(), "--classes", "--reverse-right")), stringToUrl(new ArrayList<>(Arrays.asList(libPath + "net/minecraftforge/installertools/1.2.7/installertools-1.2.7.jar", libPath + "net/md-5/SpecialSource/1.10.0/SpecialSource-1.10.0.jar", libPath + "net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar", libPath + "com/google/code/gson/gson/2.8.7/gson-2.8.7.jar", libPath + "de/siegmar/fastcsv/2.0.0/fastcsv-2.0.0.jar", libPath + "net/minecraftforge/srgutils/0.4.3/srgutils-0.4.3.jar", libPath + "org/ow2/asm/asm-commons/9.1/asm-commons-9.1.jar", libPath + "com/google/guava/guava/20.0/guava-20.0.jar", libPath + "com/opencsv/opencsv/4.4/opencsv-4.4.jar", libPath + "org/ow2/asm/asm-analysis/9.1/asm-analysis-9.1.jar", libPath + "org/ow2/asm/asm-tree/9.1/asm-tree-9.1.jar", libPath + "org/ow2/asm/asm/9.1/asm-9.1.jar", libPath + "org/apache/commons/commons-text/1.3/commons-text-1.3.jar", libPath + "org/apache/commons/commons-lang3/3.8.1/commons-lang3-3.8.1.jar", libPath + "commons-beanutils/commons-beanutils/1.9.3/commons-beanutils-1.9.3.jar", libPath + "org/apache/commons/commons-collections4/4.2/commons-collections4-4.2.jar", libPath + "commons-logging/commons-logging/1.2/commons-logging-1.2.jar", libPath + "commons-collections/commons-collections/3.2.2/commons-collections-3.2.2.jar"))));
            unmute();
        }

        if(!slim.exists() || !extra.exists()) {
            System.out.println(i18n.get("installation.jars"));
            mute();
            run("net.minecraftforge.jarsplitter.ConsoleTool", new ArrayList<>(Arrays.asList("--input", libPath + "minecraft_server." + mcVer + ".jar", "--slim", slim.getAbsolutePath(), "--extra", extra.getAbsolutePath(), "--srg", mergedMapping.getAbsolutePath())), stringToUrl(new ArrayList<>(Arrays.asList(libPath + "net/minecraftforge/jarsplitter/1.1.4/jarsplitter-1.1.4.jar", libPath + "net/minecraftforge/srgutils/0.4.3/srgutils-0.4.3.jar", libPath + "net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar"))));
            unmute();
        }

        if(!srg.exists()) {
            System.out.println(i18n.get("installation.srgjar"));
            mute();
            run("org.cadixdev.vignette.VignetteMain", new ArrayList<>(Arrays.asList("--jar-in", slim.getAbsolutePath(), "--jar-out", srg.getAbsolutePath(), "--mapping-format", "tsrg2", "--mappings", mergedMapping.getAbsolutePath(), "--create-inits", "--fix-param-annotations", "--fernflower-meta")), stringToUrl(new ArrayList<>(Arrays.asList(libPath + "net/minecraftforge/lex/vignette/0.2.0.16-fix/vignette-0.2.0.16-fix.jar", libPath + "org/cadixdev/atlas/0.2.2/atlas-0.2.2.jar", libPath + "org/cadixdev/lorenz-asm/0.5.7/lorenz-asm-0.5.7.jar", libPath + "org/cadixdev/lorenz/0.5.7/lorenz-0.5.7.jar", libPath + "net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar", libPath + "org/cadixdev/bombe-asm/0.3.5/bombe-asm-0.3.5.jar", libPath + "org/ow2/asm/asm-commons/9.1/asm-commons-9.1.jar", libPath + "com/google/jimfs/jimfs/1.2/jimfs-1.2.jar", libPath + "org/cadixdev/bombe/0.3.5/bombe-0.3.5.jar", libPath + "org/ow2/asm/asm-analysis/9.1/asm-analysis-9.1.jar", libPath + "org/ow2/asm/asm-tree/9.1/asm-tree-9.1.jar", libPath + "org/ow2/asm/asm/9.1/asm-9.1.jar", libPath + "com/google/guava/guava/30.1-android/guava-30.1-android.jar", libPath + "com/google/guava/failureaccess/1.0.1/failureaccess-1.0.1.jar", libPath + "com/google/guava/listenablefuture/9999.0-empty-to-avoid-conflict-with-guava/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar", libPath + "com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar", libPath + "org/checkerframework/checker-compat-qual/2.5.5/checker-compat-qual-2.5.5.jar", libPath + "com/google/errorprone/error_prone_annotations/2.3.4/error_prone_annotations-2.3.4.jar", libPath + "com/google/j2objc/j2objc-annotations/1.3/j2objc-annotations-1.3.jar"))));
            unmute();
        }

        String storedServerMD5 = null;
        String storedMohistMD5 = null;
        String serverMD5 = MD5Util.getMd5(serverJar);
        String mohistMD5 = MD5Util.getMd5(new File(MohistMCStart.class.getProtectionDomain().getCodeSource().getLocation().toURI()));

        if(installInfo.exists()) {
            List<String> infoLines = Files.readAllLines(installInfo.toPath());
            if(infoLines.size() > 0)
                storedServerMD5 = infoLines.get(0);
            if(infoLines.size() > 1)
                storedMohistMD5 = infoLines.get(1);
        }

        if(!serverJar.exists()
                || storedServerMD5 == null
                || storedMohistMD5 == null
                || !storedServerMD5.equals(serverMD5)
                || !storedMohistMD5.equals(mohistMD5)) {
            System.out.println(i18n.get("installation.forgejar"));
            mute();
            run("net.minecraftforge.binarypatcher.ConsoleTool", new ArrayList<>(Arrays.asList("--clean", srg.getAbsolutePath(), "--output", serverJar.getAbsolutePath(), "--apply", lzma.getAbsolutePath())), stringToUrl(new ArrayList<>(Arrays.asList(libPath + "net/minecraftforge/binarypatcher/1.0.12/binarypatcher-1.0.12.jar", libPath + "commons-io/commons-io/2.4/commons-io-2.4.jar", libPath + "com/google/guava/guava/25.1-jre/guava-25.1-jre.jar", libPath + "net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar", libPath + "com/github/jponge/lzma-java/1.3/lzma-java-1.3.jar", libPath + "com/nothome/javaxdelta/2.0.1/javaxdelta-2.0.1.jar", libPath + "com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar", libPath + "org/checkerframework/checker-qual/2.0.0/checker-qual-2.0.0.jar", libPath + "com/google/errorprone/error_prone_annotations/2.1.3/error_prone_annotations-2.1.3.jar", libPath + "com/google/j2objc/j2objc-annotations/1.1/j2objc-annotations-1.1.jar", libPath + "org/codehaus/mojo/animal-sniffer-annotations/1.14/animal-sniffer-annotations-1.14.jar", libPath + "trove/trove/1.0.2/trove-1.0.2.jar"))));
            unmute();
            serverMD5 = MD5Util.getMd5(serverJar);
        }

        FileWriter fw = new FileWriter(installInfo);
        fw.write(serverMD5 + "\n");
        fw.write(mohistMD5);
        fw.close();

        System.out.println(i18n.get("installation.finished"));
    }

    private static void run(String mainClass, List<String> args, List<URL> classPath) throws Exception {
        Class.forName(mainClass, true, new URLClassLoader(classPath.toArray(new URL[classPath.size()]), getParentClassloader())).getDeclaredMethod("main", String[].class).invoke(null, (Object) args.toArray(new String[args.size()]));
    }

    private static ClassLoader getParentClassloader() {
        try {
            return (ClassLoader) ClassLoader.class.getDeclaredMethod("getPlatformClassLoader").invoke(null);
        } catch (Exception e) {
            return null;
        }
    }

    private static List<URL> stringToUrl(List<String> strs) throws Exception {
        List<URL> temp = new ArrayList<>();
        for (String t : strs)
            temp.add(new File(t).toURI().toURL());
        return temp;
    }

    /*
    THIS IS TO NOT SPAM CONSOLE WHEN IT WILL PRINT A LOT OF THINGS
     */
    private static void mute() throws Exception {
        File out = new File(libPath + "com/mohistmc/installation/installationLogs.txt");
        if(!out.exists()) {
            out.getParentFile().mkdirs();
            out.createNewFile();
        }
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(out))));
    }

    private static void unmute() {
        System.setOut(origin);
    }

    private static void copyFileFromJar(File file, String pathInJar) throws Exception {
        InputStream is = MohistMCStart.class.getClassLoader().getResourceAsStream(pathInJar);
        if(!file.exists() || !MD5Util.getMd5(file).equals(MD5Util.getMd5(is)) || file.length() <= 1) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            if(is != null) Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            else {
                System.out.println("[Mohist] The file " + file.getName() + " doesn't exists in the Mohist jar !");
                System.exit(0);
            }
        }
    }

    private static boolean isCorrupted(File f) {
        try {
            JarFile j = new JarFile(f);
            j.close();
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}