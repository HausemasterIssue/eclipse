package xyz.aesthetical.astra.util.blur;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import xyz.aesthetical.astra.Astra;
import xyz.aesthetical.astra.features.modules.render.GuiModifier;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class FakeBlurShaderPack implements IResourcePack, IResourceManagerReloadListener {
    private final Map<ResourceLocation, String> loadedData = new HashMap<>();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        loadedData.clear();
    }

    @Override
    public InputStream getInputStream(ResourceLocation location) throws IOException {
        if (validPath(location)) {
            String s = loadedData.computeIfAbsent(location, loc -> {
                InputStream in = Astra.class.getResourceAsStream("/" + location.getPath());
                StringBuilder data = new StringBuilder();

                try (Scanner scan = new Scanner(in)) {
                    while (scan.hasNextLine()) {
                        // @todo @radius@ should be replaced with GuiModifier shitter fuckery
                        data.append(scan.nextLine().replaceAll("@radius@", String.valueOf(GuiModifier.instance.intensity.getValue()))).append('\n');
                    }
                }

                return data.toString();
            });

            return new ByteArrayInputStream(s.getBytes());
        }

        throw new FileNotFoundException(location.toString());
    }

    @Override
    public boolean resourceExists(ResourceLocation location) {
        return validPath(location) && Astra.class.getResource("/" + location.getPath()) != null;
    }

    @Override
    public Set<String> getResourceDomains() {
        return ImmutableSet.of("minecraft");
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Override
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        if ("pack".equals(metadataSectionName)) {
            return (T) new PackMetadataSection(new TextComponentString("Blur's default shaders"), 3);
        }

        return null;
    }

    @Override
    public BufferedImage getPackImage() throws IOException {
        throw new FileNotFoundException("pack.png");
    }

    @Override
    public String getPackName() {
        return "Blur dummy resource pack";
    }

    protected boolean validPath(ResourceLocation location) {
        return location.getPath().equals("minecraft") && location.getPath().startsWith("assets/minecraft/shaders/");
    }
}
