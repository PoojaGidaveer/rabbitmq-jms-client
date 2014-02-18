/* Copyright (c) 2014 Pivotal Software, Inc. All rights reserved. */
package com.rabbitmq.jms.parse.sql;

import java.util.Map;

import com.rabbitmq.jms.parse.Evaluator;

/**
 * A boolean evaluator for JMS Sql selector expressions.
 *
 */
public class SqlEvaluator implements Evaluator<Boolean> {

    private final SqlEvaluatorVisitor evalVisitor = new SqlEvaluatorVisitor();
    private final SqlParseTree typedParseTree;
    private final String errorMessage;
    private final boolean evaluatorOk;

    public SqlEvaluator(SqlParser parser, Map<String, SqlExpressionType> identTypes) {
        if (parser.parseOk()) {
            SqlParseTree parseTree = parser.parse();
            if (this.evaluatorOk = canBeBool(SqlTypeChecker.deriveExpressionType(parseTree, identTypes))) {
                this.typedParseTree = parseTree;
                this.errorMessage = null;
            } else {
                this.errorMessage = "Type error in expression";
                this.typedParseTree = null;
            }
        } else {
           this.evaluatorOk = false;
           this.typedParseTree = null;
           this.errorMessage = parser.getErrorMessage();
        }
    }

    private static boolean canBeBool(SqlExpressionType set) {
        return (set == SqlExpressionType.BOOL || set == SqlExpressionType.ANY);
    }

    @Override
    public Boolean evaluate(Map<String, Object> env) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the type-checked parse tree used for evaluation; <code>null</code> if {@link #evaluatorOk()} is <code>false</code>.
     */
    public SqlParseTree typedParseTree() {
        return this.typedParseTree;
    }

    @Override
    public boolean evaluatorOk() {
        return this.evaluatorOk;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

}
