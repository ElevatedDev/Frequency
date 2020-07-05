package xyz.elevated.frequency.util;

import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import java.lang.reflect.Field;

public final class ReflectionUtil {

    public ReflectionUtil() throws Exception {
        throw new Exception("You may not initialise utility classes.");
    }

    /**
     *
     * @param object - The object you want to grab and modify the declared field from
     * @param fieldName - The name of the declared field you would like to modify.
     * @param alteration - The new value you want to give to the field.
     */
    public static void modifyDeclaredField(final Object object, final String fieldName, final Object alteration) {
        try {
            final Field declaredField = object.getClass().getDeclaredField(fieldName);

            declaredField.setAccessible(true);
            declaredField.set(object, alteration);

            declaredField.setAccessible(false);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param object - The object you want to modify the field from.
     * @param fieldName - The name of the field you would like to modify.
     * @param alteration - The new value you want to give to the field.
     */
    public static void modifyField(final Object object, final String fieldName, final Object alteration) {
        try {
            final Field field = object.getClass().getField(fieldName);

            field.setAccessible(true);
            field.set(object, alteration);

            field.setAccessible(false);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param clazz - The class you want to grab a value from.
     * @param fieldName - The name of the field you want to get the value from.
     * @param type - The type of the value of the field.
     * @param instance - The instance for the class
     * @return - The value if found.
     */
    public static <T> T getFieldValue(final Class<?> clazz, final String fieldName, final Class<?> type, final Object instance) {
        final Field field = getField(clazz, fieldName, type);

        field.setAccessible(true);

        try {
            //noinspection unchecked
            return (T) field.get(instance);
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException("Failed to get value of field '" + field.getName() + "'");
        }
    }

    /**
     *
     * @param clazz - The class you want to grab the value from.
     * @param name - The name of the field you want to grab.
     * @param type - The type of data the field has..
     * @return
     */
    private static Field getField(final Class<?> clazz, final String name, final Class<?> type) {
        try {
            final Field field = clazz.getDeclaredField(name);

            field.setAccessible(true);

            if (field.getType() != type) {
                throw new IllegalStateException("Invalid action for field '" + name + "' (expected " + type.getName() + ", got " + field.getType().getName() + ")");
            }
            return field;
        }
        catch (final Exception e) {
            throw new RuntimeException("Failed to get field '" + name + "'");
        }
    }
}