package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.entities.physics.Movements;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public class Entity {
    protected static final float RAY_STEP = 0.02f;
    private final Game game;

    private Location location;

    private final int reach;

    private boolean isFlying = false;
    private boolean isNoClipping = false;
    private boolean isSneaking = false;
    private boolean isSprinting = false;

    private final int jumpForce;

    private final int walkSpeed;
    private final int sneakSpeed;
    private final int sprintSpeed;
    private float accelerationByTick;

    private final Vector3f velocity = new Vector3f(0, 0, 0);

    private float radius;
    private float height;

    private Model model;
    private short textureID;


    public Entity(Game game, Model model, short textureID, int reach, float radius, float height, int walkSpeed, int sneakSpeed, int sprintSpeed, float accelerationByTick, int jumpForce, double x, double y, double z, float yaw, float pitch) {
        this.location = new Location(game.getWorld(),x, y, z, yaw, pitch);
        this.game = game;
        this.model = model;
        this.height = height;
        this.radius = radius;
        this.reach = reach;
        this.accelerationByTick = accelerationByTick;
        this.walkSpeed = walkSpeed;
        this.sneakSpeed = sneakSpeed;
        this.sprintSpeed = sprintSpeed;
        this.jumpForce = jumpForce;
        this.textureID = textureID;
    }

    public void update(){
        Movements.applyMovements(this);
    }

    public Entity(Game game, int reach, float radius, float height, int walkSpeed, int sneakSpeed, int sprintSpeed, float accelerationByTick, int jumpForce, double x, double y, double z, float yaw, float pitch) {
        this(game, null, (short) 0, reach, radius, height, walkSpeed, sneakSpeed, sprintSpeed, accelerationByTick, jumpForce, x, y, z, yaw, pitch);
    }

    public void setLocation(Location loc){
        this.location = new Location(loc);
    }



    public Vector3f getRayDirection() {
        return new Vector3f(
                (float) Math.cos(Math.toRadians(this.getLocation().getYaw())) * (float) Math.cos(Math.toRadians(this.getLocation().getPitch())),
                (float) Math.sin(Math.toRadians(this.getLocation().getPitch())),
                (float) Math.sin(Math.toRadians(this.getLocation().getYaw())) * (float) Math.cos(Math.toRadians(this.getLocation().getPitch()))
        ).normalize();
    }

    public void addX(double a){
        if ((this.hasBlockInDirection(new Vector3f((float) a, 0, 0)) && !this.isNoClipping)) {
            return;
        }
        this.getLocation().addX(a);
        this.onMove();
    }
    public void addZ(double a){
        if ((this.hasBlockInDirection(new Vector3f( 0, 0, (float)a)) && !this.isNoClipping)) {
            return;
        }
        this.getLocation().addZ(a);
        this.onMove();
    }
    public void addY(double a){
        if(!this.isNoClipping && a>0 && this.hasBlockTop()){
            return;
        } else if(!this.isNoClipping && a<0 && this.hasBlockDown()) {
            return;
        }

        double step = a/10;
        for(int i=0; i<10; i++){
            if(!this.isNoClipping && a>0 && this.hasBlockTop()) break;
            if(!this.isNoClipping && a<0 && this.hasBlockDown()) break;
            this.getLocation().addY(step);
        }

        this.onMove();
    }
    public void setYaw(float a){
        this.getLocation().setYaw(a);
        this.onMove();
    }
    public void setPitch(float a){
        this.getLocation().setPitch(a);
        this.onMove();
    }

    protected void onMove(){

    }

    public Block getBlockInDirection(Vector3f direction, int yLevel) {
        if(yLevel > this.height){
            this.game.errorLog("Trying to get block at yLevel for an Entity with height " + this.height + "\nreturning null...");
            return null;
        }
        // Normaliser le vecteur pour garantir qu'il a une norme de 1
        Vector3f normalizedDirection = direction.normalize();

        // Calculer les coordonnées en fonction du rayon du joueur
        double targetX = this.getLocation().getX() + normalizedDirection.get(0) * this.radius;
        double targetY = this.getLocation().getY() - yLevel;
        double targetZ = this.getLocation().getZ() + normalizedDirection.get(2) * this.radius;

        // Retourner le bloc à cette position
        return this.getLocation().getWorld().getBlockAt(targetX, targetY, targetZ, false);
    }

    public boolean hasBlockInDirection(Vector3f direction) {
        for(int y=0; y<this.height; y++) {
            Block block = this.getBlockInDirection(direction, y);
            if( block != null && block.getMaterial() != null) return true;
        }
        return false;
    }


    public Block getBlockDown(){
        return this.getLocation().getWorld().getBlockAt(this.getLocation().getX(), this.getLocation().getY()-this.height-0.2f, this.getLocation().getZ(), false);
    }

    public boolean hasBlockDown(){
        Block block = this.getBlockDown();
        return block != null && block.getMaterial() != null;
    }

    public Block getBlockTop(){
        return this.getLocation().getWorld().getBlockAt(this.getLocation().getX(), this.getLocation().getY()+0.2f, this.getLocation().getZ(), false);
    }

    public boolean hasBlockTop(){
        Block block = this.getBlockTop();
        return block != null && block.getMaterial() != null;
    }

    public boolean isInLiquid() {
        Block head = this.getWorld().getBlockAt(
                this.getLocation().getX(),
                this.getLocation().getY(),
                this.getLocation().getZ(),
                false);
        if(head != null && head.getMaterial() != null && head.getMaterial().isLiquid()) return true;

        Block body = this.getWorld().getBlockAt(
                this.getLocation().getX(),
                this.getLocation().getY() - this.height * 0.5,
                this.getLocation().getZ(),
                false);
        return body != null && body.getMaterial() != null && body.getMaterial().isLiquid();
    }

    public int getSpeed(){
        if(isSneaking) return sneakSpeed;
        if(isSprinting) return sprintSpeed;
        return walkSpeed;
    }

    public void jump() {
        if(this.velocity.get(1) == 0)
            this.getVelocity().add(0, (float)jumpForce/9, 0);
    }


    public Block getSelectedBlock() {
        Vector3f direction = getRayDirection();
        Vector3f pos = new Vector3f((float) this.getLocation().getX(), (float) this.getLocation().getY(), (float) this.getLocation().getZ());

        for (float distance = 0; distance < this.getReach(); distance += RAY_STEP) {
            Block block = this.getGame().getWorld().getBlockAt(pos, true);

            if (block != null && block.getMaterial() != null && !block.getMaterial().isLiquid()) {
                return block;
            }

            pos.add(direction.x * RAY_STEP, direction.y * RAY_STEP, direction.z * RAY_STEP);
        }
        return null;
    }

    public void placeBlock(final Item item){
        Vector3f direction = getRayDirection();
        Vector3f pos = new Vector3f((float) this.getLocation().getX(), (float) this.getLocation().getY(), (float) this.getLocation().getZ());
        Vector3f hitPosition = null;
        Block block = null;
        Block block1 = null;

        for (float distance = 0; distance < this.getReach(); distance += RAY_STEP) {
            block1 = this.getLocation().getWorld().getBlockAt(pos, true);
            if(block1 == null) break;

            if (block1.isCliquable()) {
                break;
            }
            block = block1;
            hitPosition = new Vector3f(pos);
            pos.add(direction.x * RAY_STEP, direction.y * RAY_STEP, direction.z * RAY_STEP);
        }

        if(block1 == null || !block1.isCliquable()) return;
        placeBlockAt(item, block, hitPosition, direction);
    }

    protected void placeBlockAt(Item item, Block block, Vector3f hitPosition, Vector3f direction){
        if (block == null) {
            System.out.println("Impossible de déterminer la face du bloc.");
            return;
        }

        Material material = item.getMaterial();

        Model model = item.getModel();
        block.setModel(model).setMaterial(material);

        model.setBlockDataOnPlace(block, hitPosition, direction);
    }

    public Material breakBlock(){
        Block block = this.getSelectedBlock();
        if(block == null || block.getMaterial() == null) return null;
        Material mat = block.getMaterial();
        block.setMaterial(null);
        return mat;
    }

    public String getName() {
        return "EntityXXX";
    }

    public World getWorld() {return this.location.getWorld();}

}
