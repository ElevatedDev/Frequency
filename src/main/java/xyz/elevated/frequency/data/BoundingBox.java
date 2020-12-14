package xyz.elevated.frequency.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import xyz.elevated.frequency.util.NmsUtil;

import java.util.ArrayList;
import java.util.function.Predicate;

@Getter
public final class BoundingBox {

    private double minX, minY, minZ;
    private double maxX, maxY, maxZ;

    private final long timestamp = System.currentTimeMillis();
    private final World world;

    public BoundingBox(final Location position) {
        this(position.getX(), position.getY(), position.getZ(), position.getWorld());
    }

    public BoundingBox(final double x, final double y, final double z, final World world) {
        this(x, x, y, y, z, z, world);
    }

    public BoundingBox(final double minX, final double maxX, final double minY, final double maxY, final double minZ, final double maxZ, final World world) {
        if (minX < maxX) {
            this.minX = minX;
            this.maxX = maxX;
        } else {
            this.minX = maxX;
            this.maxX = minX;
        }
        if (minY < maxY) {
            this.minY = minY;
            this.maxY = maxY;
        } else {
            this.minY = maxY;
            this.maxY = minY;
        }
        if (minZ < maxZ) {
            this.minZ = minZ;
            this.maxZ = maxZ;
        } else {
            this.minZ = maxZ;
            this.maxZ = minZ;
        }

        this.world = world;
    }

    public double distance(final Location location) {
        return Math.sqrt(Math.min(Math.pow(location.getX() - this.minX, 2), Math.pow(location.getX() - this.maxX, 2)) + Math.min(Math.pow(location.getZ() - this.minZ, 2), Math.pow(location.getZ() - this.maxZ, 2)));
    }

    public double distance(final double x, final double z) {
        final double dx = Math.min(Math.pow(x - minX, 2), Math.pow(x - maxX, 2));
        final double dz = Math.min(Math.pow(z - minZ, 2), Math.pow(z - maxZ, 2));

        return Math.sqrt(dx + dz);
    }

    public double distance(final BoundingBox box) {
        final double dx = Math.min(Math.pow(box.minX - minX, 2), Math.pow(box.maxX - maxX, 2));
        final double dz = Math.min(Math.pow(box.minZ - minZ, 2), Math.pow(box.maxZ - maxZ, 2));

        return Math.sqrt(dx + dz);
    }

    public Vector getDirection() {
        final double centerX = (minX + maxX) / 2.0;
        final double centerY = (minY + maxY) / 2.0;
        final double centerZ = (minZ + maxZ) / 2.0;

        return new Location(world, centerX, centerY, centerZ).getDirection();
    }

    public BoundingBox add(final BoundingBox box) {
        this.minX += box.minX;
        this.minY += box.minY;
        this.minZ += box.minZ;

        this.maxX += box.maxX;
        this.maxY += box.maxY;
        this.maxZ += box.maxZ;

        return this;
    }

    public BoundingBox move(final double x, final double y, final double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public BoundingBox expand(final double x, final double y, final double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public BoundingBox expandMax(final double x, final double y, final double z) {
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }


    public boolean checkBlocks(final Predicate<Material> predicate) {
        final int first = (int) Math.floor(this.minX);
        final int second = (int) Math.ceil(this.maxX);
        final int third = (int) Math.floor(this.minY);
        final int forth = (int) Math.ceil(this.maxY);
        final int fifth = (int) Math.floor(this.minZ);
        final int sixth = (int) Math.ceil(this.maxZ);

        final ArrayList<Block> list = new ArrayList<>();

        list.add(world.getBlockAt(first, third, fifth));

        for (int i = first; i < second; ++i) {
            for (int j = third; j < forth; ++j) {
                for (int k = fifth; k < sixth; ++k) {
                    list.add(world.getBlockAt(i, j, k));
                }
            }
        }


        return list.stream().allMatch(block -> predicate.test(block.getType()));
    }

    public double getCenterX() {
        return (minX + maxX) / 2.0;
    }

    public double getCenterY() {
        return (minY + maxY) / 2.0;
    }

    public double getCenterZ() {
        return (minZ + maxZ) / 2.0;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

@Getter @Setter
final class BlockPosition {

    private int x, y, z;

    public BlockPosition(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Block getBlock(final World world) {
        return NmsUtil.getBlock(new Location(world, x, y, z));
    }
}