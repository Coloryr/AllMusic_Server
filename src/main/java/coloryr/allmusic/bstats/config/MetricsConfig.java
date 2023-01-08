package coloryr.allmusic.bstats.config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MetricsConfig {
    private final File file;
    private final boolean defaultEnabled;

    private String serverUUID;
    private boolean enabled;
    private boolean logErrors;
    private boolean logSentData;
    private boolean logResponseStatusText;

    private boolean didExistBefore = true;

    public MetricsConfig(File file, boolean defaultEnabled) throws IOException {
        this.file = file;
        this.defaultEnabled = defaultEnabled;

        setupConfig();
    }

    public String getServerUUID() {
        return serverUUID;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isLogErrorsEnabled() {
        return logErrors;
    }

    public boolean isLogSentDataEnabled() {
        return logSentData;
    }

    public boolean isLogResponseStatusTextEnabled() {
        return logResponseStatusText;
    }

    /**
     * Checks whether the config file did exist before or not.
     *
     * @return If the config did exist before.
     */
    public boolean didExistBefore() {
        return didExistBefore;
    }

    /**
     * Creates the config file if it does not exist and read its content.
     */
    private void setupConfig() throws IOException {
        if (!file.exists()) {
            didExistBefore = false; // Looks like it's the first time we create it (or someone deleted it).
            writeConfig();
        }
        readConfig();
        if (serverUUID == null) {
            // Found a malformed config file with no UUID. Let's recreate it.
            writeConfig();
            readConfig();
        }
    }

    /**
     * Creates a config file with teh default content.
     */
    private void writeConfig() throws IOException {
        List<String> configContent = new ArrayList<>();
        configContent.add("# bStats (https://bStats.org) collects some basic information for plugin authors, like");
        configContent.add("# how many people use their plugin and their total player count. It's recommended to keep");
        configContent.add("# bStats enabled, but if you're not comfortable with this, you can turn this setting off.");
        configContent.add("# There is no performance penalty associated with having metrics enabled, and data sent to");
        configContent.add("# bStats is fully anonymous.");
        configContent.add("enabled=" + defaultEnabled);
        configContent.add("server-uuid=" + UUID.randomUUID());
        configContent.add("log-errors=false");
        configContent.add("log-sent-data=false");
        configContent.add("log-response-status-text=false");
        writeFile(file, configContent);
    }

    /**
     * Reads the content of the config file.
     */
    private void readConfig() throws IOException {
        List<String> lines = readFile(file);
        if (lines == null) {
            throw new AssertionError("Content of newly created file is null");
        }

        enabled = getConfigValue("enabled", lines).map("true"::equals).orElse(true);
        serverUUID = getConfigValue("server-uuid", lines).orElse(null);
        logErrors = getConfigValue("log-errors", lines).map("true"::equals).orElse(false);
        logSentData = getConfigValue("log-sent-data", lines).map("true"::equals).orElse(false);
        logResponseStatusText = getConfigValue("log-response-status-text", lines).map("true"::equals).orElse(false);
    }

    /**
     * Gets a config setting from the given list of lines of the file.
     *
     * @param key   The key for the setting.
     * @param lines The lines of the file.
     * @return The value of the setting.
     */
    private Optional<String> getConfigValue(String key, List<String> lines) {
        return lines.stream()
                .filter(line -> line.startsWith(key + "="))
                .map(line -> line.replaceFirst(Pattern.quote(key + "="), ""))
                .findFirst();
    }

    /**
     * Reads the text content of the given file.
     *
     * @param file The file to read.
     * @return The lines of the given file.
     */
    private List<String> readFile(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        try (
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader)
        ) {
            return bufferedReader.lines().collect(Collectors.toList());
        }
    }

    /**
     * Writes the given lines to the given file.
     *
     * @param file  The file to write to.
     * @param lines The lines to write.
     */
    private void writeFile(File file, List<String> lines) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        try (
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
        ) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
    }
}
