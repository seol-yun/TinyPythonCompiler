import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.w3c.dom.ls.LSOutput;
import java.util.*;


// if, elif, else, 중첩 if 구현
// def 구현 (return은 int만)
// Main영역은 main메서드로
// tpy에 구현할 수 있는 거의 대부분의 기능이 들어가 있습니다.

public class tinyPythonPrintListener extends tinyPythonBaseListener{
    public int load = 0;
    public int flagstatic =0;
    public Deque<Integer> dequeGoto = new ArrayDeque<>();
    public Map<String, Integer> dict = new HashMap<>();
    public Map<String, Integer> staticdict = new HashMap<>();
    int staticcount = 0;
    boolean main = true;
    @Override
    public void enterAssignment_stmt(tinyPythonParser.Assignment_stmtContext ctx) {
        if(flagstatic==0){
            if (!staticdict.containsKey(ctx.getChild(0).getText())) {
                staticdict.put(ctx.getChild(0).getText(), staticcount);
                staticcount++;
                System.out.print("ldc ");
                System.out.println(ctx.getChild(2).getText());
            }else{
                staticdict.put(ctx.getChild(0).getText(), staticdict.get(ctx.getChild(0).getText()));
            }
        }else{
            if(staticdict.containsKey(ctx.getChild(0).getText())){
                staticdict.put(ctx.getChild(0).getText(), staticdict.get(ctx.getChild(0).getText()));
            }
            else{
                if (!dict.containsKey(ctx.getChild(0).getText())) {
                    load+=1;
                    dict.put(ctx.getChild(0).getText(), load);
                } else{
                    dict.put(ctx.getChild(0).getText(), dict.get(ctx.getChild(0).getText()));
                }
            }

        }
    }
    @Override
    public void exitAssignment_stmt(tinyPythonParser.Assignment_stmtContext ctx) {
        if(staticdict.containsKey(ctx.getChild(0).getText())){
            System.out.println("putstatic Test/"+ ctx.getChild(0).getText()+ " I");
        }
        else {
            if (dict.containsKey(ctx.getChild(0).getText())) {
                System.out.println("istore_" + dict.get(ctx.getChild(0).getText()));
            }
        }
    }
    @Override
    public void enterExpr(tinyPythonParser.ExprContext ctx){
        if(flagstatic==1) {
            if (ctx.getChildCount() == 1) {
                System.out.println("ldc " + ctx.getChild(0).getText());
            } else if (ctx.getChildCount() == 2) {
                if (dict.containsKey(ctx.getChild(0).getText())) {
                    System.out.println("iload_" + dict.get(ctx.getChild(0).getText()));
                } else {
                }

            } else if (ctx.getChildCount() == 3) {

            }
        }

    }
    @Override
    public void exitExpr(tinyPythonParser.ExprContext ctx){
        if(flagstatic==1) {
            if (ctx.getChildCount() == 1) {
            } else if (ctx.getChildCount() == 2) {
                if (dict.containsKey(ctx.getChild(0).getText())) {
                } else {
                    if (staticdict.containsKey(ctx.getChild(0).getText())) {
                        System.out.println("getstatic Test/" + ctx.getChild(0).getText() + " I");
                    } else {
                        System.out.print("invokestatic Test/" + ctx.getChild(0).getText() + "(");
                        for (int i = 1; i < ctx.getChild(1).getChildCount() - 1; i += 2) {
                            System.out.print("I");
                        }
                        System.out.println(")I");
                    }

                }

            } else if (ctx.getChildCount() == 3) {
                if (ctx.getChild(1).getText().equals("+")) {
                    System.out.println("iadd");
                } else if (ctx.getChild(1).getText().equals("-")) {
                    System.out.println("isub");
                }
            }
        }
    }
    @Override
    public void enterOpt_paren(tinyPythonParser.Opt_parenContext ctx){
    }
    @Override
    public void exitOpt_paren(tinyPythonParser.Opt_parenContext ctx){
    }
    @Override
    public void enterPrint_stmt(tinyPythonParser.Print_stmtContext ctx){
        System.out.println("getstatic java/lang/System/out Ljava/io/PrintStream;");
    }
    @Override
    public void exitPrint_stmt(tinyPythonParser.Print_stmtContext ctx){
        if(ctx.getChild(1).getChild(0).getChildCount()!=0) {
            System.out.println("invokevirtual java/io/PrintStream/println(I)V");
        } else{
            System.out.println("invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
        }
    }
    @Override
    public void enterPrint_arg(tinyPythonParser.Print_argContext ctx){
        if(ctx.getChild(0).getChildCount()==0) {
            System.out.println("ldc " + ctx.getChild(0).getText());
        }

    }
    @Override
    public void enterFile_input(tinyPythonParser.File_inputContext ctx){
        System.out.println(".class public Test");
        System.out.println(".super java/lang/Object");
        System.out.println();
        int c=0;
        for(int i=1;i<ctx.getChildCount()-1;i++){
            if(ctx.getChild(i).getChildCount()==0){
                continue;
            }
            c++;
            if(!ctx.getChild(i).getChild(0).getChild(0).getChild(0).getText().equals("def") && !ctx.getChild(i).getChild(0).getChild(0).getChild(0).getText().equals("if") && !ctx.getChild(i).getChild(0).getChild(0).getChild(0).getText().equals("while")) {
                if(!staticdict.containsKey(ctx.getChild(i).getChild(0).getChild(0).getChild(0).getChild(0).getText())) {
                    System.out.print(".field public static ");
                    System.out.println(ctx.getChild(i).getChild(0).getChild(0).getChild(0).getChild(0).getText() + " I");
                    staticdict.put(ctx.getChild(i).getChild(0).getChild(0).getChild(0).getChild(0).getText(), -1);
                }
            }

        }

        System.out.println(".method static <clinit>()V");
        System.out.println("ldc 0");
        System.out.println(".limit stack 32");
        System.out.println(".limit locals 32");
    }
    @Override
    public void exitFile_input(tinyPythonParser.File_inputContext ctx){
        System.out.println("return");
        System.out.println(".end method");
        System.out.println();
        System.out.println("; standard initializer");
        System.out.println(".method public <init>()V");
        System.out.println("aload_0");
        System.out.println("invokespecial java/lang/Object/<init>()V");
        System.out.println("return");
        System.out.println(".end method");
        System.out.println();


    }
    public void enterStmt(tinyPythonParser.StmtContext ctx){
        if(!ctx.getParent().getParent().getChild(0).getText().equals("def")
                && !ctx.getParent().getParent().getChild(0).getText().equals("if")
                && main==true){
            System.out.println("return");
            System.out.println(".end method");
            System.out.println();
            flagstatic=1;
            main =false;

            System.out.println(".method public static main([Ljava/lang/String;)V");
            System.out.println(".limit stack 32");
            System.out.println(".limit locals 32");
        }
    }
    public void exitStmt(tinyPythonParser.StmtContext ctx){

    }
    public void enterSimple_stmt(tinyPythonParser.Simple_stmtContext ctx){

    }

    /**
     * Exit a parse tree produced by {@link tinyPythonParser#simple_stmt}.
     * @param ctx the parse tree
     */
    public void exitSimple_stmt(tinyPythonParser.Simple_stmtContext ctx){

    }
    @Override public void enterDefs(tinyPythonParser.DefsContext ctx) {

    }
    @Override public void exitDefs(tinyPythonParser.DefsContext ctx) {

    }
    @Override
    public void enterDef_stmt(tinyPythonParser.Def_stmtContext ctx){
        System.out.println("return");
        System.out.println(".end method");

        System.out.println();

        flagstatic=1;
//        System.out.print(ctx.getChild(1).getText()+ ctx.getChild(2).getText());
//        if(ctx.getChild(1).getText().equals("Main")){
//            System.out.println(".method public static main([Ljava/lang/String;)V");
//            System.out.println(".limit stack 32");
//            System.out.println(".limit locals 32");
//        } else{
            System.out.print(".method public static "+ctx.getChild(1).getText()+"(");
            for (int i = 0; i < ctx.getChild(3).getChildCount(); i+=2) {
                System.out.print("I");
            }
            System.out.println(")I");
            System.out.println(".limit stack 32");
            System.out.println(".limit locals 32");
//        }

    }
    @Override
    public void exitDef_stmt(tinyPythonParser.Def_stmtContext ctx) {
        load=0;
        dict = new HashMap<>();
        flagstatic=0;
    }
    @Override
    public void enterSuite(tinyPythonParser.SuiteContext ctx){
        int count = ctx.getParent().getChildCount();
        int index=0;
        for (int i = 0; i < count; i++) {
            if(ctx.getParent().getChild(i)==ctx){
                index=i;
            }
        }
        if(!ctx.getParent().getChild(0).getText().equals("def")){
            if(!ctx.getParent().getChild(index-2).getText().equals("else")) {
                if (ctx.getParent().getChild(index - 2).getChild(1).getText().equals("==")) {
                    System.out.print("if_icmpne ");
                } else if (ctx.getParent().getChild(index - 2).getChild(1).getText().equals("<")) {
                    System.out.print("if_icmpge ");
                } else if (ctx.getParent().getChild(index - 2).getChild(1).getText().equals(">")) {
                    System.out.print("if_icmple ");
                } else if (ctx.getParent().getChild(index - 2).getChild(1).getText().equals("<=")) {
                    System.out.print("if_icmpgt ");
                } else if (ctx.getParent().getChild(index - 2).getChild(1).getText().equals(">=")) {
                    System.out.print("if_icmplt ");
                } else if (ctx.getParent().getChild(index - 2).getChild(1).getText().equals("!=")) {
                    System.out.print("if_icmpeq ");
                }
                System.out.print("L");
                Iterator<Integer> reverseIterator = dequeGoto.iterator();
                while (reverseIterator.hasNext()) {
                    System.out.print(reverseIterator.next());
                }
                if(ctx.getParent().getChild(0).getText().equals("while")) {
                    System.out.print("@");
                }
                System.out.println();
            }
        }



    }
    public void exitSuite(tinyPythonParser.SuiteContext ctx){
        int count = ctx.getParent().getChildCount();
        if(count==4){
            count=0;
        }else{
            count = count/4;
        }

        if(ctx.getParent().getChild(0).getText().equals("if")) {
            System.out.print("goto L");
            Iterator<Integer> reverseIterator1 = dequeGoto.iterator();
            int flag=1;
            while (reverseIterator1.hasNext()) {
                if(flag==dequeGoto.size()){
                    break;
                }
                int s = reverseIterator1.next();
                System.out.print(s);
                flag++;

            }
            System.out.println(count);

            System.out.print("L");
            Iterator<Integer> reverseIterator = dequeGoto.iterator();
            while (reverseIterator.hasNext()) {
                System.out.print(reverseIterator.next());
            }
            if(ctx.getParent().getChild(0).getText().equals("if")){
                int temp = dequeGoto.pollLast();
                dequeGoto.addLast(temp + 1);
            }
            System.out.println(":");
        }
    }

    @Override
    public void enterReturn_stmt(tinyPythonParser.Return_stmtContext ctx){
    }
    @Override
    public void exitReturn_stmt(tinyPythonParser.Return_stmtContext ctx){
        System.out.println("ireturn");
    }

    @Override
    public void enterArgs(tinyPythonParser.ArgsContext ctx){
        int count = ctx.getChildCount();
        for (int i = 0; i < count; i+=2) {
            dict.put(ctx.getChild(i).getText(), load);
            load++;
        }
    }


    @Override
    public void enterIf_stmt(tinyPythonParser.If_stmtContext ctx){
        dequeGoto.addLast(0);
    }
    @Override
    public void exitIf_stmt(tinyPythonParser.If_stmtContext ctx) {
        dequeGoto.pollLast();
    }
    @Override
    public void enterComp_op(tinyPythonParser.Comp_opContext ctx){
    }

    @Override
    public void enterBreak_stmt(tinyPythonParser.Break_stmtContext ctx) {

    }

    public void exitComp_op(tinyPythonParser.Comp_opContext ctx){
    }
    @Override
    public void enterWhile_stmt(tinyPythonParser.While_stmtContext ctx){
        dequeGoto.addLast(0);
        System.out.print("L");
        Iterator<Integer> reverseIterator = dequeGoto.iterator();
        while (reverseIterator.hasNext()) {
            System.out.print(reverseIterator.next());
        }
        System.out.println(":");
    }
    @Override
    public void exitWhile_stmt(tinyPythonParser.While_stmtContext ctx){
        System.out.print("goto L");
        Iterator<Integer> reverseIterator = dequeGoto.iterator();
        while (reverseIterator.hasNext()) {
            System.out.print(reverseIterator.next());
        }
        System.out.println();
        System.out.print("L");
        reverseIterator = dequeGoto.iterator();
        while (reverseIterator.hasNext()) {
            System.out.print(reverseIterator.next());
        }
        System.out.println("@:");
        dequeGoto.pollLast();
    }

    @Override
    public void exitBreak_stmt(tinyPythonParser.Break_stmtContext ctx) {
        System.out.print("goto L");
        int dequeSize = dequeGoto.size();
        Iterator<Integer> reverseIterator = dequeGoto.iterator();
        int cnt=0;
        while (reverseIterator.hasNext()) {
            cnt++;
            if(cnt<dequeSize+1) {
                System.out.print(reverseIterator.next());
            }else{
                reverseIterator.next();
            }
        }
        System.out.print("@");
        System.out.println();
    }

    @Override
    public void exitContinue_stmt(tinyPythonParser.Continue_stmtContext ctx) {
        System.out.print("goto L");
        int dequeSize = dequeGoto.size();
        Iterator<Integer> reverseIterator = dequeGoto.iterator();
        int cnt=0;
        while (reverseIterator.hasNext()) {
            cnt++;
            if(cnt<dequeSize+1) {
                System.out.print(reverseIterator.next());
            }else{
                reverseIterator.next();
            }
        }
        System.out.println();

    }
}
