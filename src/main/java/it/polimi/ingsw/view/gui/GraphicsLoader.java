package it.polimi.ingsw.view.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.power.PowerStrategy;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class GraphicsLoader {
    private static GraphicsLoader instance = null;

    HashMap<String, TriangleMesh> meshes;
    HashMap<String, PhongMaterial> textures;
    HashMap<String, Image> images;

    private GraphicsLoader() {
        meshes = new HashMap<>();
        textures = new HashMap<>();
        images = new HashMap<>();
        try {
            loader();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static GraphicsLoader instance() {
        if(instance == null)
            instance = new GraphicsLoader();
        return instance;
    }

    public Mesh getMesh(String type) {
        return meshes.get(type);
    }

    public PhongMaterial getTexture(String type) {
        return textures.get(type);
    }

    public Image getImage(String name) {
        return images.get(name);
    }

    void loader() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        try{
            File file = new File("./src/main/resources/graphics.json");
            StringBuilder stringGraphics = new StringBuilder((int)file.length());
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                stringGraphics.append(scanner.nextLine() + System.lineSeparator());
            }
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(stringGraphics.toString());

            JsonArray meshes = jsonObject.get("meshes").getAsJsonArray();

            for(JsonElement mesh: meshes){
                final String name = mesh.getAsJsonObject().get("name").getAsString();
                final String uri = mesh.getAsJsonObject().get("file").getAsString();
                threads.add(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loaderMesh(name, uri);
                    }
                }));
            }

            JsonArray textures = jsonObject.get("textures").getAsJsonArray();

            for(JsonElement texture: textures){
                final String name = texture.getAsJsonObject().get("name").getAsString();
                final String uri = texture.getAsJsonObject().get("file").getAsString();
                threads.add(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loaderTexture(name, uri);
                    }
                }));
            }

            JsonArray cards = jsonObject.get("cards").getAsJsonArray();
            for(JsonElement card: cards){
                final String name = card.getAsJsonObject().get("name").getAsString();
                final String image = card.getAsJsonObject().get("file").getAsString();
                threads.add(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loaderImage(name, image);
                    }
                }));
            }
        }catch (Exception e){
            System.out.println("Error during the loading of graphic: "+e.getMessage());
        }
        for(Thread thread: threads){
            thread.start();
        }

        for(Thread thread: threads){
            thread.join();
        }
    }

    private void loaderMesh(String name, String URI){
        TriangleMesh mesh = createMeshFromOBJ("./src/main/resources/"+URI);
        synchronized (meshes){
            meshes.put(name, mesh);
        }
    }
    private void loaderTexture(String name, String URI){
        File file = new File("./src/main/resources/"+URI);
        String path = file.getAbsolutePath();
        PhongMaterial texture = new PhongMaterial();
        try{
            FileInputStream inputStream = new FileInputStream(path);
            Image i = new Image(inputStream);
            texture.setDiffuseMap(i);
        }catch(Exception e){
            System.out.println("file "+path+" doesn't exists");
        }
        synchronized (textures){
            textures.put(name, texture);
        }
    }
    private void loaderImage(String name, String URI){
        File file = new File("./src/main/resources/"+URI);
        String path = file.getAbsolutePath();
        try{
            FileInputStream inputStream = new FileInputStream(path);
            Image image = new Image(inputStream);
            synchronized (images){
                images.put(name, image);
            }
        }catch(Exception e){
            System.out.println("file "+path+" doesn't exists");
        }

    }
    TriangleMesh createMeshFromOBJ(String URI){
        TriangleMesh mesh = new TriangleMesh();
        try {
            File myObj = new File(URI);
            Scanner myReader = new Scanner(myObj);
            mesh.setVertexFormat(VertexFormat.POINT_TEXCOORD);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Scanner line = new Scanner(data);
                line.useLocale(Locale.US); // . invece di , in file .obj
                if(data.charAt(0)=='v' && data.charAt(1)=='t') {
                    line.next();
                    float p1 = line.nextFloat();
                    float p2 = line.nextFloat();
                    mesh.getTexCoords().addAll(p1, 1-p2);
                }else if(data.charAt(0)=='v' && data.charAt(1)=='n') {
                    mesh.setVertexFormat(VertexFormat.POINT_NORMAL_TEXCOORD);
                    line.next();
                    float p1 = line.nextFloat();
                    float p2 = line.nextFloat();
                    float p3 = line.nextFloat();
                    mesh.getNormals().addAll(p1, p2, p3);
                }else if(data.charAt(0) == 'v'){
                    line.next();
                    float p1 = line.nextFloat();
                    float p2 = line.nextFloat();
                    float p3 = line.nextFloat();
                    mesh.getPoints().addAll(p1, p2, p3);
                    //mesh.getTexCoords().addAll(0f, 0f);
                }else if(data.charAt(0) == 'f'){
                    if(mesh.getVertexFormat().equals(VertexFormat.POINT_TEXCOORD)) {
                        mesh.getTexCoords().addAll(0f, 0f);
                        line.useDelimiter(" |// |//|\\t");
                        line.next();
                        int v1 = line.nextInt();
                        int v2 = line.nextInt();
                        int v3 = line.nextInt();
                        mesh.getFaces().addAll(v1 - 1, 0, v2 - 1, 0, v3 - 1, 0);
                    }else{
                        line.useDelimiter(" |/ |/");
                        line.next();
                        int v1 = line.nextInt();
                        int vt1 = line.nextInt();
                        int vn1 = line.nextInt();
                        int v2 = line.nextInt();
                        int vt2 = line.nextInt();
                        int vn2 = line.nextInt();
                        int v3 = line.nextInt();
                        int vt3 = line.nextInt();
                        int vn3 = line.nextInt();
                        mesh.getFaces().addAll(v1-1, vn1-1, vt1-1, v2-1, vn2-1, vt2-1, v3-1, vn3-1, vt3-1);
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred with "+URI+": can't create the mesh");
            e.printStackTrace();
        }
        return mesh;
    }


}
