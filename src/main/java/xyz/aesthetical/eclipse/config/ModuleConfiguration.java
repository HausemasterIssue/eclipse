package xyz.aesthetical.eclipse.config;

import org.json.JSONObject;
import xyz.aesthetical.eclipse.Eclipse;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.FileManager;
import xyz.aesthetical.eclipse.managers.modules.Module;

public class ModuleConfiguration extends Configuration {
    public ModuleConfiguration() {
        super(FileManager.getInstance().getClientFolder().resolve("modules.json"));
    }

    @Override
    public void save() {
        JSONObject data = new JSONObject();

        for (Module module : Eclipse.moduleManager.getModules()) {
            JSONObject mod = new JSONObject()
                    .put("bind", module.getBind())
                    .put("visible", module.isVisible());

            JSONObject settings = new JSONObject();

            for (Setting setting : module.getSettings()) {
                settings.put(setting.getName().replace("", "_").toLowerCase(), setting.getValue());
            }

            mod.put("settings", settings);

            data.put(module.getName().toLowerCase(), data);
        }

        files.writeFile(path, data.toString(2));
    }

    @Override
    public void load() {
        String data = read();
        if (data == null || data.isEmpty()) {
            save();
            return;
        }

        JSONObject json = new JSONObject(data);
        for (String name : json.keySet()) {
            if (Eclipse.moduleManager.getModule(name) == null || !(json.get(name) instanceof JSONObject)) {
                continue;
            }

            Module module = Eclipse.moduleManager.getModule(name);
            JSONObject mod = json.getJSONObject(name);

            module.setBind(get(mod, "bind", -1));

            if (mod.has("settings")) {
                JSONObject settings = mod.getJSONObject("settings");
                for (String key : settings.keySet()) {
                    for (Setting setting : module.getSettings()) {
                        if (setting.getName().equalsIgnoreCase(key.replaceAll("_", " "))) {
                            setting.setValue(get(settings, key, setting.getValue()));
                        }
                    }
                }
            }
        }
    }
}
