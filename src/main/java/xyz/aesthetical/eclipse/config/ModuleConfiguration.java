package xyz.aesthetical.eclipse.config;

import org.json.JSONObject;
import xyz.aesthetical.eclipse.features.settings.Setting;
import xyz.aesthetical.eclipse.managers.FileManager;
import xyz.aesthetical.eclipse.managers.modules.Module;
import xyz.aesthetical.eclipse.managers.modules.ModuleManager;
import xyz.aesthetical.eclipse.util.EnumConverter;

import java.awt.*;

public class ModuleConfiguration extends Configuration {
    private final ModuleManager moduleManager;

    public ModuleConfiguration(ModuleManager manager) {
        super(FileManager.getInstance().getClientFolder().resolve("modules.json"));

        System.out.println(path.toString());

        this.moduleManager = manager;
    }

    @Override
    public void save() {
        JSONObject data = new JSONObject();

        for (Module module : moduleManager.getModules()) {
            JSONObject mod = new JSONObject()
                    .put("bind", module.getBind())
                    .put("toggled", module.isToggled());

            JSONObject settings = new JSONObject();

            for (Setting setting : module.getSettings()) {
                String name = setting.getName().replace(" ", "_").toLowerCase();

                if (setting.getValue() instanceof Enum) {
                    settings.put(name, ((Enum<?>) setting.getValue()).name());
                } else {
                    settings.put(name, setting.getValue());
                }
            }

            mod.put("settings", settings);
            data.put(module.getName().toLowerCase(), mod);
        }

        files.writeFile(path, data.toString(4));
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
            if (moduleManager.getModule(name) == null || !(json.get(name) instanceof JSONObject)) {
                continue;
            }

            Module module = moduleManager.getModule(name);
            JSONObject mod = json.getJSONObject(name);

            module.setBind(get(mod, "bind", -1));

            if (get(mod, "toggled", false)) {
                module.toggle();
            }

            if (mod.has("settings")) {
                JSONObject settings = mod.getJSONObject("settings");
                for (String key : settings.keySet()) {
                    loop: for (Setting setting : module.getSettings()) {
                        if (setting.getName().equalsIgnoreCase(key.replaceAll("_", " "))) {
                            Object value = get(settings, key, setting.getValue());

                            if (setting.getValue() instanceof Enum) {
                                setting.setValue(new EnumConverter(((Enum<?>) setting.getValue()).getClass()).doBackward((String) value));
                                continue;
                            } else if (setting.getValue() instanceof Color) {
                                // @todo
                                continue;
                            } else if (setting.getValue() instanceof Integer) {
                                setting.setValue(this.<Integer>get(settings, key, (Integer) setting.getValue()));
                            } else if (setting.getValue() instanceof Float) {
                                setting.setValue(this.<Float>get(settings, key, (Float) setting.getValue()));
                            } else {
                                setting.setValue(value);
                            }
                        }
                    }
                }
            }
        }
    }
}
