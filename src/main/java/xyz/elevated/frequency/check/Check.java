package xyz.elevated.frequency.check;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import xyz.elevated.frequency.alert.AlertManager;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.exempt.type.ExemptType;
import xyz.elevated.frequency.util.LogUtil;

import java.util.Deque;
import java.util.List;

@Getter
public abstract class Check<T> {
    protected final PlayerData playerData;

    private String checkName;
    private int threshold;

    private final AlertManager alertManager = new AlertManager(this);
    private final List<Long> violations = Lists.newArrayList();

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
        alertManager.fail();
    }

    protected boolean isExempt(final ExemptType exemptType) {
        return playerData.getExemptManager().isExempt(exemptType);
    }

    protected boolean isExempt(final ExemptType... exemptTypes) {
        return playerData.getExemptManager().isExempt(exemptTypes);
    }

    public abstract void process(final T object);
}
