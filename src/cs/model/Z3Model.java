package cs.model;

import com.microsoft.z3.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class Z3Model {
    protected Context ctx;
    protected HashMap<String, String> cfg;
    protected ArrayList<Sentence> sentences;

    public Z3Model(){
        cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        ctx = new Context(cfg);

        sentences = new ArrayList<Sentence>();
    }

    public void buildSentence(ArrayList<Constant> conditions, ArrayList<Constant> actions, boolean orCond, boolean orAct){
        Sentence sentence = new Sentence(ctx, conditions, actions, orCond, orAct);
        sentences.add(sentence);
    }

}
