package de.imolli.mywarp.warp.warpflags;

public enum WarpFlag {

    NOCOSTS("NoCosts"),
    ONLYPERMITTEDCANWARP("OnlyPermittedCanWarp"),
    GUIINVISIBLE("GuiInvisible"),
    ONLYCREATORCANWARP("OnlyCreatorCanWarp");

    private Boolean defaultAdded;
    private String name = this.toString();
    private String displayname;

    WarpFlag(String displayname) {
        this.displayname = displayname;
    }

    public Boolean getDefault() {
        return defaultAdded;
    }

    public void setDefault(Boolean defaultAdded) {
        this.defaultAdded = defaultAdded;
    }

    public String getName() {
        return name;
    }

    public String getDisplayname() {
        return displayname;
    }
}
