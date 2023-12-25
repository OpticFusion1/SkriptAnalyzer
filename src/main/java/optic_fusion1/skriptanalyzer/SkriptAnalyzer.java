package optic_fusion1.skriptanalyzer;

import optic_fusion1.kitsune.Kitsune;
import optic_fusion1.skriptanalyzer.analyzer.SkAnalyzer;
import org.pf4j.Plugin;

public class SkriptAnalyzer extends Plugin {

    private SkAnalyzer analyzer = new SkAnalyzer();

    @Override
    public void start() {
        Kitsune.getInstance().getAnalyzerManager().addAnalyzerForExtension("sk", analyzer);
    }

}
