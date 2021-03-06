/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import expressivo.parser.ExpressionLexer;
import expressivo.parser.ExpressionParser;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS3 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition TODO
    //   Expression := SumExpression(a: Expression, b: Expression) +
    //      ProductExpression(a: Expression, b: Expression) +
    //      PrimitiveExpression(x: String)
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        CharStream stream = new ANTLRInputStream(input);
        ExpressionLexer lexer = new ExpressionLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokens);
        ParseTree tree = parser.expr();
        ParseTreeWalker walker = new ParseTreeWalker();
        MakeExpression maker = new MakeExpression();
        walker.walk(maker, tree);
        return maker.getExpression();
    }
    
    /**
     * Make a parsable String representation of the expression, with
     * a few rules according to the type of the expression:
     * For all expressions, get rid of all extra space
     * For product and sum expressions, wrap both terms in parentheses
     * For primitive expressions, don't use parentheses
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS3 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    /**
     * @param variable name on which we differentiate
     * 
     * @return Expression that's the result of
     * differentiating the current Expression
     */
    public Expression differentiate(String variable);
    
    /**
     * @param mapping variables to values
     * 
     * @return a simplification of the Expression
     * should be reduced to a single number if no variables remaining
     */
    public Expression simplify(Map<String, Double> environment);
    
    /**
     * @return a set of all the variables used in the current expression
     */
    public Set<String> variables();
    
    /**
     * @require variables to return an empty set
     * @return reduced expression to a single integer
     */
    public int reduce();
}
