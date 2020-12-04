package me.dryrain.hwptool1;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.writer.HWPWriter;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static me.dryrain.hwptool1.HWPTool.getParagraph;
import static me.dryrain.hwptool1.HWPTool.replaceStringFromParagraph;


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

        List<String[]> csvData = CSV.readData(data_path);
        String[] head = {};

        FileWriter fw = new FileWriter(macro_path, true);

        File directory = new File(output_path);
        String path = directory.getCanonicalPath() + System.getProperty("file.separator");
        if (!directory.exists()) {
            directory.mkdirs();
        }


        if (csvData.size() > 1) {
            head = csvData.get(0);
        }

        HWPFile hwpFile = HWPReader.fromFile(template_path);
        if (hwpFile != null) {
            for (int r = 1; r < csvData.size(); r++) {
                for (int c = 1; c < csvData.get(r).length; c++) {
                    ArrayList<Paragraph> paragraphs = getParagraph(hwpFile);
                    for (Paragraph paragraph : paragraphs) {
                        replaceStringFromParagraph(paragraph, head[c].strip(), csvData.get(r)[c].strip());
                    }
                }
                System.out.println("Writing " + output_path + csvData.get(r)[0] + ".hwp");
                HWPWriter.toFile(hwpFile, output_path +  csvData.get(r)[0] + ".hwp");
                try {
                    fw.write("HAction.GetDefault(\"InsertFile\", HParameterSet.HInsertFile.HSet); with (HParameterSet.HInsertFile) { FileName = \"" + (path + csvData.get(r)[0]).replace("\\", "\\\\") + ".hwp\"; KeepSection = 1; KeepCharshape = 0; KeepParashape = 0; KeepStyle = 0; } HAction.Execute(\"InsertFile\", HParameterSet.HInsertFile.HSet);\n");
                } catch (Exception ignored) {
                }
            }
        }

        try {
            fw.close();
        } catch (Exception ignored) {

        }

    }
}