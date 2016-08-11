package org.tasks.themes;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatDelegate;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;

import org.tasks.R;

import java.util.ArrayList;
import java.util.List;

public class ThemeCache {

    private final List<ThemeBase> themes = new ArrayList<>();
    private final List<ThemeColor> colors = new ArrayList<>();
    private final List<ThemeAccent> accents = new ArrayList<>();
    private final List<WidgetTheme> widgetThemes = new ArrayList<>();
    private final List<LEDColor> led = new ArrayList<>();
    private final ThemeColor untaggedColor;

    public ThemeCache(Context context) {
        Resources resources = context.getResources();

        themes.add(new ThemeBase(context.getString(R.string.theme_light), 0, resources.getColor(R.color.md_background_light), AppCompatDelegate.MODE_NIGHT_NO));
        themes.add(new ThemeBase(context.getString(R.string.theme_black), 1, resources.getColor(R.color.widget_background_black), AppCompatDelegate.MODE_NIGHT_YES));
        themes.add(new ThemeBase(context.getString(R.string.theme_dark), 2, resources.getColor(R.color.md_background_dark), AppCompatDelegate.MODE_NIGHT_YES));
        themes.add(new ThemeBase(context.getString(R.string.theme_wallpaper), 3, resources.getColor(R.color.black_38), AppCompatDelegate.MODE_NIGHT_YES));
        themes.add(new ThemeBase(context.getString(R.string.theme_day_night), 4, resources.getColor(R.color.md_background_light), AppCompatDelegate.MODE_NIGHT_AUTO));

        String[] colorNames = resources.getStringArray(R.array.colors);
        for (int i = 0 ; i < ThemeColor.COLORS.length ; i++) {
            Resources.Theme theme = new ContextThemeWrapper(context, ThemeColor.COLORS[i]).getTheme();
            colors.add(new ThemeColor(
                    colorNames[i],
                    i,
                    resolveAttribute(theme, R.attr.colorPrimary),
                    resolveAttribute(theme, R.attr.colorPrimaryDark),
                    resolveAttribute(theme, R.attr.actionBarPrimaryText),
                    resolveBoolean(theme, R.attr.dark_status_bar)));
        }
        String[] accentNames = resources.getStringArray(R.array.accents);
        for (int i = 0 ; i < ThemeAccent.ACCENTS.length ; i++) {
            Resources.Theme theme = new ContextThemeWrapper(context, ThemeAccent.ACCENTS[i]).getTheme();
            accents.add(new ThemeAccent(
                    accentNames[i],
                    i,
                    resolveAttribute(theme, R.attr.colorAccent)));
        }
        String[] widgetBackgroundNames = resources.getStringArray(R.array.widget_background);
        for (int i = 0; i < WidgetTheme.BACKGROUNDS.length ; i++) {
            widgetThemes.add(new WidgetTheme(
                    widgetBackgroundNames[i],
                    i,
                    resources.getColor(WidgetTheme.BACKGROUNDS[i]),
                    resources.getColor(i == 0 ? R.color.black_87 : R.color.white_100)));
        }
        String []ledNames = resources.getStringArray(R.array.led);
        for (int i = 0 ; i < LEDColor.LED_COLORS.length ; i++) {
            led.add(new LEDColor(
                    ledNames[i],
                    resources.getColor(LEDColor.LED_COLORS[i])));
        }
        untaggedColor = new ThemeColor(null, 19, resources.getColor(R.color.tag_color_none_background), 0, resources.getColor(R.color.black_87), false);
    }

    public WidgetTheme getWidgetTheme(int index) {
        return widgetThemes.get(index);
    }

    public ThemeBase getThemeBase(int index) {
        return themes.get(index);
    }

    public ThemeColor getThemeColor(int index) {
        return colors.get(index);
    }

    public ThemeAccent getThemeAccent(int index) {
        return accents.get(index);
    }

    public ThemeColor getUntaggedColor() {
        return untaggedColor;
    }

    public LEDColor getLEDColor(int index) {
        return led.get(index);
    }

    public int getLEDColorCount() {
        return led.size();
    }

    private static int resolveAttribute(Resources.Theme theme, int attribute) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attribute, typedValue, true);
        return typedValue.data;
    }

    private boolean resolveBoolean(Resources.Theme theme, int attribute) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attribute, typedValue, false);
        return typedValue.data != 0;
    }
}
