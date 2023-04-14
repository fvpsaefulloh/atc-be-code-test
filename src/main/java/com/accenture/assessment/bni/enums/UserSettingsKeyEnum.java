package com.accenture.assessment.bni.enums;

public enum UserSettingsKeyEnum {
    BIOMETRIC_LOGIN("biometric_login", Boolean.class, "false"),
    PUSH_NOTIFICATION("push_notification", Boolean.class, "false"),
    SMS_NOTIFICATION("sms_notification", Boolean.class, "false"),
    SHOW_ONBOARDING("show_onboarding", Boolean.class, "false"),
    WIDGET_ORDER("widget_order", String.class, "1,2,3,4,5");

    private String name;
    private Class classType;
    
    private String defaultValue;

    UserSettingsKeyEnum(String name, Class classType, String defaultValue) {
        this.name = name;
        this.classType = classType;
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getName() {
        return name;
    }

    public Class getClassType() {
        return classType;
    }

    public static UserSettingsKeyEnum of(String name) {
        for (UserSettingsKeyEnum userSettingsKeyEnum : values()) {
            if (userSettingsKeyEnum.getName().equalsIgnoreCase(name)) {
                return userSettingsKeyEnum;
            }
        }
        throw new IllegalArgumentException("User settings key doesn't exist");
    }
}
