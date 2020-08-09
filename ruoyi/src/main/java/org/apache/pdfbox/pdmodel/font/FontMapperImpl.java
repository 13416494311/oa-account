//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.apache.pdfbox.pdmodel.font;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import org.apache.fontbox.FontBoxFont;
import org.apache.fontbox.ttf.OpenTypeFont;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.fontbox.type1.Type1Font;

final class FontMapperImpl implements FontMapper {
    private static final FontCache fontCache = new FontCache();
    private FontProvider fontProvider;
    private Map<String, FontInfo> fontInfoByName;
    private final TrueTypeFont lastResortFont;
    private final Map<String, List<String>> substitutes = new HashMap();

    FontMapperImpl() {
        this.substitutes.put("Courier", Arrays.asList("CourierNew", "CourierNewPSMT", "LiberationMono", "NimbusMonL-Regu"));
        this.substitutes.put("Courier-Bold", Arrays.asList("CourierNewPS-BoldMT", "CourierNew-Bold", "LiberationMono-Bold", "NimbusMonL-Bold"));
        this.substitutes.put("Courier-Oblique", Arrays.asList("CourierNewPS-ItalicMT", "CourierNew-Italic", "LiberationMono-Italic", "NimbusMonL-ReguObli"));
        this.substitutes.put("Courier-BoldOblique", Arrays.asList("CourierNewPS-BoldItalicMT", "CourierNew-BoldItalic", "LiberationMono-BoldItalic", "NimbusMonL-BoldObli"));
        this.substitutes.put("Helvetica", Arrays.asList("ArialMT", "Arial", "LiberationSans", "NimbusSanL-Regu"));
        this.substitutes.put("Helvetica-Bold", Arrays.asList("Arial-BoldMT", "Arial-Bold", "LiberationSans-Bold", "NimbusSanL-Bold"));
        this.substitutes.put("Helvetica-Oblique", Arrays.asList("Arial-ItalicMT", "Arial-Italic", "Helvetica-Italic", "LiberationSans-Italic", "NimbusSanL-ReguItal"));
        this.substitutes.put("Helvetica-BoldOblique", Arrays.asList("Arial-BoldItalicMT", "Helvetica-BoldItalic", "LiberationSans-BoldItalic", "NimbusSanL-BoldItal"));
        this.substitutes.put("Times-Roman", Arrays.asList("TimesNewRomanPSMT", "TimesNewRoman", "TimesNewRomanPS", "LiberationSerif", "NimbusRomNo9L-Regu"));
        this.substitutes.put("Times-Bold", Arrays.asList("TimesNewRomanPS-BoldMT", "TimesNewRomanPS-Bold", "TimesNewRoman-Bold", "LiberationSerif-Bold", "NimbusRomNo9L-Medi"));
        this.substitutes.put("Times-Italic", Arrays.asList("TimesNewRomanPS-ItalicMT", "TimesNewRomanPS-Italic", "TimesNewRoman-Italic", "LiberationSerif-Italic", "NimbusRomNo9L-ReguItal"));
        this.substitutes.put("Times-BoldItalic", Arrays.asList("TimesNewRomanPS-BoldItalicMT", "TimesNewRomanPS-BoldItalic", "TimesNewRoman-BoldItalic", "LiberationSerif-BoldItalic", "NimbusRomNo9L-MediItal"));
        this.substitutes.put("Symbol", Arrays.asList("Symbol", "SymbolMT", "StandardSymL"));
        this.substitutes.put("ZapfDingbats", Arrays.asList("ZapfDingbatsITC", "Dingbats", "MS-Gothic"));
        this.substitutes.put("STSongStd-Light", Arrays.asList("ArialUnicodeMS","DengXian"));
        Iterator i$ = Standard14Fonts.getNames().iterator();

        while(i$.hasNext()) {
            String baseName = (String)i$.next();
            if (!this.substitutes.containsKey(baseName)) {
                String mappedName = Standard14Fonts.getMappedFontName(baseName);
                this.substitutes.put(baseName, this.copySubstitutes(mappedName));
            }
        }

        try {
            String ttfName = "org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf";
            URL url = FontMapper.class.getClassLoader().getResource(ttfName);
            if (url == null) {
                throw new IOException("Error loading resource: " + ttfName);
            } else {
                InputStream ttfStream = url.openStream();
                TTFParser ttfParser = new TTFParser();
                this.lastResortFont = ttfParser.parse(ttfStream);
            }
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }
    }

