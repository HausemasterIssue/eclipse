package xyz.aesthetical.astra.config;

import org.json.JSONObject;
import xyz.aesthetical.astra.managers.FileManager;

import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Configuration {
    private static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();

    protected final Path path;
    protected final FileManager files = FileManager.getInstance();

    public Configuration(Path path) {
        this.path = path;
        SERVICE.scheduleAtFixedRate(this::save, 5, 5, TimeUnit.MINUTES);
    }

    public abstract void save();
    public abstract void load();

    public String read() {
        return FileManager.getInstance().readFile(path);
    }

    public <T> T get(JSONObject object, String key, T defaultValue) {
        if (!object.has(key)) {
            return defaultValue;
        }

        return (T) object.get(key);
    }

    public void shutdown() {
        SERVICE.shutdown();
        save();
    }
}
