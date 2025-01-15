package fr.rhumun.game.worldcraftopengl.worlds.generators.utils;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;
import fr.rhumun.game.worldcraftopengl.worlds.generators.NormalWorldGenerator;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biome;
import lombok.Getter;

@Getter
public class HeightCalculation {


    private final NormalWorldGenerator worldGenerator;
    private final JNoise continentalness;
    private final JNoise erosion;
    private final JNoise pav;

    private final JNoise regularity;

    private final Graph continentalGraph;
    private final Graph erosionGraph;
    private final Graph pavGraph;
    public HeightCalculation(NormalWorldGenerator worldGenerator, JNoise continentalness, JNoise erosion, JNoise pav) {
        this.worldGenerator = worldGenerator;
        this.continentalness = continentalness;
        this.erosion = erosion;
        this.pav = pav;

        // Initialisation des graphes avec les nouvelles données
        double[] continentalKeys = new double[]{-1, -0.94, -0.6, -0.5, -0.3, -0.2, -0.1, 0.2, 0.9, 1};
        int[] continentalValues = new int[]{120, 37, 37, 50, 50, 80, 87, 90, 100, 100};
        this.continentalGraph = new Graph(continentalKeys, continentalValues);

        double[] erosionKeys = new double[]{-1, -0.7, -0.3, -0.22, 0, 0.4, 0.65, 0.7, 0.8, 0.85, 0.9, 1};
        int[] erosionValues = new int[]{170, 70, 60, 65, 35, 15, 25, 45, 45, 25, 15, 15};
        this.erosionGraph = new Graph(erosionKeys, erosionValues);

        double[] pavKeys = new double[]{-1, -0.7, -0.15, 0.07, 0.4, 0.55, 0.7, 0.9, 1};
        int[] pavValues = new int[]{0, 0, 15, 26, 55, 40, 150, 135, 150};
        this.pavGraph = new Graph(pavKeys, pavValues);



        regularity = JNoise.newBuilder()
                .perlin(worldGenerator.getSeed().getLong(), Interpolation.COSINE, FadeFunction.QUADRATIC_PIECEWISE)
                .octavate(2, 0.5, 2.0, FractalFunction.FBM, false)
                .abs()
                .build();
    }

    public int calcHeight(double x, double z, Biome biome){
        double regularityValue = regularity.evaluateNoise(x / 500.0, z / 500.0); // Bruit à grande échelle

        double continentalValue = continentalness.evaluateNoise(x/512f, z/512f);
        double erosionValue = erosion.evaluateNoise(x/612f, z/612f);
        double absContinentalValue = Math.abs(continentalValue);

        double pavLargeScale = pav.evaluateNoise(x / 500.0, z / 500.0); // Relief large
        double pavSmallScale = pav.evaluateNoise(x / 40.0, z / 40.0); // Détails fins


        double pavEffect = pavGraph.getResult(pavLargeScale)* (((absContinentalValue<0.4) ? 0 : (absContinentalValue-0.4)*10)+0.2) ; // Modulation dynamique
        double smallPeaks = pavGraph.getResult(pavSmallScale)* ((absContinentalValue<0.5) ? 0 : (absContinentalValue-0.5)) * regularityValue;

        if(pavEffect>150) pavEffect *= (0.2*regularityValue+0.8);

        int cH = (int) continentalGraph.getResult(continentalValue);
        int eH = (int) erosionGraph.getResult(erosionValue);

        int base = (int) ( cH - eH + pavEffect + smallPeaks);

//        System.out.println(continentalValue + "(" + cH + ") "  + " - " + erosionValue  + "(" + eH + ") = " + base);

        if(base>256) System.out.println(cH + " " + eH + " " + pavEffect + " ( " + continentalValue +" " + pavLargeScale + " )");

        return Math.max(0, Math.min(worldGenerator.getWorld().getHeigth(), base));
    }
}
