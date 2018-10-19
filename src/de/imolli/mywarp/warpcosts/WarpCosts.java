package de.imolli.mywarp.warpcosts;

import org.bukkit.configuration.file.FileConfiguration;

public enum WarpCosts {

    WARP("WarpCosts.Warp", 10.0),
    CREATEWARP("WarpCosts.CreateWarp", 100.0),
    DELETEWARP("WarpCosts.DeleteWarp", 50.0),
    CREATEHOLOGRAM("WarpCosts.CreateHologram", 100.0),
    DELETEHOLOGRAM("WarpCosts.DeleteHologram", 50.0),
    LISTWARPS("WarpCosts.ListWarps", 5.0);

    private Double costs = 0.0;
    private String configKey;
    private Boolean active = false;
    private Double defaultValue;

    WarpCosts(String configKey, Double defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }

    public void init(FileConfiguration config) {
        costs = config.getDouble(configKey);

        active = !(costs == 0.0);
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

    public Double getDefaultValue() {
        return defaultValue;
    }
}
