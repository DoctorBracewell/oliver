package dev.brace.config;

public enum GlobalConfigurationOption {
    Username("username", "The default username for authoring deltas.", "STRING", String.class);

    private String name;
    private String description;
    private String paramLabel;

    private Class type;

    GlobalConfigurationOption(String name, String description, String paramLabel, Class type) {
        this.name = name;
        this.description = description;
        this.paramLabel = paramLabel;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getParamLabel() {
        return paramLabel;
    }

    public Class getType() {
        return type;
    }
}
