package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.content.models.AbstractModel;
import fr.rhumun.game.worldcraftopengl.content.models.ModelHitbox;
import fr.rhumun.game.worldcraftopengl.content.models.ModelMultiHitbox;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.Hitbox;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.entities.physics.Movements;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.AxisAlignedBB;
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

    /**
     * Compute the bounding box of the entity in world coordinates.
     */
    public AxisAlignedBB getBoundingBox() {
        float minX = (float) (this.getLocation().getX() - radius);
        float minY = (float) (this.getLocation().getY() - height);
        float minZ = (float) (this.getLocation().getZ() - radius);
        float maxX = (float) (this.getLocation().getX() + radius);
        float maxY = (float) this.getLocation().getY();
        float maxZ = (float) (this.getLocation().getZ() + radius);
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void addX(double a){
        if(a==0) return;
        if (!this.isNoClipping && hasBlockInDirection(new Vector3f((a>0 ? 1 : -1), 0, 0))) {
            return;
        }
        this.getLocation().addX(a);
        this.onMove();
    }
    public void addZ(double a){
        if(a==0) return;
        if (!this.isNoClipping && hasBlockInDirection(new Vector3f(0, 0, (a>0 ? 1 : -1)))) {
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
        int maxLevel = (int) Math.ceil(this.height);
        if (yLevel > maxLevel) {
            this.game.errorLog("Trying to get block at yLevel for an Entity with height " + this.height + "\nreturning null...");
            return null;
        }

        Vector3f normalizedDirection = new Vector3f(direction).normalize();

        AxisAlignedBB bb = getBoundingBox();
        double targetX = this.getLocation().getX() + normalizedDirection.get(0) * this.radius;
        double baseY = bb.minY;
        double targetY = baseY + yLevel;
        double targetZ = this.getLocation().getZ() + normalizedDirection.get(2) * this.radius;

        return this.getLocation().getWorld().getBlockAt(targetX, targetY, targetZ, false);
    }

    public boolean hasBlockInDirection(Vector3f direction) {
        Vector3f normalizedDirection = direction;
        AxisAlignedBB bb = getBoundingBox();

        System.out.println(normalizedDirection.toString());

        float eps = 0.001f;
        float espY = 0.1f;
        int maxLevel = (int) Math.ceil(this.height);
        for (int y = 0; y <= maxLevel; y++) {
            double yPos = bb.minY + y;
            if (y == 0) yPos += espY;

            if (normalizedDirection.x > 0) {
                boolean middleP = checkBlockCollision(bb.maxX + eps, yPos, bb.minZ + radius);
                if (middleP || (checkBlockCollision(bb.maxX + eps, yPos, bb.minZ) ||
                        checkBlockCollision(bb.maxX + eps, yPos, bb.maxZ))) {
                    System.out.println("X1");
                    return true;
                }
            } else if (normalizedDirection.x < 0) {
                boolean middleN = checkBlockCollision(bb.minX - eps, yPos, bb.minZ + radius);
                if (middleN || (checkBlockCollision(bb.minX - eps, yPos, bb.minZ) ||
                        checkBlockCollision(bb.minX - eps, yPos, bb.maxZ))) {
                    System.out.println("X2");
                    return true;
                }
            }

            if (normalizedDirection.z > 0) {
                boolean middleP = checkBlockCollision(bb.minX + radius, yPos, bb.maxZ+eps);
                if( middleP || (checkBlockCollision(bb.minX, yPos, bb.maxZ + eps) ||
                        checkBlockCollision(bb.maxX, yPos, bb.maxZ + eps))) {
                    System.out.println("Z1");
                    return true;
                }
            } else if (normalizedDirection.z < 0) {
                boolean middleN = checkBlockCollision(bb.minX + radius, yPos, bb.minZ-eps);
                if (middleN || (checkBlockCollision(bb.minX, yPos, bb.minZ - eps) ||
                        checkBlockCollision(bb.maxX, yPos, bb.minZ - eps))) {
                    System.out.println("Z2");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkBlockCollision(double x, double y, double z) {
        Block block = getWorld().getBlockAt(x,y,z, false);
        if(block == null || block.getMaterial() == null || block.getMaterial().isLiquid()) return false;
        Model model = block.getModel();

        if(model == null) return false;
        if(model.getModel() instanceof ModelHitbox modelHitbox) return modelHitbox.getHitbox(block).intersects(this, block);
        if(model.getModel() instanceof ModelMultiHitbox modelMultiHitbox)
            for(Hitbox hitbox : modelMultiHitbox.getHitboxes(block))
                if(hitbox.intersects(this, block)) return true;

        return false;
    }


    public Block getBlockDown(){
        AxisAlignedBB bb = getBoundingBox();
        return this.getLocation().getWorld().getBlockAt(this.getLocation().getX(), bb.minY - 0.2f, this.getLocation().getZ(), false);
    }

    public boolean hasBlockDown(){
        AxisAlignedBB bb = getBoundingBox();
        float y = bb.minY;

        return checkBlockCollision(bb.minX + 0.001f, y, bb.minZ + 0.001f) ||
               checkBlockCollision(bb.maxX - 0.001f, y, bb.minZ + 0.001f) ||
               checkBlockCollision(bb.minX + 0.001f, y, bb.maxZ - 0.001f) ||
               checkBlockCollision(bb.maxX - 0.001f, y, bb.maxZ - 0.001f);
    }

    public Block getBlockTop(){
        AxisAlignedBB bb = getBoundingBox();
        return this.getLocation().getWorld().getBlockAt(this.getLocation().getX(), bb.maxY + 0.2f, this.getLocation().getZ(), false);
    }

    public boolean hasBlockTop(){
        AxisAlignedBB bb = getBoundingBox();
        float y = bb.maxY + 0.001f;

        return checkBlockCollision(bb.minX + 0.001f, y, bb.minZ + 0.001f) ||
               checkBlockCollision(bb.maxX - 0.001f, y, bb.minZ + 0.001f) ||
               checkBlockCollision(bb.minX + 0.001f, y, bb.maxZ - 0.001f) ||
               checkBlockCollision(bb.maxX - 0.001f, y, bb.maxZ - 0.001f);
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
