package coloryr.allmusic.side.velocity;

import coloryr.allmusic.bstats.MetricsBase;
import coloryr.allmusic.bstats.charts.CustomChart;
import coloryr.allmusic.bstats.config.MetricsConfig;
import coloryr.allmusic.bstats.json.JsonObjectBuilder;
import com.google.inject.Inject;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MetricsVelocity {
    private final PluginContainer pluginContainer;
    private final ProxyServer server;
    private MetricsBase metricsBase;
    private MetricsVelocity(Object plugin, ProxyServer server, Logger logger, Path dataDirectory, int serviceId) {
        pluginContainer = server.getPluginManager().fromInstance(plugin)
                .orElseThrow(() -> new IllegalArgumentException("The provided instance is not a plugin"));
        this.server = server;

        File configFile = dataDirectory.getParent().resolve("bStats").resolve("config.txt").toFile();
        MetricsConfig config;
        try {
            config = new MetricsConfig(configFile, true);
        } catch (IOException e) {
            logger.error("Failed to create bStats config", e);
            return;
        }

        metricsBase = new MetricsBase(
                "velocity",
                config.getServerUUID(),
                serviceId,
                config.isEnabled(),
                this::appendPlatformData,
                this::appendServiceData,
                task -> server.getScheduler().buildTask(plugin, task).schedule(),
                () -> true,
                logger::warn,
                logger::info,
                config.isLogErrorsEnabled(),
                config.isLogSentDataEnabled(),
                config.isLogResponseStatusTextEnabled()
        );

        if (!config.didExistBefore()) {
            // Send an info message when the bStats config file gets created for the first time
            logger.info("Velocity and some of its plugins collect metrics and send them to bStats (https://bStats.org).");
            logger.info("bStats collects some basic information for plugin authors, like how many people use");
            logger.info("their plugin and their total player count. It's recommend to keep bStats enabled, but");
            logger.info("if you're not comfortable with this, you can opt-out by editing the config.txt file in");
            logger.info("the '/plugins/bStats/' folder and setting enabled to false.");
        }
    }

    /**
     * Adds a custom chart.
     *
     * @param chart The chart to add.
     */
    public void addCustomChart(CustomChart chart) {
        if (metricsBase != null) {
            metricsBase.addCustomChart(chart);
        }
    }

    private void appendPlatformData(JsonObjectBuilder builder) {
        builder.appendField("playerAmount", server.getPlayerCount());
        builder.appendField("managedServers", server.getAllServers().size());
        builder.appendField("onlineMode", server.getConfiguration().isOnlineMode() ? 1 : 0);
        builder.appendField("velocityVersionVersion", server.getVersion().getVersion());
        builder.appendField("velocityVersionName", server.getVersion().getName());
        builder.appendField("velocityVersionVendor", server.getVersion().getVendor());

        builder.appendField("javaVersion", System.getProperty("java.version"));
        builder.appendField("osName", System.getProperty("os.name"));
        builder.appendField("osArch", System.getProperty("os.arch"));
        builder.appendField("osVersion", System.getProperty("os.version"));
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }

    private void appendServiceData(JsonObjectBuilder builder) {
        builder.appendField("pluginVersion", pluginContainer.getDescription().getVersion().orElse("unknown"));
    }

    /**
     * A factory to create new Metrics classes.
     */
    public static class Factory {

        private final ProxyServer server;
        private final Logger logger;
        private final Path dataDirectory;

        // The constructor is not meant to be called by the user.
        // The instance is created using Dependency Injection
        @Inject
        private Factory(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
            this.server = server;
            this.logger = logger;
            this.dataDirectory = dataDirectory;
        }

        /**
         * Creates a new Metrics class.
         *
         * @param plugin    The plugin instance.
         * @param serviceId The id of the service.
         *                  It can be found at <a href="https://bstats.org/what-is-my-plugin-id">What is my plugin id?</a>
         *                  <p>Not to be confused with Velocity's {@link PluginDescription#getId()} method!
         * @return A Metrics instance that can be used to register custom charts.
         * <p>The return value can be ignored, when you do not want to register custom charts.
         */
        public MetricsVelocity make(Object plugin, int serviceId) {
            return new MetricsVelocity(plugin, server, logger, dataDirectory, serviceId);
        }
    }

}
