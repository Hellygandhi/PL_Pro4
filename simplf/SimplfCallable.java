
package simplf;

import java.util.List;

interface SimplfCallable {
    Object call(Interpreter interpreter, List<Object> args);
    int arity(); // Add this to support argument count checking
    
}
