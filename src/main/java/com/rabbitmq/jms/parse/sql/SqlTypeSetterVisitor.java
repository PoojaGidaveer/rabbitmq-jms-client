package com.rabbitmq.jms.parse.sql;

import java.util.Collections;
import java.util.Map;

import com.rabbitmq.jms.parse.Visitor;


/**
 * This visitor sets the type of the expression at a node of a tree, using the type of node
 * and the {@link SqlExpressionType} of the children nodes.
 * <p>
 * To type a whole tree this can be applied to it by visiting the subtrees in ‘postorder’; each node visited
 * then has its child nodes already visited and therefore typed.
 * </p>
 * <p>
 * The visitor has a (final) map state which pre-specifies the {@link SqlExpressionType} of some identifiers,
 * otherwise identifiers are of type {@link SqlExpressionType SqlExpressionType.ANY}.
 * </p>
 */
public class SqlTypeSetterVisitor implements Visitor<SqlTreeNode> {

    private final Map<String, SqlExpressionType> identifierType;

    public SqlTypeSetterVisitor(Map<String, SqlExpressionType> identifierType) {
        if (identifierType == null) throw new IllegalArgumentException("Identifier Type map cannot be null");
        this.identifierType = identifierType;
    }

    public SqlTypeSetterVisitor() {
        this(Collections.<String, SqlExpressionType> emptyMap());
    }

    @Override
    public boolean visit(SqlTreeNode parent, SqlTreeNode[] children) {
        parent.setExpType(typeOf(parent, children));
        return true; // traverse the whole tree
    }

    private final SqlExpressionType typeOf(SqlTreeNode parent, SqlTreeNode[] children) {
        switch (parent.treeType()) {
        case BINARYOP:
            switch (parent.value().type()) {
            case CMP_EQ:
            case CMP_NEQ:
                return equalTypeBool(children[0].getExpType(), children[1].getExpType());
            case CMP_GT:
            case CMP_GTEQ:
            case CMP_LT:
            case CMP_LTEQ:
                return arithTypeBool(children[0].getExpType(), children[1].getExpType());
            case LIKE:
            case NOT_LIKE:
            case IN:
            case NOT_IN:
                return stringTypeBool(children[0].getExpType());
            case OP_DIV:
            case OP_MINUS:
            case OP_MULT:
            case OP_PLUS:
                return arithTypeArith(children[0].getExpType(), children[1].getExpType());
            default:
                break;
            }
            break;
        case CONJUNCTION:
        case DISJUNCTION:
            return boolTypeBool(children[0].getExpType(), children[1].getExpType());
        case LEAF:
            switch (parent.value().type()) {
            case TRUE:
            case FALSE: return SqlExpressionType.BOOL;
            case FLOAT:
            case HEX:
            case INT:   return SqlExpressionType.ARITH;
            case IDENT: return identifierType(parent.value().getIdent());
            case LIST:  return SqlExpressionType.LIST;
            case STRING:return SqlExpressionType.STRING;
            default:
                break;
            }
            break;
        case POSTFIXUNARYOP:
            return SqlExpressionType.BOOL;
        case PREFIXUNARYOP:
            switch (parent.value().type()) {
            case NOT:
                return boolTypeBool(children[0].getExpType());
            case OP_MINUS:
            case OP_PLUS:
                return arithTypeArith(children[0].getExpType());
            default:
                break;
            }
            break;
        case TERNARYOP:
            return arithTypeBool(children[0].getExpType(), children[1].getExpType(), children[2].getExpType());
        // Following cases should not affect the result:
        case PATTERN1:
        case PATTERN2:
        case LIST:
            break;
        // Following cases should not appear:
        case COLLAPSE1:
        case COLLAPSE2:
        case JOINLIST:
            throw new RuntimeException("Node type:"+parent.treeType()+" should not occur");
        }
        return SqlExpressionType.NOT_SET;
    }

    private final SqlExpressionType identifierType(String ident) {
        if (identifierType.containsKey(ident)) return identifierType.get(ident);
        return SqlExpressionType.ANY;
    }

    private static SqlExpressionType arithTypeBool(SqlExpressionType expType, SqlExpressionType expType2,
                                                    SqlExpressionType expType3) {
        if (SqlExpressionType.isArith(expType) && SqlExpressionType.isArith(expType2) && SqlExpressionType.isArith(expType3)) return SqlExpressionType.BOOL;
        return SqlExpressionType.INVALID;
    }

    private static SqlExpressionType boolTypeBool(SqlExpressionType expType, SqlExpressionType expType2) {
        if (SqlExpressionType.isBool(expType) && SqlExpressionType.isBool(expType2)) return SqlExpressionType.BOOL;
        return SqlExpressionType.INVALID;
    }

    private static SqlExpressionType arithTypeArith(SqlExpressionType expType) {
        if (SqlExpressionType.isArith(expType)) return SqlExpressionType.ARITH;
        return SqlExpressionType.INVALID;
    }

    private static SqlExpressionType arithTypeArith(SqlExpressionType expType, SqlExpressionType expType2) {
        if (SqlExpressionType.isArith(expType) && SqlExpressionType.isArith(expType2)) return SqlExpressionType.ARITH;
        return SqlExpressionType.INVALID;
    }

    private static SqlExpressionType stringTypeBool(SqlExpressionType expType) {
        if (SqlExpressionType.isString(expType)) return SqlExpressionType.BOOL;
        return SqlExpressionType.INVALID;
    }

    private static SqlExpressionType boolTypeBool(SqlExpressionType expType) {
        if (SqlExpressionType.isBool(expType)) return SqlExpressionType.BOOL;
        return SqlExpressionType.INVALID;
    }

    private static SqlExpressionType arithTypeBool(SqlExpressionType expType, SqlExpressionType expType2) {
        if (SqlExpressionType.isArith(expType) && SqlExpressionType.isArith(expType2)) return SqlExpressionType.BOOL;
        return SqlExpressionType.INVALID;
    }

    private static SqlExpressionType equalTypeBool(SqlExpressionType expType, SqlExpressionType expType2) {
        if (SqlExpressionType.isBool(expType) && SqlExpressionType.isBool(expType2)) return SqlExpressionType.BOOL;
        if (SqlExpressionType.isArith(expType) && SqlExpressionType.isArith(expType2)) return SqlExpressionType.BOOL;
        if (SqlExpressionType.isString(expType) && SqlExpressionType.isString(expType2)) return SqlExpressionType.BOOL;
        return SqlExpressionType.INVALID;
    }
}