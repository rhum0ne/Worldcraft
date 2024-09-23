package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.blocks.MeshArrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by Kristoffer on 15-08-06.
 */
public class MeshObjectLoader {

    private static String TAG = "ObjectLoader";
    private static int BUFFER_READER_SIZE = 65536;
    private static boolean ENABLE_LOGGING = false;

    /**
     * Load 3D model mesh from inputstream.
     * A Wavefront formatted file (.obj) is expected as input and may be loaded
     * remotely or as a local file.
     * @param is input file in form of a .obj "text file".
     * @return a object containing vertices, normals and textureCoordinates.
     */
    public static MeshArrays loadModelMeshFromStream(InputStream is) {
        BufferedReader bufferedReader = null;

        // Variables pour les coordonnées minimales et maximales
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;

        try {
            // Lists to keep all data when reading file
            ArrayList<Float> vlist = new ArrayList<Float>();
            ArrayList<Float> tlist = new ArrayList<Float>();
            ArrayList<Float> nlist = new ArrayList<Float>();
            ArrayList<Face> fplist = new ArrayList<Face>();

            // Result buffers
            FloatBuffer mVertexBuffer;
            FloatBuffer mTexBuffer;
            FloatBuffer mNormBuffer;

            int numVerts = 0;
            int numTexCoords = 0;
            int numNormals = 0;
            int numFaces = 0;

            String str;
            String[] tmp;

            bufferedReader = new BufferedReader(new InputStreamReader(is), BUFFER_READER_SIZE);

            while ((str = bufferedReader.readLine()) != null) {

                // Replace double spaces. Some files may have it. Ex. files from 3ds max.
                str = str.replace("  ", " ");
                tmp = str.split(" ");

                if (tmp[0].equalsIgnoreCase("v")) {
                    for (int i = 1; i < 4; i++) {
                        float value = Float.parseFloat(tmp[i]);
                        vlist.add(value);

                        // Mise à jour des valeurs minimales et maximales
                        if (i == 1) {
                            minX = Math.min(minX, value);
                            maxX = Math.max(maxX, value);
                        } else if (i == 2) {
                            minY = Math.min(minY, value);
                            maxY = Math.max(maxY, value);
                        } else if (i == 3) {
                            minZ = Math.min(minZ, value);
                            maxZ = Math.max(maxZ, value);
                        }
                    }
                    numVerts++;
                }

                if (tmp[0].equalsIgnoreCase("vn")) {
                    for (int i = 1; i < 4; i++) {
                        nlist.add(Float.parseFloat(tmp[i]));
                    }
                    numNormals++;
                }

                if (tmp[0].equalsIgnoreCase("vt")) {
                    for (int i = 1; i < 3; i++) {
                        tlist.add(Float.parseFloat(tmp[i]));
                    }
                    numTexCoords++;
                }

                if (tmp[0].equalsIgnoreCase("f")) {
                    String[] ftmp;
                    int facex;
                    int facey;
                    int facez;

                    for (int i = 1; i < 4; i++) {
                        ftmp = tmp[i].split("/");

                        facex = faceStringToInt(ftmp, 0) - 1;
                        facey = faceStringToInt(ftmp, 1) - 1;
                        facez = faceStringToInt(ftmp, 2) - 1;

                        fplist.add(new Face(facex, facey, facez));
                    }

                    numFaces++;

                    if (tmp.length > 4 && !tmp[4].equals("")) {
                        for (int i = 1; i < 5; i++) {
                            ftmp = tmp[i].split("/");

                            if (i == 1 || i == 3) {
                                facex = faceStringToInt(ftmp, 0) - 1;
                                facey = faceStringToInt(ftmp, 1) - 1;
                                facez = faceStringToInt(ftmp, 2) - 1;
                                fplist.add(new Face(facex, facey, facez));
                            } else if (i == 2) {
                                String[] gtmp = tmp[4].split("/");
                                facex = faceStringToInt(gtmp, 0) - 1;
                                facey = faceStringToInt(gtmp, 1) - 1;
                                facez = faceStringToInt(gtmp, 2) - 1;
                                fplist.add(new Face(facex, facey, facez));
                            }
                        }
                        numFaces++;
                    }
                }
            }

            if (ENABLE_LOGGING) {
                System.out.println("Vertices: " + numVerts);
                System.out.println("Normals: " + numNormals);
                System.out.println("TextureCoords: " + numTexCoords);
                System.out.println("Faces: " + numFaces);
                System.out.println("Bounding Box: minX=" + minX + ", minY=" + minY + ", minZ=" + minZ +
                        ", maxX=" + maxX + ", maxY=" + maxY + ", maxZ=" + maxZ);
            }

            // Each float takes 4 bytes
            int fplistSize = fplist.size();
            ByteBuffer vbb = ByteBuffer.allocateDirect(fplistSize * 4 * 3);
            vbb.order(ByteOrder.LITTLE_ENDIAN);
            mVertexBuffer = vbb.asFloatBuffer();

            ByteBuffer vtbb = ByteBuffer.allocateDirect(fplistSize * 4 * 2);
            vtbb.order(ByteOrder.LITTLE_ENDIAN);
            mTexBuffer = vtbb.asFloatBuffer();

            ByteBuffer nbb = ByteBuffer.allocateDirect(fplistSize * 4 * 3);
            nbb.order(ByteOrder.LITTLE_ENDIAN);
            mNormBuffer = nbb.asFloatBuffer();

            int vlistSize = vlist.size();
            int nlistSize = nlist.size();
            int tlistSize = tlist.size();

            Face face;

            for (int j = 0; j < fplistSize; j++) {
                face = fplist.get(j);

                // Ajout des coordonnées des sommets (vertex)
                mVertexBuffer.put(vlist.get(fixedIndex(vlistSize, (int) face.fx * 3, numVerts)));
                mVertexBuffer.put(vlist.get(fixedIndex(vlistSize, (int) (face.fx * 3 + 1), numVerts)));
                mVertexBuffer.put(vlist.get(fixedIndex(vlistSize, (int) (face.fx * 3 + 2), numVerts)));

                // Ajout des coordonnées de texture, avec inversion de l'axe Y
                mTexBuffer.put(tlist.get(fixedIndex(tlistSize, face.fy * 2, numTexCoords)));
                // Inverser la coordonnée Y (1 - y)
                float invertedY = 1.0f - tlist.get(fixedIndex(tlistSize, face.fy * 2 + 1, numTexCoords));
                mTexBuffer.put(invertedY);

                // Ajout des normales
                mNormBuffer.put(nlist.get(fixedIndex(nlistSize, face.fz * 3, numNormals)));
                mNormBuffer.put(nlist.get(fixedIndex(nlistSize, face.fz * 3 + 1, numNormals)));
                mNormBuffer.put(nlist.get(fixedIndex(nlistSize, face.fz * 3 + 2, numNormals)));
            }

            mVertexBuffer.rewind();
            mTexBuffer.rewind();
            mNormBuffer.rewind();

            return new MeshArrays(mVertexBuffer, mNormBuffer, mTexBuffer, numFaces * 3,
                    minX, minY, minZ, maxX, maxY, maxZ);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static int fixedIndex(int listSize, int index, int total) {
        if (index >= listSize || index < 0) {
            return total -1;
        } else {
            return index;
        }
    }

    private static int faceStringToInt(String[] number, int index) {
        int result;

        try {
            result = Integer.parseInt(number[index]);
        } catch (Exception e) {
            // do nothing at the moment return 0
            result = 0;
        }

        return result;
    }

    /**
     * Local class to keep vertex, normal and texture coordinate while reading file.
     */
    private static class Face {
        public long fx;
        public int fy;
        public int fz;

        public Face(long fx, int fy, int fz) {
            this.fx = fx;
            this.fy = fy;
            this.fz = fz;
        }
    }
}