    public synchronized void setProvider(FontProvider fontProvider) {
        this.fontInfoByName = this.createFontInfoByName(fontProvider.getFontInfo());
        this.fontProvider = fontProvider;
    }

    public synchronized FontProvider getProvider() {
        if (this.fontProvider == null) {
            this.setProvider(FontMapperImpl.DefaultFontProvider.INSTANCE);
        }

        return this.fontProvider;
    }

    public FontCache getFontCache() {
        return fontCache;
    }

    private Map<String, FontInfo> createFontInfoByName(List<? extends FontInfo> fontInfoList) {
        Map<String, FontInfo> map = new LinkedHashMap();
        Iterator i$ = fontInfoList.iterator();

        while(i$.hasNext()) {
            FontInfo info = (FontInfo)i$.next();
            i$ = this.getPostScriptNames(info.getPostScriptName()).iterator();

            while(i$.hasNext()) {
                String name = (String)i$.next();
                map.put(name, info);
            }
        }

        return map;
    }

    private Set<String> getPostScriptNames(String postScriptName) {
        Set<String> names = new HashSet();
        names.add(postScriptName);
        names.add(postScriptName.replaceAll("-", ""));
        return names;
    }

    private List<String> copySubstitutes(String postScriptName) {
        return new ArrayList((Collection)this.substitutes.get(postScriptName));
    }

    public void addSubstitute(String match, String replace) {
        if (!this.substitutes.containsKey(match)) {
            this.substitutes.put(match, new ArrayList());
        }

        ((List)this.substitutes.get(match)).add(replace);
    }

    private List<String> getSubstitutes(String postScriptName) {
        List<String> subs = (List)this.substitutes.get(postScriptName.replaceAll(" ", ""));
        return subs != null ? subs : Collections.emptyList();
    }

    private String getFallbackFontName(PDFontDescriptor fontDescriptor) {
        String fontName;
        if (fontDescriptor != null) {
            boolean isBold = false;
            String name = fontDescriptor.getFontName();
            if (name != null) {
                String lower = fontDescriptor.getFontName().toLowerCase();
                isBold = lower.contains("bold") || lower.contains("black") || lower.contains("heavy");
            }

            if (fontDescriptor.isFixedPitch()) {
                fontName = "Courier";
                if (isBold && fontDescriptor.isItalic()) {
                    fontName = fontName + "-BoldOblique";
                } else if (isBold) {
                    fontName = fontName + "-Bold";
                } else if (fontDescriptor.isItalic()) {
                    fontName = fontName + "-Oblique";
                }
            } else if (fontDescriptor.isSerif()) {
                fontName = "Times";
                if (isBold && fontDescriptor.isItalic()) {
                    fontName = fontName + "-BoldItalic";
                } else if (isBold) {
                    fontName = fontName + "-Bold";
                } else if (fontDescriptor.isItalic()) {
                    fontName = fontName + "-Italic";
                } else {
                    fontName = fontName + "-Roman";
                }
            } else {
                fontName = "Helvetica";
                if (isBold && fontDescriptor.isItalic()) {
                    fontName = fontName + "-BoldOblique";
                } else if (isBold) {
                    fontName = fontName + "-Bold";
                } else if (fontDescriptor.isItalic()) {
                    fontName = fontName + "-Oblique";
                }
            }
        } else {
            fontName = "Times-Roman";
        }

        return fontName;
    }

    public FontMapping<TrueTypeFont> getTrueTypeFont(String baseFont, PDFontDescriptor fontDescriptor) {
        TrueTypeFont ttf = (TrueTypeFont)this.findFont(FontFormat.TTF, baseFont);
        if (ttf != null) {
            return new FontMapping(ttf, false);
        } else {
            String fontName = this.getFallbackFontName(fontDescriptor);
            ttf = (TrueTypeFont)this.findFont(FontFormat.TTF, fontName);
            if (ttf == null) {
                ttf = this.lastResortFont;
            }

            return new FontMapping(ttf, true);
        }
    }

    public FontMapping<FontBoxFont> getFontBoxFont(String baseFont, PDFontDescriptor fontDescriptor) {
        FontBoxFont font = this.findFontBoxFont(baseFont);
        if (font != null) {
            return new FontMapping(font, false);
        } else {
            String fallbackName = this.getFallbackFontName(fontDescriptor);
            font = this.findFontBoxFont(fallbackName);
            if (font == null) {
                font = this.lastResortFont;
            }

            return new FontMapping((FontBoxFont)font, true);
        }
    }

