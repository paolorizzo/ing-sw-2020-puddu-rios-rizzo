package it.polimi.ingsw.view.gui;

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
    private GraphicsLoader() {
        meshes = new HashMap<>();
        textures = new HashMap<>();
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

    void loader() throws InterruptedException {
        //TODO create a file json
        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("LEVEL0", "LV0.obj");
            }
        }));
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("LEVEL1", "LV1.obj");
            }
        }));
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("LEVEL2", "LV2.obj");
            }
        }));
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("LEVEL3", "LV3.obj");
            }
        }));
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("DOME", "DOME.obj");
            }
        }));
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("WORKER_F", "worker_F.obj");
            }
        }));
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("WORKER_M", "worker_M.obj");
            }
        }));
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("INNERWALL", "innerwall.obj");
            }
        }));
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("OUTERWALL", "outerwall.obj");
            }
        }));
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("ISLAND", "island.obj");
            }
        }));
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("ISLANDS", "islands.obj");
            }
        }));
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("SEA", "sea.obj");
            }
        }));

        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                loaderMesh("SEADOWN", "seadown.obj");
            }
        }));

        for(Thread thread: threads){
            thread.start();
        }
        textures.put("WORKER_M_BLUE", loadTexture("worker_male_blue.png"));
        textures.put("WORKER_M_WHITE", loadTexture("worker_male_white.png"));
        textures.put("WORKER_M_ORANGE", loadTexture("worker_male_orange.png"));
        textures.put("WORKER_M_PINK", loadTexture("worker_male_pink.png"));
        textures.put("WORKER_M_PURPLE", loadTexture("worker_male_purple.png"));
        textures.put("WORKER_M_TAN", loadTexture("worker_male_tan.png"));

        textures.put("WORKER_M_ENABLED", loadTexture("worker_male_enabled.png"));
        textures.put("WORKER_M_PREVIEW", loadTexture("worker_male_preview.png"));
        textures.put("WORKER_M_DISABLED", loadTexture("worker_male_disabled.png"));

        textures.put("WORKER_F_BLUE", loadTexture("worker_female_blue.png"));
        textures.put("WORKER_F_WHITE", loadTexture("worker_female_white.png"));
        textures.put("WORKER_F_ORANGE", loadTexture("worker_female_orange.png"));
        textures.put("WORKER_F_PINK", loadTexture("worker_female_pink.png"));
        textures.put("WORKER_F_PURPLE", loadTexture("worker_female_purple.png"));
        textures.put("WORKER_F_TAN", loadTexture("worker_female_tan.png"));

        textures.put("WORKER_F_ENABLED", loadTexture("worker_female_enabled.png"));
        textures.put("WORKER_F_PREVIEW", loadTexture("worker_female_preview.png"));
        textures.put("WORKER_F_DISABLED", loadTexture("worker_female_disabled.png"));

        textures.put("LEVEL0_default", loadTexture("LV0_default.png"));
        textures.put("LEVEL0_enabled", loadTexture("LV0_enabled.png"));
        textures.put("LEVEL0_disabled", loadTexture("LV0_disabled.png"));
        textures.put("LEVEL0_preview", loadTexture("LV0_preview.png"));

        textures.put("LEVEL1_default", loadTexture("LV1_default.png"));
        textures.put("LEVEL1_enabled", loadTexture("LV1_enabled.png"));
        textures.put("LEVEL1_disabled", loadTexture("LV1_disabled.png"));
        textures.put("LEVEL1_preview", loadTexture("LV1_preview.png"));

        textures.put("LEVEL2_default", loadTexture("LV2_default.png"));
        textures.put("LEVEL2_enabled", loadTexture("LV2_enabled.png"));
        textures.put("LEVEL2_disabled", loadTexture("LV2_disabled.png"));
        textures.put("LEVEL2_preview", loadTexture("LV2_preview.png"));

        textures.put("LEVEL3_default", loadTexture("LV3_default.png"));
        textures.put("LEVEL3_enabled", loadTexture("LV3_enabled.png"));
        textures.put("LEVEL3_disabled", loadTexture("LV3_disabled.png"));
        textures.put("LEVEL3_preview", loadTexture("LV3_preview.png"));

        textures.put("DOME_default", loadTexture("DOME_default.png"));
        textures.put("DOME_enabled", loadTexture("DOME_enabled.png"));
        textures.put("DOME_disabled", loadTexture("DOME_disabled.png"));
        textures.put("DOME_preview", loadTexture("DOME_preview.png"));

        textures.put("ISLAND", loadTexture("island.png"));

        textures.put("SEA", loadTexture("sea.png"));

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
            Image i = new Image(path);
            texture.setDiffuseMap(i);
        }catch(Exception e){
            System.out.println("file "+path+" doesn't exists");
        }
        synchronized (textures){
            textures.put(name, texture);
        }
    }
    private PhongMaterial loadTexture(String URI){
        File file = new File("./src/main/resources/"+URI);
        String path = file.getAbsolutePath();
        PhongMaterial texture = new PhongMaterial();
        try{
            FileInputStream inputStream = new FileInputStream(path);
            Image i = new Image(inputStream);
            texture.setDiffuseMap(i);
        }catch(Exception e){
            System.out.println("file "+URI+" doesn't exists, "+path);
        }
        return texture;
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
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return mesh;
    }


}
