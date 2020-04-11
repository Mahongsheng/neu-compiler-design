package parser.LL1;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnalysisTableItem {
    private char Vn;
    private char Vt;
    private int changeToNextGrammar;
}