    private FontBoxFont findFontBoxFont(String postScriptName) {
        Type1Font t1 = (Type1Font)this.findFont(FontFormat.PFB, postScriptName);
        if (t1 != null) {
            return t1;
        } else {
            TrueTypeFont ttf = (TrueTypeFont)this.findFont(FontFormat.TTF, postScriptName);
            if (ttf != null) {
                return ttf;
            } else {
                OpenTypeFont otf = (OpenTypeFont)this.findFont(FontFormat.OTF, postScriptName);
                return otf != null ? otf : null;
            }
        }
    }

    private FontBoxFont findFont(FontFormat format, String postScriptName) {
        if (postScriptName == null) {
            return null;
        } else {
            if (this.fontProvider == null) {
                this.getProvider();
            }

            FontInfo info = this.getFont(format, postScriptName);
            if (info != null) {
                return info.getFont();
            } else {
                info = this.getFont(format, postScriptName.replaceAll("-", ""));
                if (info != null) {
                    return info.getFont();
                } else {
                    Iterator i$ = this.getSubstitutes(postScriptName).iterator();

                    do {
                        if (!i$.hasNext()) {
                            info = this.getFont(format, postScriptName.replaceAll(",", "-"));
                            if (info != null) {
                                return info.getFont();
                            }

                            info = this.getFont(format, postScriptName + "-Regular");
                            if (info != null) {
                                return info.getFont();
                            }

                            return null;
                        }

                        String substituteName = (String)i$.next();
                        info = this.getFont(format, substituteName);
                    } while(info == null);

                    return info.getFont();
                }
            }
        }
    }

    private FontInfo getFont(FontFormat format, String postScriptName) {
        if (postScriptName.contains("+")) {
            postScriptName = postScriptName.substring(postScriptName.indexOf(43) + 1);
        }

        FontInfo info = (FontInfo)this.fontInfoByName.get(postScriptName);
        return info != null && info.getFormat() == format ? info : null;
    }

    public CIDFontMapping getCIDFont(String baseFont, PDFontDescriptor fontDescriptor, PDCIDSystemInfo cidSystemInfo) {
        OpenTypeFont otf1 = (OpenTypeFont)this.findFont(FontFormat.OTF, baseFont);
        if (otf1 != null) {
            return new CIDFontMapping(otf1, (FontBoxFont)null, false);
        } else {
            TrueTypeFont ttf = (TrueTypeFont)this.findFont(FontFormat.TTF, baseFont);
            if (ttf != null) {
                return new CIDFontMapping((OpenTypeFont)null, ttf, false);
            } else {
                if (cidSystemInfo != null) {
                    String collection = cidSystemInfo.getRegistry() + "-" + cidSystemInfo.getOrdering();
                    if (collection.equals("Adobe-GB1") || collection.equals("Adobe-CNS1") || collection.equals("Adobe-Japan1") || collection.equals("Adobe-Korea1")) {
                        PriorityQueue<FontMapperImpl.FontMatch> queue = this.getFontMatches(fontDescriptor, cidSystemInfo);
                        FontMapperImpl.FontMatch bestMatch = (FontMapperImpl.FontMatch)queue.poll();
                        if (bestMatch != null) {
                            FontBoxFont font = bestMatch.info.getFont();
                            if (font instanceof OpenTypeFont) {
                                return new CIDFontMapping((OpenTypeFont)font, (FontBoxFont)null, true);
                            }

                            return new CIDFontMapping((OpenTypeFont)null, font, true);
                        }
                    }
                }

                return new CIDFontMapping((OpenTypeFont)null, this.lastResortFont, true);
            }
        }
    }

