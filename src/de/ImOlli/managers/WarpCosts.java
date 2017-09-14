package de.ImOlli.managers;

import org.bukkit.configuration.file.FileConfiguration;

public enum WarpCosts {
    WARP("WarpCosts.Warp"),
    CREATEWARP("WarpCosts.CreateWarp"),
    DELETEWARP("WarpCosts.DeleteWarp"),
    LISTWARPS("WarpCosts.ListWarps");

    private Double costs = 0.0;
    private String configKey;
    private Boolean active = false;

    WarpCosts(String configKey) {
        this.configKey = configKey;
    }

    public void init(FileConfiguration config) {
        costs = config.getDouble(configKey);

        if (costs == 0.0) {
            active = false;
        } else {
            active = true;
        }
    }

    public void setCosts(Double costs) {
        this.costs = costs;
    }

    public String getConfigKey() {
        return configKey;
    }

    public Double getCosts() {
        return costs;
    }

    public Boolean isActive() {
        return active;
    }
}
