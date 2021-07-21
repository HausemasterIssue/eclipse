package xyz.aesthetical.eclipse.util.translator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public abstract class Translator {
    public abstract String translate(Locale from, Locale to, String text);
    public abstract String getRequestURL();

    public String toEncoded(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public URL getUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public URLConnection getConnection(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        return connection;
    }

    public enum Locale {
        Azerbaijan("az"),
        Albanian("sq"),
        Amharic("am"),
        English("en"),
        Arabic("ar"),
        Armenian("hy"),
        Afrikaans("af"),
        Basque("eu"),
        Bashkir("ba"),
        Belarusian("be"),
        Bengali("bn"),
        Burmese("my"),
        Bulgarian("bg"),
        Bosnian("bs"),
        Welsh("cy"),
        Hungarian("hu"),
        Vietnamese("vi"),
        Haitian("ht"),
        Galician("gl"),
        Dutch("nl"),
        HillMari("mrj"),
        Greek("el"),
        Georgian("ka"),
        Gujarati("gu"),
        Danish("da"),
        Hebrew("he"),
        Yiddish("yi"),
        Indonesian("id"),
        Irish("ga"),
        Italian("it"),
        Icelandic("is"),
        Spanish("es"),
        Kazakh("kk"),
        Kannada("kn"),
        Catalan("ca"),
        Kyrgyz("ky"),
        Chinese("zh"),
        Korean("ko"),
        Xhosa("xh"),
        Khmer("km"),
        Laotian("lo"),
        Latin("la"),
        Latvian("lv"),
        Lithuanian("lt"),
        Luxembourgish("lb"),
        Malagasy("mg"),
        Malay("ms"),
        Malayalam("ml"),
        Maltese("mt"),
        Macedonian("mk"),
        Maori("mi"),
        Marathi("mr"),
        Mari("mhr"),
        Mongolian("mn"),
        German("de"),
        Nepali("ne"),
        Norwegian("no"),
        Russian("ru"),
        Punjabi("pa"),
        Papiamento("pap"),
        Persian("fa"),
        Polish("pl"),
        Portuguese("pt"),
        Romanian("ro"),
        Cebuano("ceb"),
        Serbian("sr"),
        Sinhala("si"),
        Slovakian("sk"),
        Slovenian("sl"),
        Swahili("sw"),
        Sundanese("su"),
        Tajik("tg"),
        Thai("th"),
        Tagalog("tl"),
        Tamil("ta"),
        Tatar("tt"),
        Telugu("te"),
        Turkish("tr"),
        Udmurt("udm"),
        Uzbek("uz"),
        Ukrainian("uk"),
        Urdu("ur"),
        Finnish("fi"),
        French("fr"),
        Hindi("hi"),
        Croatian("hr"),
        Czech("cs"),
        Swedish("sv"),
        Scottish("gd"),
        Estonian("et"),
        Esperanto("eo"),
        Javanese("jv"),
        Japanese("ja");

        private String code;

        Locale(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }

        public static Locale getByCode(String code) {
            for (Locale language : Locale.values()) {
                if (language.getCode().equalsIgnoreCase(code)) {
                    return language;
                }
            }

            return null;
        }
    }
}