    private PriorityQueue<FontMapperImpl.FontMatch> getFontMatches(PDFontDescriptor fontDescriptor, PDCIDSystemInfo cidSystemInfo) {
        PriorityQueue<FontMapperImpl.FontMatch> queue = new PriorityQueue(20);
        Iterator i$ = this.fontInfoByName.values().iterator();

        while(true) {
            FontInfo info;
            do {
                if (!i$.hasNext()) {
                    return queue;
                }

                info = (FontInfo)i$.next();
            } while(cidSystemInfo != null && !this.isCharSetMatch(cidSystemInfo, info));

            FontMapperImpl.FontMatch match = new FontMapperImpl.FontMatch(info);
            if (fontDescriptor.getPanose() != null && info.getPanose() != null) {
                PDPanoseClassification panose = fontDescriptor.getPanose().getPanose();
                if (panose.getFamilyKind() == info.getPanose().getFamilyKind()) {
                    if (panose.getSerifStyle() == info.getPanose().getSerifStyle()) {
                        match.score += 2.0D;
                    } else if (panose.getSerifStyle() >= 2 && panose.getSerifStyle() <= 5 && info.getPanose().getSerifStyle() >= 2 && info.getPanose().getSerifStyle() <= 5) {
                        ++match.score;
                    } else if (panose.getSerifStyle() >= 11 && panose.getSerifStyle() <= 13 && info.getPanose().getSerifStyle() >= 11 && info.getPanose().getSerifStyle() <= 13) {
                        ++match.score;
                    } else if (panose.getSerifStyle() != 0 && info.getPanose().getSerifStyle() != 0) {
                        --match.score;
                    }

                    int weight = info.getPanose().getWeight();
                    int weightClass = info.getWeightClassAsPanose();
                    if (Math.abs(weight - weightClass) > 2) {
                        weight = weightClass;
                    }

                    if (panose.getWeight() == weight) {
                        match.score += 2.0D;
                    } else if (panose.getWeight() > 1 && weight > 1) {
                        float dist = (float)Math.abs(panose.getWeight() - weight);
                        match.score += 1.0D - (double)dist * 0.5D;
                    }
                }
            } else if (fontDescriptor.getFontWeight() > 0.0F && info.getWeightClass() > 0) {
                float dist = Math.abs(fontDescriptor.getFontWeight() - (float)info.getWeightClass());
                match.score += 1.0D - (double)(dist / 100.0F) * 0.5D;
            }

            queue.add(match);
        }
    }

    private boolean isCharSetMatch(PDCIDSystemInfo cidSystemInfo, FontInfo info) {
        if (info.getCIDSystemInfo() != null) {
            return info.getCIDSystemInfo().getRegistry().equals(cidSystemInfo.getRegistry()) && info.getCIDSystemInfo().getOrdering().equals(cidSystemInfo.getOrdering());
        } else {
            long codePageRange = info.getCodePageRange();
            long JIS_JAPAN = 131072L;
            long CHINESE_SIMPLIFIED = 262144L;
            long KOREAN_WANSUNG = 524288L;
            long CHINESE_TRADITIONAL = 1048576L;
            long KOREAN_JOHAB = 2097152L;
            if (cidSystemInfo.getOrdering().equals("GB1") && (codePageRange & CHINESE_SIMPLIFIED) == CHINESE_SIMPLIFIED) {
                return true;
            } else if (cidSystemInfo.getOrdering().equals("CNS1") && (codePageRange & CHINESE_TRADITIONAL) == CHINESE_TRADITIONAL) {
                return true;
            } else if (cidSystemInfo.getOrdering().equals("Japan1") && (codePageRange & JIS_JAPAN) == JIS_JAPAN) {
                return true;
            } else {
                return cidSystemInfo.getOrdering().equals("Korea1") && (codePageRange & KOREAN_WANSUNG) == KOREAN_WANSUNG || (codePageRange & KOREAN_JOHAB) == KOREAN_JOHAB;
            }
        }
    }

    private FontMapperImpl.FontMatch printMatches(PriorityQueue<FontMapperImpl.FontMatch> queue) {
        FontMapperImpl.FontMatch bestMatch = (FontMapperImpl.FontMatch)queue.peek();
        System.out.println("-------");

        while(!queue.isEmpty()) {
            FontMapperImpl.FontMatch match = (FontMapperImpl.FontMatch)queue.poll();
            FontInfo info = match.info;
            System.out.println(match.score + " | " + info.getMacStyle() + " " + info.getFamilyClass() + " " + info.getPanose() + " " + info.getCIDSystemInfo() + " " + info.getPostScriptName() + " " + info.getFormat());
        }

        System.out.println("-------");
        return bestMatch;
    }

    private static class FontMatch implements Comparable<FontMapperImpl.FontMatch> {
        double score;
        final FontInfo info;

        FontMatch(FontInfo info) {
            this.info = info;
        }

        public int compareTo(FontMapperImpl.FontMatch match) {
            return Double.compare(match.score, this.score);
        }
    }

    private static class DefaultFontProvider {
        private static final FontProvider INSTANCE;

        private DefaultFontProvider() {
        }

        static {
            INSTANCE = new FileSystemFontProvider(FontMapperImpl.fontCache);
        }
    }
}
