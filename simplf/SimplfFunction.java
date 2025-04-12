package simplf;

import java.util.List;

class SimplfFunction implements SimplfCallable {
    private final Stmt.Function declaration;
    private Environment closure;

    SimplfFunction(Stmt.Function declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    public void setClosure(Environment environment) {
        this.closure = environment;
    }

    public Object call(Interpreter interpreter, List<Object> args) {
        if (args.size() != declaration.params.size()) {
            throw new RuntimeError(null, "Expected " + declaration.params.size() + " arguments but got " + args.size() + ".");
        }
    
        Environment environment = new Environment(this.closure);
        environment = environment.define(declaration.name, declaration.name.lexeme, this);
    
        for (int i = 0; i < declaration.params.size(); i++) {
            Object arg = args.get(i);
            if (arg instanceof Number) {
                arg = ((Number)arg).doubleValue();
            }
            environment = environment.define(
                declaration.params.get(i),
                declaration.params.get(i).lexeme,
                arg
            );
        }
    
        List<Stmt> stmts = declaration.body;
        for (int i = 0; i < stmts.size(); i++) {
            Stmt stmt = stmts.get(i);
            if (i == stmts.size() - 1 && stmt instanceof Stmt.Expression) {
                Object value = interpreter.evaluate(((Stmt.Expression) stmt).expr);
                return value;
            } else {
                interpreter.execute(stmt);
            }
        }
    
        return null;
    }
    
    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }
}
