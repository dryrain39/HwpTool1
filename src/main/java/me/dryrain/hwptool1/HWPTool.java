package me.dryrain.hwptool1;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bodytext.Section;
import kr.dogfoot.hwplib.object.bodytext.control.Control;
import kr.dogfoot.hwplib.object.bodytext.control.ControlTable;
import kr.dogfoot.hwplib.object.bodytext.control.ControlType;
import kr.dogfoot.hwplib.object.bodytext.control.table.Cell;
import kr.dogfoot.hwplib.object.bodytext.control.table.Row;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.HWPChar;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.HWPCharNormal;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.HWPCharType;
import kr.dogfoot.hwplib.tool.objectfinder.ControlFilter;
import kr.dogfoot.hwplib.tool.objectfinder.ControlFinder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class HWPTool {
    public static void replaceStringFromParagraph(Paragraph paragraph, String target, String replace) throws UnsupportedEncodingException {
        if (paragraph.getText() == null) {
            return;
        }

        ArrayList<HWPCharNormal> replaceChars = new ArrayList<>();
        for (int i = 0; i < replace.length(); i++) {
            HWPCharNormal hwpCharNormal = new HWPCharNormal();
            hwpCharNormal.setCode((short) replace.charAt(i));
            replaceChars.add(hwpCharNormal);
        }

        // get chars
        ArrayList<HWPChar> hwpChars = paragraph.getText().getCharList();
        // get string
        StringBuilder stringBuilder = new StringBuilder();
        for (HWPChar hwpChar : hwpChars) {
//            System.out.println("(char)hwpChar.getCode() = " + (char) hwpChar.getCode() + " " + (char) hwpChar.getCode());

            stringBuilder.append((char) hwpChar.getCode());
        }

        // target 의 시작점 index
        int index = stringBuilder.toString().indexOf(target);

        if (index != -1) {
            // 먼저 교체할 글자들 삽입
            hwpChars.addAll(index, replaceChars);

            // 글자들이 삽입되었으니 인덱스가 증가함
            index += replace.length();

            // 이후 타겟 스트링 지움
            for (int i = 0; i < target.length(); i++) {
                if (hwpChars.get(index).getType() != HWPCharType.Normal) {
                    continue;
                }

                hwpChars.remove(index);
            }

        }
    }

    public static ArrayList<Paragraph> getParagraph(HWPFile hwpFile) {
        ArrayList<Paragraph> paragraphs = new ArrayList<>();

        // bodyText 에서 paragraphs 가져옴
        for (Section section : hwpFile.getBodyText().getSectionList()) {
            for (Paragraph paragraph : section) {
                paragraphs.add(paragraph);
            }
        }

        // 표 싹 다 긁어서 paragraphs 가져옴
        ArrayList<Control> tableControls = ControlFinder.find(hwpFile, new ControlFilter() {
            @Override
            public boolean isMatched(Control control, Paragraph paragraph, Section section) {
                return control.getType() == ControlType.Table;
            }
        });

        for (Control tableControl : tableControls) {
            ControlTable table = (ControlTable) tableControl;

            ArrayList<Row> rows = table.getRowList();
            ArrayList<Cell> cols = new ArrayList<>();

            for (Row row : rows) {
                cols.addAll(row.getCellList());
            }

            for (Cell col : cols) {
                for (Paragraph paragraph : col.getParagraphList()) {
                    paragraphs.add(paragraph);
                }
            }
        }

        return paragraphs;
    }


    public static void copyPage(HWPFile hwpFile, int section_idx) {
        int blankSection_idx = hwpFile.getBodyText().getSectionList().size();
        hwpFile.getBodyText().addNewSection();

        Section originalSection = hwpFile.getBodyText().getSectionList().get(section_idx);
        Section newSection = hwpFile.getBodyText().getSectionList().get(blankSection_idx);

//        ParagraphCopier copyer = new ParagraphCopier(new DocInfoAdder(hwpFile, hwpFile));
//        for (Paragraph p : list) {
//            Paragraph targetParagraph = targetParaList.addNewParagraph();
//            copyer.copy(p, targetParagraph);
//        }

    }
}
