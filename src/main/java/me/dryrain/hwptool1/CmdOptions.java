package me.dryrain.hwptool1;

import org.apache.commons.cli.*;

public class CmdOptions {
    public static Options getOptions(){
        Options options = new Options();

        Option template = new Option("t", "template", true, "Template file name");
        options.addOption(template);

        Option dataFileName = new Option("d", "data", true, "Data file name");
        options.addOption(dataFileName);

        Option macroName = new Option("m", "macro", true, "Merge macro file name");
        options.addOption(macroName);

        Option output = new Option("o", "output", true, "output directory");
        options.addOption(output);

        return options;
    }
}
