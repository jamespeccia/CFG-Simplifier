import java.util.HashSet;
import java.util.LinkedList;

public class ProductionRule {

    public HashSet<ProductionRule> out;
    public LinkedList<String> productions;
    private final HashSet<ProductionRule> in;
    private final Character symbol;


    private boolean isStartSymbol;


    public ProductionRule(char symbol) {
        this.symbol = symbol;
        this.in = new HashSet<>();
        this.out = new HashSet<>();
        this.productions = new LinkedList<>();
        this.isStartSymbol = false;
    }

    public void setStartSymbol() {
        this.isStartSymbol = true;
    }

    public Character getSymbol() {
        return symbol;
    }

    public void addIn(ProductionRule productionRule) {
        this.in.add(productionRule);
    }

    public void addOut(ProductionRule productionRule) {
        this.out.add(productionRule);
    }

    public void addProduction(String production) {
        this.productions.add(production.substring(4));
    }

    public boolean isUnreachable() {
        return !isStartSymbol && (in.size() == 0 || (in.size() == 1 && in.contains(this)));
    }

    public boolean isUnproductive() {
        return !containsTerminal() && (out.size() == 0 || (out.size() == 1 && out.contains(this)));
    }

    private boolean containsTerminal() {
        for (String production : productions) {
            char lastChar = production.charAt(production.length() - 1);
            if (Character.isLowerCase(lastChar) || lastChar == '$') {
                return true;
            }
        }
        return false;
    }

    public boolean containsEpsilon() {
        for (String production : productions) {
            char lastChar = production.charAt(production.length() - 1);
            if (lastChar == '$') {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        String result = "";

        for (String production : productions) {
            result += symbol + ":: " + production + "\n";
        }

        return result;
    }
}
