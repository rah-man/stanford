import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.*;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class ParserTest {

    /**
     * This example shows a few more ways of providing input to a parser.
     * <p>
     * Usage: ParserDemo2 [grammar [textFile]]
     */
    public static void main(String[] args) throws Exception {

        /*
        edu/stanford/nlp/models/lexparser/englishFactored.ser.gz
        edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz
        edu/stanford/nlp/models/lexparser/englishRNN.ser.gz
        */

        HashMap<String, String> gramMap = new HashMap<String, String>();
        gramMap.put("factored", "edu/stanford/nlp/models/lexparser/englishFactored.ser.gz");
        gramMap.put("pcfg", "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        gramMap.put("rnn", "edu/stanford/nlp/models/lexparser/englishRNN.ser.gz");

        for(Map.Entry<String, String> entry : gramMap.entrySet()){
            processText(entry.getValue(), args[0], entry.getKey());
        }
    }

    private static void processText(String grammar, String filePath, String outName) throws Exception{
        String outPath = "test-" + outName + ".out.txt";
        PrintWriter writer = new PrintWriter(new File(outPath));
        writer.println();
        writer.println("GRAMMAR: " + grammar + "\n");
        String[] options = {"-maxLength", "80", "-retainTmpSubcategories"};
        LexicalizedParser lp = LexicalizedParser.loadModel(grammar);
        TreebankLanguagePack tlp = lp.getOp().langpack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

        Iterable<List<? extends HasWord>> sentences;

        DocumentPreprocessor dp = new DocumentPreprocessor(filePath);
        List<List<? extends HasWord>> tmp = new ArrayList<>();
        for (List<HasWord> sentence : dp) {
            tmp.add(sentence);
        }
        sentences = tmp;


        for (List<? extends HasWord> sentence : sentences) {
            Tree parse = lp.parse(sentence);
            parse.pennPrint(writer);
            writer.println();
            GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
            List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
            writer.println(tdl);
            writer.println();

//            System.out.println("The words of the sentence:");
//            for (Label lab : parse.yield()) {
//                if (lab instanceof CoreLabel) {
//                    System.out.println(((CoreLabel) lab).toString(CoreLabel.OutputFormat.VALUE_MAP));
//                } else {
//                    System.out.println(lab);
//                }
//            }
//            System.out.println();
            writer.println(parse.taggedYield());
            writer.println();
        }
        writer.close();
    }

    private ParserTest() {
    } // static methods only

}
