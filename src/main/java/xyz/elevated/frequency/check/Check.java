package xyz.elevated.frequency.check;

import lombok.Getter;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.util.LogUtil;

@Getter
public abstract class Check<T> {
    protected final PlayerData playerData;

    private String checkName;
    private int threshold;

    public Check(final PlayerData playerData) {
        this.playerData = playerData;

        final Class<?> checkClass = this.getClass();

        if (checkClass.isAnnotationPresent(CheckData.class)) {
            final CheckData checkData = checkClass.getAnnotation(CheckData.class);

            this.checkName = checkData.name();
            this.threshold = checkData.threshold();
        } else {
            LogUtil.log("Check annotation not found in class: " + checkClass.getSimpleName());
        }
    }

    protected void fail() {

    }

    protected boolean isExempt(final ExemptType exceptType) {
        return playerData.getExceptManager().isExempt(exceptType);
    }

    protected boolean isExempt(final ExemptType... exceptTypes) {
        return playerData.getExceptManager().isExempt(exceptTypes);
    }

    public abstract void process(final T object);
}
