package my.noveldokusha.core

import android.content.Context
import android.content.res.Configuration
import my.noveldokusha.core.appPreferences.AppLanguage
import my.noveldokusha.core.appPreferences.AppLanguageProvider
import java.util.Locale

object LocaleManager {

    /**
     * Контекст с локалью, выбранной в настройках приложения
     * (APP_LANGUAGE_CODE). Используется в attachBaseContext Application
     * и каждой Activity: без этого экран падает на системный язык.
     */
    fun createAppLocaleContext(context: Context): Context {
        val prefs = context.getSharedPreferences(
            context.packageName + "_preferences", Context.MODE_PRIVATE
        )
        // Na primeira instalação, adota o idioma do Android quando disponível.
        // Depois disso, respeita a escolha manual ou a opção "Seguir sistema".
        if (!prefs.getBoolean("IS_FIRST_LAUNCH_DONE", false)) {
            val systemLocale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                context.resources.configuration.locales[0]
            } else {
                @Suppress("DEPRECATION")
                context.resources.configuration.locale
            }
            val detected = AppLanguageProvider.fromLocale(systemLocale)
            prefs.edit()
                .putString("APP_LANGUAGE_CODE", detected.code)
                .putBoolean("IS_FOLLOW_SYSTEM_LANGUAGE", true)
                .putBoolean("IS_FIRST_LAUNCH_DONE", true)
                .apply()
        }

        val followsSystem = prefs.getBoolean("IS_FOLLOW_SYSTEM_LANGUAGE", false)
        val language = if (followsSystem) {
            val systemLocale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                context.resources.configuration.locales[0]
            } else {
                @Suppress("DEPRECATION")
                context.resources.configuration.locale
            }
            AppLanguageProvider.fromLocale(systemLocale)
        } else {
            val code = prefs.getString("APP_LANGUAGE_CODE", "pt") ?: "pt"
            AppLanguageProvider.fromCode(code) ?: AppLanguage.PORTUGUESE
        }
        return createLocaleContext(context, language)
    }

    fun createLocaleContext(context: Context, language: AppLanguage): Context {
        val locale = language.locale
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    fun applyLocale(context: Context, language: AppLanguage) {
        val locale = language.locale
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    fun applyLocale(context: Context, locale: Locale) {
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    fun getCurrentLocale(context: Context): Locale {
        return context.resources.configuration.locales[0]
    }
}
