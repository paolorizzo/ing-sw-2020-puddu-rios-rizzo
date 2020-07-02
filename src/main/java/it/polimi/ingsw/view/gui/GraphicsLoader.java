package it.polimi.ingsw.view.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

import java.io.*;
import java.util.*;

/**
 * It is a Loader of all graphics elements of GUI. It is used to read and store only one time the graphics from resources.
 */
public class GraphicsLoader {
    private static GraphicsLoader instance = null;

    private HashMap<String, TriangleMesh> meshes;
    private HashMap<String, PhongMaterial> textures;
    private HashMap<String, Image> images;

    /**
     * Initialized all the HashMap and call the loader
     */
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

    /**
     * Pattern singleton
     * @return always the same instance
     */
    public static GraphicsLoader instance() {
        if(instance == null)
            instance = new GraphicsLoader();
        return instance;
    }

    /**
     * It returns the mesh request
     * @param name the name of mesh saved in the HashMap
     * @return the TriangularMesh request
     */
    public Mesh getMesh(String name) {
        return meshes.get(name);
    }
    /**
     * It returns the texture texture
     * @param name the name of texture saved in the HashMap
     * @return the PhongMaterial request
     */
    public PhongMaterial getTexture(String name) {
        return textures.get(name);
    }
    /**
     * It returns the image request
     * @param name the name of image saved in the HashMap
     * @return the Image request
     */
    public Image getImage(String name) {
        return images.get(name);
    }

    /**
     * It initializes and starts threads for every graphic elements read in graphics.json
     * @throws InterruptedException if a thread is interrupted
     */
    void loader() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        try{

            BufferedReader reader = new BufferedReader(new InputStreamReader(GraphicsLoader.class.getResourceAsStream("/graphics.json")));
            StringBuilder stringGraphics = new StringBuilder();
            while (reader.ready()) {
                String line = reader.readLine();
                stringGraphics.append(line.toCharArray());
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

            JsonArray images = jsonObject.get("images").getAsJsonArray();
            for(JsonElement im: images){
                final String name = im.getAsJsonObject().get("name").getAsString();
                final String image = im.getAsJsonObject().get("file").getAsString();
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

    /**
     * It loads and puts in the hashmap the mesh
     * @param name the name of mesh
     * @param URI the URI in the resources
     */
    private void loaderMesh(String name, String URI){
        TriangleMesh mesh = createMeshFromOBJ("/"+URI);
        synchronized (meshes){
            meshes.put(name, mesh);
        }
    }
    /**
     * It loads and puts in the hashmap the texture
     * @param name the name of texture
     * @param URI the URI in the resources
     */
    private void loaderTexture(String name, String URI) {

        PhongMaterial texture = new PhongMaterial();
        try{
            InputStream inputStream = GraphicsLoader.class.getResourceAsStream("/"+URI);
            Image i = new Image(inputStream);
            texture.setDiffuseMap(i);
        }catch(Exception e){
            System.out.println("file texture /"+URI+"  doesn't exists");
        }
        synchronized (textures){
            textures.put(name, texture);
        }
    }
    /**
     * It loads and puts in the hashmap the image
     * @param name the name of image
     * @param URI the URI in the resources
     */
    private void loaderImage(String name, String URI){
        try{
            InputStream inputStream = GraphicsLoader.class.getResourceAsStream("/"+URI);
            Image image = new Image(inputStream);
            synchronized (images){
                images.put(name, image);
            }
        }catch(Exception e){
            System.out.println("file image /"+URI+" doesn't exists");
        }

    }

    /**
     * It is a custom loader of OBJ files. It reads the points, the normal vectors of the face and the coordinates of
     * textures of an OBJ and creates a TringularMesh
     * @param URI the URI in the resources
     * @return the TriangularMesh of the given OBJ files
     */
    TriangleMesh createMeshFromOBJ(String URI){
        TriangleMesh mesh = new TriangleMesh();
        try {
            BufferedReader myReader = new BufferedReader(new InputStreamReader(GraphicsLoader.class.getResourceAsStream(URI)));

            mesh.setVertexFormat(VertexFormat.POINT_TEXCOORD);

            while (myReader.ready()) {
                String data = myReader.readLine();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mesh;
    }


}
