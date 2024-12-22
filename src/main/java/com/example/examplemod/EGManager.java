package src.main.java.com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

enum EGTypes {
    normal,
    special,
    coded,
}

public class EGManager {
    private final String FILE_NAME;
    private final Set<BlockPos> coords = new HashSet<>();

    public EGManager(String FILE_NAME) {
        this.FILE_NAME = FILE_NAME;

        File directory = new File(Minecraft.getMinecraft().mcDataDir, "examplemod");

        if (!directory.exists()) directory.mkdir();

        File file = new File(directory, FILE_NAME);

        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Gson gson = new Gson();
                JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
                for (JsonElement element : jsonArray) {
                    JsonObject coordObj = element.getAsJsonObject();
                    int x = coordObj.get("x").getAsInt();
                    int y = coordObj.get("y").getAsInt();
                    int z = coordObj.get("z").getAsInt();
                    coords.add(new BlockPos(x, y, z));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(BlockPos pos) {
        coords.add(pos);
        try (Writer writer = new FileWriter(FILE_NAME)) {
            Gson gson = new Gson();
            JsonArray jsonArray = new JsonArray();
            for (BlockPos position : coords) {
                JsonObject coordObj = new JsonObject();
                coordObj.addProperty("x", position.getX());
                coordObj.addProperty("y", position.getY());
                coordObj.addProperty("z", position.getZ());
                jsonArray.add(coordObj);
            }
            gson.toJson(jsonArray, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: public void remove() {}
    // TODO:

    public Set<BlockPos> getSaved() {
        return coords;
    }

    public String[] types() {
        return Arrays.stream(EGTypes.values()).map(Enum::name).toArray(String[]::new);
    }
}
