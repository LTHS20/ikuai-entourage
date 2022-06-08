package ltd.lths.wireless.ikuai.entourage;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import taboolib.common.LifeCycle;
import taboolib.common.TabooLibCommon;

import java.util.Arrays;

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.Main
 *
 * @author Score2
 * @since 2022/06/08 1:34
 */
public class Main {

    public static void main(String[] args) {
        try {
            OptionParser parser = new OptionParser() {
                {
                    acceptsAll(Arrays.asList("?", "help"), "获取帮助");
                    acceptsAll(Arrays.asList("test"), "随意调试");
                }
            };
            OptionSet options = parser.parse(args);

            if (options.has("?")) {
                parser.printHelpOn(System.out);
                return;
            }
        } catch (Throwable t) {

        }
        TabooLibCommon.testSetup();
    }
}
