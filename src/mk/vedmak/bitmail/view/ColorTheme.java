package mk.vedmak.bitmail.view;

public enum ColorTheme {
    LIGHT,
    DEFAULT,
    DARK;

    public static String getCssPath(ColorTheme colorTheme) {
        switch (colorTheme) {
            case LIGHT: return "css/themeLight.css";
            case DARK: return "css/themeDark.css";
            case DEFAULT: return "css/themeDefault.css";
            default: return null;
        }
    }
}
