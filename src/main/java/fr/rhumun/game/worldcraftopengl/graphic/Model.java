package fr.rhumun.game.worldcraftopengl.graphic;

import fr.rhumun.game.worldcraftopengl.Vector;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Model {

    //Double array because 1 triangle = 3 points  && 1 point = 3 coordinates;
    List<Double[][]> triangles = new ArrayList<>();
    List<Double[][]> textureMapping = new ArrayList<>();
    List<Vector> normals = new ArrayList<>();

    public Model(){

    }

    public void addTriangle(
            double x0, double y0, double z0,
            double x1, double y1, double z1,
            double x2, double y2, double z2,
            Vector normal,
            double u0, double v0,
            double u1, double v1,
            double u2, double v2
    ) {
        Double[][] triangle = new Double[3][];

        triangle[0] = new Double[]{x0, y0, z0, u0, v0};
        triangle[1] = new Double[]{x1, y1, z1, u1, v1};
        triangle[2] = new Double[]{x2, y2, z2, u2, v2};

        this.triangles.add(triangle);
        this.normals.add(normal);
    }



    public void addQuad(
            double x0, double y0, double z0, // Premier coin
            double x1, double y1, double z1, // Deuxième coin
            double x2, double y2, double z2, // Troisième coin
            double x3, double y3, double z3, // Quatrième coin
            Vector normal, // Normale
            double u0, double v0, // UV pour le premier coin
            double u1, double v1, // UV pour le deuxième coin
            double u2, double v2, // UV pour le troisième coin
            double u3, double v3 // UV pour le quatrième coin
    ) {
        // Premier triangle
        addTriangle(x0, y0, z0, x1, y1, z1, x2, y2, z2, normal, u0, v0, u1, v1, u2, v2);
        // Deuxième triangle
        addTriangle(x2, y2, z2, x3, y3, z3, x0, y0, z0, normal, u2, v2, u3, v3, u0, v0);
    }



    public Vector getNormal(int id){ return normals.get(id); }

}