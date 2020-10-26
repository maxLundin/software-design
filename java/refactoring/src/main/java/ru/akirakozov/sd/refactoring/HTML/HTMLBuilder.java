package ru.akirakozov.sd.refactoring.HTML;

public class HTMLBuilder {

    final StringBuilder sb;
    final static String startPt = "<html><body>";
    final static String finalPt = "</body></html>";

    public HTMLBuilder() {
        this.sb = new StringBuilder(startPt).append('\n');
    }

    public void addHTML(String str) {
        if (!str.isEmpty())
            this.sb.append(str).append('\n');
    }

    public String getHTML() {
        sb.append(finalPt).append('\n');
        return sb.toString();
    }
}
