package me.dryrain.hwptool1;

import org.apache.commons.cli.*;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(CmdOptions.getOptions(), args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("hwpTool1", CmdOptions.getOptions());

            System.exit(1);
        }

        String template_path = cmd.getOptionValue('t', "./template.hwp");
        String data_path = cmd.getOptionValue('d', "./data.csv");
        String macro_path = cmd.getOptionValue('m', "./macro.txt");
        String output_path = cmd.getOptionValue('o', "./output/");

        File input_hwp = new File(template_path);
        File data_csv = new File(data_path);
        File macro_txt = new File(macro_path);
        if (!input_hwp.exists() || !data_csv.exists()) {
            System.out.println(template_path + " OR " + data_path + " not found.");
            return;
        }
        if (macro_txt.exists()) {
            macro_txt.delete();
        }

        try{
            Action.replace(template_path, data_path, macro_path, output_path);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}